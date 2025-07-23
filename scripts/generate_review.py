"""
Generate automated PR review using stored memory embeddings
"""

import os
import json
import sys
from github import Github
from claude_embeddings import ClaudeEmbeddingClient, ReviewEmbeddingManager
from faiss_memory_manager import FAISSMemoryManager
import requests
from typing import List, Dict, Tuple


def get_pr_changes():
    """Get PR changes and file diffs"""
    
    github_token = os.getenv('GITHUB_TOKEN')
    pr_number = int(os.getenv('PR_NUMBER'))
    repo_name = os.getenv('REPO_NAME')
    base_sha = os.getenv('BASE_SHA')
    head_sha = os.getenv('HEAD_SHA')
    
    g = Github(github_token)
    repo = g.get_repo(repo_name)
    pr = repo.get_pull(pr_number)
    
    # Get changed files
    files = pr.get_files()
    
    changes = []
    for file in files:
        if file.patch and file.status in ['modified', 'added']:
            # Split patch into chunks for better processing
            chunks = split_patch_into_chunks(file.patch, file.filename)
            changes.extend(chunks)
    
    return changes, pr


def split_patch_into_chunks(patch: str, filename: str, max_lines: int = 20) -> List[Dict]:
    """Split large patches into smaller reviewable chunks"""
    
    chunks = []
    lines = patch.split('\n')
    current_chunk = []
    hunk_header = None
    
    for line in lines:
        if line.startswith('@@'):
            # Save previous chunk if exists
            if current_chunk and hunk_header:
                chunks.append({
                    'filename': filename,
                    'hunk_header': hunk_header,
                    'code_chunk': '\n'.join(current_chunk),
                    'added_lines': [l for l in current_chunk if l.startswith('+')],
                    'removed_lines': [l for l in current_chunk if l.startswith('-')]
                })
            
            # Start new chunk
            hunk_header = line
            current_chunk = []
        else:
            current_chunk.append(line)
            
            # Split large chunks
            if len(current_chunk) >= max_lines and any(l.startswith(('+', '-')) for l in current_chunk[-5:]):
                if hunk_header:
                    chunks.append({
                        'filename': filename,
                        'hunk_header': hunk_header,
                        'code_chunk': '\n'.join(current_chunk),
                        'added_lines': [l for l in current_chunk if l.startswith('+')],
                        'removed_lines': [l for l in current_chunk if l.startswith('-')]
                    })
                current_chunk = []
    
    # Add final chunk
    if current_chunk and hunk_header:
        chunks.append({
            'filename': filename,
            'hunk_header': hunk_header,
            'code_chunk': '\n'.join(current_chunk),
            'added_lines': [l for l in current_chunk if l.startswith('+')],
            'removed_lines': [l for l in current_chunk if l.startswith('-')]
        })
    
    return chunks


def find_similar_past_reviews(code_chunks: List[Dict], memory_manager: FAISSMemoryManager, claude_client: ClaudeEmbeddingClient) -> List[Dict]:
    """Find similar past reviews for code chunks"""
    
    review_suggestions = []
    
    for chunk in code_chunks:
        # Generate embedding for current code chunk
        context = f"File: {chunk['filename']}\nChanges: +{len(chunk['added_lines'])} -{len(chunk['removed_lines'])}"
        query_embedding = claude_client.generate_review_embedding(chunk['code_chunk'], context)
        
        # Search for similar reviews
        similar_reviews = memory_manager.search_similar(
            query_embedding, 
            top_k=3, 
            min_similarity=0.4
        )
        
        if similar_reviews:
            suggestion = {
                'file': chunk['filename'],
                'hunk_header': chunk['hunk_header'],
                'code_chunk': chunk['code_chunk'],
                'similar_reviews': []
            }
            
            for review_embedding, similarity in similar_reviews:
                suggestion['similar_reviews'].append({
                    'similarity': similarity,
                    'comment': review_embedding.review_comment,
                    'reviewer': review_embedding.reviewer,
                    'tags': review_embedding.tags,
                    'original_code': review_embedding.code_chunk[:200] + "..." if len(review_embedding.code_chunk) > 200 else review_embedding.code_chunk
                })
            
            review_suggestions.append(suggestion)
    
    return review_suggestions


def generate_review_comments_with_claude(review_suggestions: List[Dict], pr_info: Dict) -> List[Dict]:
    """Use Claude to generate contextual review comments based on similar past reviews"""
    
    anthropic_api_key = os.getenv('ANTHROPIC_API_KEY')
    if not anthropic_api_key:
        print("ANTHROPIC_API_KEY not found")
        return []
    
    review_comments = []
    
    for suggestion in review_suggestions:
        # Build prompt for Claude
        similar_reviews_text = ""
        for sr in suggestion['similar_reviews']:
            similar_reviews_text += f"""
Past Review (similarity: {sr['similarity']:.2f}):
Reviewer: {sr['reviewer']}
Tags: {', '.join(sr['tags'])}
Comment: {sr['comment']}
Original code context: {sr['original_code']}
---
"""
        
        prompt = f"""You are a code reviewer analyzing a pull request. Based on similar past reviews, provide a helpful review comment for the following code change.

PR Information:
- Title: {pr_info.get('title', 'N/A')}
- File: {suggestion['file']}

Current Code Change:
{suggestion['code_chunk']}

Similar Past Reviews Found:
{similar_reviews_text}

Based on these similar past reviews, generate a concise, helpful review comment for the current code change. Focus on:
1. Specific issues that might apply to this code
2. Best practices from past reviews
3. Consistency with previous feedback patterns

If the similar reviews don't apply well to the current code, indicate that no review is needed.

Response format: Provide only the review comment text, or "NO_REVIEW_NEEDED" if not applicable."""

        try:
            # Call Claude API (using requests since anthropic library might not have the exact endpoint we need)
            headers = {
                "Content-Type": "application/json",
                "x-api-key": anthropic_api_key,
                "anthropic-version": "2023-06-01"
            }
            
            data = {
                "model": "claude-3-sonnet-20240229",
                "max_tokens": 300,
                "messages": [
                    {
                        "role": "user",
                        "content": prompt
                    }
                ]
            }
            
            response = requests.post(
                "https://api.anthropic.com/v1/messages",
                headers=headers,
                json=data,
                timeout=30
            )
            
            if response.status_code == 200:
                result = response.json()
                comment_text = result['content'][0]['text'].strip()
                
                if comment_text and comment_text != "NO_REVIEW_NEEDED":
                    # Extract line information for inline comments
                    line_number = extract_line_number_from_hunk(suggestion['hunk_header'])
                    
                    review_comments.append({
                        'file': suggestion['file'],
                        'line': line_number,
                        'comment': comment_text,
                        'similarity_info': [
                            {
                                'similarity': sr['similarity'],
                                'reviewer': sr['reviewer'],
                                'tags': sr['tags']
                            } for sr in suggestion['similar_reviews']
                        ]
                    })
                    
                    print(f"Generated review for {suggestion['file']}:{line_number}")
            else:
                print(f"Error calling Claude API: {response.status_code} - {response.text}")
                
        except Exception as e:
            print(f"Error generating review comment: {e}")
            continue
    
    return review_comments


def extract_line_number_from_hunk(hunk_header: str) -> int:
    """Extract line number from git hunk header"""
    try:
        # Parse hunk header like "@@ -10,7 +10,7 @@"
        parts = hunk_header.split()
        new_range = parts[1]  # +10,7 part
        line_num = int(new_range.split(',')[0].replace('+', ''))
        return line_num
    except:
        return 1


def main():
    """Main execution function"""
    
    print("🔍 Analyzing PR for auto-review...")
    
    # Load memory
    memory_manager = FAISSMemoryManager()
    index_path = "memory_data/faiss.index"
    metadata_path = "memory_data/metadata.json"
    
    # Configure git first
    os.system("git config --global user.email 'action@github.com'")
    os.system("git config --global user.name 'GitHub Action'")
    
    # Switch to memory branch to load data
    os.system("git checkout memory")
    
    if not os.path.exists(index_path) or not os.path.exists(metadata_path):
        print("📭 No memory data found, skipping auto-review")
        return
    
    memory_manager.load_from_files(index_path, metadata_path)
    print(f"📚 Loaded memory with {memory_manager.index.ntotal} past reviews")
    
    if memory_manager.index.ntotal == 0:
        print("📭 Empty memory, skipping auto-review")
        return
    
    # Switch back to PR branch
    os.system("git checkout -")
    
    # Get PR changes
    try:
        code_chunks, pr = get_pr_changes()
        print(f"📝 Found {len(code_chunks)} code chunks to review")
        
        if not code_chunks:
            print("📭 No reviewable changes found")
            return
        
    except Exception as e:
        print(f"❌ Error getting PR changes: {e}")
        return
    
    # Initialize Claude client
    anthropic_api_key = os.getenv('ANTHROPIC_API_KEY')
    claude_client = ClaudeEmbeddingClient(anthropic_api_key)
    
    # Find similar past reviews
    review_suggestions = find_similar_past_reviews(code_chunks, memory_manager, claude_client)
    print(f"🔎 Found {len(review_suggestions)} chunks with similar past reviews")
    
    if not review_suggestions:
        print("📭 No similar past reviews found")
        return
    
    # Generate review comments using Claude
    pr_info = {
        'title': pr.title,
        'number': pr.number,
        'author': pr.user.login
    }
    
    review_comments = generate_review_comments_with_claude(review_suggestions, pr_info)
    print(f"💬 Generated {len(review_comments)} review comments")
    
    # Save generated review
    if review_comments:
        review_data = {
            'pr_number': pr.number,
            'comments': review_comments,
            'metadata': {
                'total_chunks_analyzed': len(code_chunks),
                'chunks_with_similar_reviews': len(review_suggestions),
                'comments_generated': len(review_comments),
                'memory_size': memory_manager.index.ntotal
            }
        }
        
        with open('generated_review.json', 'w') as f:
            json.dump(review_data, f, indent=2)
        
        print("✅ Review generated successfully")
    else:
        print("📭 No review comments generated")


if __name__ == "__main__":
    main()