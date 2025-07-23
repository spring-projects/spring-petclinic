"""
Extract PR review data and convert to embeddings for memory storage
"""

import os
import json
import sys
from github import Github
from claude_embeddings import ClaudeEmbeddingClient, ReviewEmbeddingManager
from faiss_memory_manager import FAISSMemoryManager
import tempfile
from pathlib import Path


def get_pr_diff_context(repo, pr_number, file_path, line_number, context_lines=3):
    """Get code context around a specific line in PR diff"""
    try:
        pr = repo.get_pull(pr_number)
        files = pr.get_files()
        
        for file in files:
            if file.filename == file_path:
                # Get the patch content
                patch = file.patch
                if not patch:
                    continue
                    
                lines = patch.split('\n')
                target_line = None
                context_start = 0
                
                # Find the line in the diff
                for i, line in enumerate(lines):
                    if line.startswith('@@'):
                        # Parse hunk header to understand line numbers
                        continue
                    elif line.startswith('+') or line.startswith('-') or line.startswith(' '):
                        # This is a code line
                        if target_line is None:
                            target_line = i
                        if abs(i - target_line) <= context_lines:
                            context_start = max(0, target_line - context_lines)
                
                # Extract context
                context_lines_data = lines[context_start:target_line + context_lines + 1]
                return '\n'.join(context_lines_data)
                
        return None
    except Exception as e:
        print(f"Error getting diff context: {e}")
        return None


def extract_review_from_event():
    """Extract review data from GitHub event"""
    
    # Get environment variables
    github_token = os.getenv('GITHUB_TOKEN')
    pr_number = int(os.getenv('PR_NUMBER'))
    repo_name = os.getenv('REPO_NAME')
    review_id = os.getenv('REVIEW_ID')
    event_type = os.getenv('EVENT_TYPE')
    
    if not all([github_token, pr_number, repo_name, review_id]):
        print("Missing required environment variables")
        return None
    
    # Initialize GitHub client
    g = Github(github_token)
    repo = g.get_repo(repo_name)
    pr = repo.get_pull(pr_number)
    
    extracted_reviews = []
    
    try:
        if event_type == 'pull_request_review':
            # Handle PR review
            review = pr.get_review(int(review_id))
            
            # Get review comments (inline comments)
            review_comments = review.get_comments()
            
            for comment in review_comments:
                # Get code context
                code_context = get_pr_diff_context(
                    repo, pr_number, comment.path, comment.position
                )
                
                if code_context:
                    review_data = {
                        'code_chunk': code_context,
                        'review_comment': comment.body,
                        'reviewer': review.user.login,
                        'file_path': comment.path,
                        'line_number': comment.position,
                        'pr_info': {
                            'repo': repo_name,
                            'pr_number': pr_number,
                            'title': pr.title,
                            'files': [f.filename for f in pr.get_files()]
                        }
                    }
                    extracted_reviews.append(review_data)
            
            # Also handle general review comment if exists
            if review.body and review.body.strip():
                # For general reviews, get overall PR context
                pr_files = pr.get_files()
                main_changes = []
                
                for file in list(pr_files)[:3]:  # Limit to first 3 files
                    if file.patch:
                        main_changes.append(f"File: {file.filename}\n{file.patch[:500]}...")
                
                if main_changes:
                    review_data = {
                        'code_chunk': '\n\n'.join(main_changes),
                        'review_comment': review.body,
                        'reviewer': review.user.login,
                        'file_path': 'general',
                        'line_number': None,
                        'pr_info': {
                            'repo': repo_name,
                            'pr_number': pr_number,
                            'title': pr.title,
                            'files': [f.filename for f in pr.get_files()]
                        }
                    }
                    extracted_reviews.append(review_data)
                    
        elif event_type == 'pull_request_review_comment':
            # Handle individual review comment
            comment = pr.get_review_comment(int(review_id))
            
            code_context = get_pr_diff_context(
                repo, pr_number, comment.path, comment.position
            )
            
            if code_context:
                review_data = {
                    'code_chunk': code_context,
                    'review_comment': comment.body,
                    'reviewer': comment.user.login,
                    'file_path': comment.path,
                    'line_number': comment.position,
                    'pr_info': {
                        'repo': repo_name,
                        'pr_number': pr_number,
                        'title': pr.title,
                        'files': [f.filename for f in pr.get_files()]
                    }
                }
                extracted_reviews.append(review_data)
        
        return extracted_reviews
        
    except Exception as e:
        print(f"Error extracting review: {e}")
        return None


def process_and_store_reviews(review_data_list):
    """Convert review data to embeddings and store in memory"""
    
    anthropic_api_key = os.getenv('ANTHROPIC_API_KEY')
    if not anthropic_api_key:
        print("ANTHROPIC_API_KEY not found, cannot generate embeddings")
        return False
    
    # Initialize embedding components
    claude_client = ClaudeEmbeddingClient(anthropic_api_key)
    embedding_manager = ReviewEmbeddingManager(claude_client)
    
    # Load existing memory
    memory_manager = FAISSMemoryManager()
    
    # Try to load existing memory data
    index_path = "memory_data/faiss.index"
    metadata_path = "memory_data/metadata.json"
    
    if os.path.exists(index_path) and os.path.exists(metadata_path):
        memory_manager.load_from_files(index_path, metadata_path)
        print(f"Loaded existing memory with {memory_manager.index.ntotal} embeddings")
    
    # Process new reviews
    new_embeddings = []
    for review_data in review_data_list:
        try:
            embedding = embedding_manager.create_review_embedding(
                code_chunk=review_data['code_chunk'],
                review_comment=review_data['review_comment'],
                pr_info=review_data['pr_info'],
                reviewer=review_data['reviewer']
            )
            new_embeddings.append(embedding)
            print(f"Created embedding for review: {embedding.id}")
            
        except Exception as e:
            print(f"Error creating embedding: {e}")
            continue
    
    if new_embeddings:
        # Add new embeddings to memory
        added_count = memory_manager.add_embeddings_batch(new_embeddings)
        
        # Save updated memory
        os.makedirs("memory_data", exist_ok=True)
        memory_manager.save_to_files(index_path, metadata_path)
        
        print(f"Added {added_count} new embeddings to memory")
        return True
    else:
        print("No new embeddings to add")
        return False


def main():
    """Main execution function"""
    
    print("🔍 Extracting review data...")
    
    # Extract review data from GitHub event
    review_data_list = extract_review_from_event()
    
    if not review_data_list:
        print("❌ No review data extracted")
        return
    
    print(f"📝 Found {len(review_data_list)} review items")
    
    # Process and store reviews
    success = process_and_store_reviews(review_data_list)
    
    if success:
        print("✅ Reviews processed and stored successfully")
    else:
        print("⚠️ No new reviews were stored")


if __name__ == "__main__":
    main()