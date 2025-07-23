"""
Claude Embeddings Utility for PR Review Memory System
Handles embedding generation and management using Anthropic Claude API
"""

import os
import json
import hashlib
import numpy as np
from typing import List, Dict, Optional, Tuple
import requests
import time
from dataclasses import dataclass, asdict
from datetime import datetime


@dataclass
class ReviewEmbedding:
    """Structure for storing review embeddings and metadata"""
    id: str
    pr_info: Dict
    code_chunk: str
    review_comment: str
    reviewer: str
    timestamp: str
    embedding_vector: List[float]
    tags: List[str]
    similarity_score: Optional[float] = None


class ClaudeEmbeddingClient:
    """Client for generating embeddings using Claude API"""
    
    def __init__(self, api_key: str, base_url: str = "https://api.anthropic.com"):
        self.api_key = api_key
        self.base_url = base_url
        self.headers = {
            "Content-Type": "application/json",
            "x-api-key": api_key,
            "anthropic-version": "2023-06-01"
        }
        
    def generate_embedding(self, text: str, model: str = "claude-3-haiku-20240307") -> List[float]:
        """
        Generate embedding for given text using Claude API
        Note: This is a placeholder - actual Claude API might not have direct embedding endpoint
        In practice, you might need to use a different embedding service or implement text processing
        """
        # For now, we'll use a simple hash-based approach as placeholder
        # In production, replace with actual embedding API call
        text_hash = hashlib.sha256(text.encode()).hexdigest()
        
        # Simulate embedding generation (replace with actual API call)
        embedding = self._simulate_embedding(text)
        
        return embedding
    
    def _simulate_embedding(self, text: str, dimension: int = 768) -> List[float]:
        """
        Simulate embedding generation for development/testing
        Replace this with actual Claude API call when available
        """
        # Create deterministic embedding based on text content
        text_hash = hashlib.sha256(text.encode()).digest()
        
        # Convert hash to float values
        embedding = []
        for i in range(0, min(len(text_hash), dimension // 8)):
            chunk = text_hash[i:i+8] if i+8 <= len(text_hash) else text_hash[i:]
            chunk = chunk.ljust(8, b'\x00')
            value = int.from_bytes(chunk[:4], 'big') / (2**32)
            embedding.append(value - 0.5)  # Center around 0
        
        # Pad or truncate to desired dimension
        while len(embedding) < dimension:
            embedding.append(0.0)
        
        # Normalize vector
        norm = np.linalg.norm(embedding)
        if norm > 0:
            embedding = [x / norm for x in embedding]
            
        return embedding[:dimension]
    
    def generate_review_embedding(self, code_chunk: str, context: str = "") -> List[float]:
        """
        Generate embedding specifically for code review context
        Combines code and contextual information
        """
        combined_text = f"Code:\n{code_chunk}\n\nContext:\n{context}"
        return self.generate_embedding(combined_text)


class ReviewEmbeddingManager:
    """Manages review embeddings storage and retrieval"""
    
    def __init__(self, claude_client: ClaudeEmbeddingClient):
        self.claude_client = claude_client
        
    def create_review_embedding(
        self,
        code_chunk: str,
        review_comment: str,
        pr_info: Dict,
        reviewer: str,
        tags: List[str] = None
    ) -> ReviewEmbedding:
        """Create a new review embedding from PR review data"""
        
        # Generate unique ID
        content_hash = hashlib.sha256(
            f"{code_chunk}{review_comment}{pr_info.get('pr_number', '')}".encode()
        ).hexdigest()[:16]
        
        # Generate embedding
        context = f"PR #{pr_info.get('pr_number', 'unknown')} in {pr_info.get('repo', 'unknown')}"
        embedding_vector = self.claude_client.generate_review_embedding(code_chunk, context)
        
        # Extract tags from review comment if not provided
        if tags is None:
            tags = self._extract_tags_from_comment(review_comment)
            
        return ReviewEmbedding(
            id=content_hash,
            pr_info=pr_info,
            code_chunk=code_chunk,
            review_comment=review_comment,
            reviewer=reviewer,
            timestamp=datetime.utcnow().isoformat(),
            embedding_vector=embedding_vector,
            tags=tags
        )
    
    def _extract_tags_from_comment(self, comment: str) -> List[str]:
        """Extract relevant tags from review comment text"""
        tags = []
        comment_lower = comment.lower()
        
        # Security related
        if any(word in comment_lower for word in ['security', 'vulnerable', 'injection', 'xss', 'csrf']):
            tags.append('security')
            
        # Performance related
        if any(word in comment_lower for word in ['performance', 'slow', 'optimize', 'memory', 'cpu']):
            tags.append('performance')
            
        # Code style
        if any(word in comment_lower for word in ['style', 'format', 'naming', 'convention']):
            tags.append('style')
            
        # Architecture
        if any(word in comment_lower for word in ['architecture', 'design', 'pattern', 'structure']):
            tags.append('architecture')
            
        # Testing
        if any(word in comment_lower for word in ['test', 'coverage', 'mock', 'assertion']):
            tags.append('testing')
            
        # Documentation
        if any(word in comment_lower for word in ['document', 'comment', 'javadoc', 'readme']):
            tags.append('documentation')
            
        return tags if tags else ['general']
    
    def calculate_similarity(self, embedding1: List[float], embedding2: List[float]) -> float:
        """Calculate cosine similarity between two embeddings"""
        vec1 = np.array(embedding1)
        vec2 = np.array(embedding2)
        
        dot_product = np.dot(vec1, vec2)
        norm1 = np.linalg.norm(vec1)
        norm2 = np.linalg.norm(vec2)
        
        if norm1 == 0 or norm2 == 0:
            return 0.0
            
        return dot_product / (norm1 * norm2)
    
    def find_similar_reviews(
        self,
        query_embedding: List[float],
        review_embeddings: List[ReviewEmbedding],
        top_k: int = 5,
        min_similarity: float = 0.3
    ) -> List[ReviewEmbedding]:
        """Find most similar review embeddings to query"""
        
        similarities = []
        for review in review_embeddings:
            similarity = self.calculate_similarity(query_embedding, review.embedding_vector)
            if similarity >= min_similarity:
                review_copy = ReviewEmbedding(**asdict(review))
                review_copy.similarity_score = similarity
                similarities.append(review_copy)
        
        # Sort by similarity score (descending)
        similarities.sort(key=lambda x: x.similarity_score, reverse=True)
        
        return similarities[:top_k]


def create_embedding_for_pr_code(code_diff: str, file_path: str, pr_context: Dict) -> List[float]:
    """
    Utility function to create embedding for PR code changes
    """
    api_key = os.getenv('ANTHROPIC_API_KEY')
    if not api_key:
        raise ValueError("ANTHROPIC_API_KEY environment variable is required")
    
    client = ClaudeEmbeddingClient(api_key)
    context = f"File: {file_path}, PR: {pr_context.get('title', '')}"
    
    return client.generate_review_embedding(code_diff, context)


if __name__ == "__main__":
    # Example usage
    api_key = os.getenv('ANTHROPIC_API_KEY', 'dummy-key-for-testing')
    claude_client = ClaudeEmbeddingClient(api_key)
    manager = ReviewEmbeddingManager(claude_client)
    
    # Test embedding generation
    code_sample = """
    public void processPayment(Payment payment) {
        if (payment.getAmount() > 0) {
            paymentService.process(payment);
        }
    }
    """
    
    review_comment = "This method should validate the payment object for null values before processing"
    
    pr_info = {
        "repo": "spring-petclinic",
        "pr_number": 123,
        "files": ["src/main/java/PaymentController.java"]
    }
    
    embedding = manager.create_review_embedding(
        code_chunk=code_sample,
        review_comment=review_comment,
        pr_info=pr_info,
        reviewer="senior-dev"
    )
    
    print(f"Created embedding with ID: {embedding.id}")
    print(f"Tags: {embedding.tags}")
    print(f"Embedding dimension: {len(embedding.embedding_vector)}")