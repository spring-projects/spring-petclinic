"""
FAISS Memory Manager for PR Review System
Manages vector storage and retrieval using FAISS index
"""

import os
import json
import faiss
import numpy as np
from typing import List, Dict, Optional, Tuple
from dataclasses import asdict
import tempfile
import shutil
from pathlib import Path

from claude_embeddings import ReviewEmbedding, ReviewEmbeddingManager


class FAISSMemoryManager:
    """Manages FAISS index and metadata for review embeddings"""
    
    def __init__(self, dimension: int = 768, index_type: str = "flat"):
        self.dimension = dimension
        self.index_type = index_type
        self.index = None
        self.metadata = []
        self.id_to_index = {}  # Maps embedding ID to FAISS index position
        
        self._initialize_index()
    
    def _initialize_index(self):
        """Initialize FAISS index based on type"""
        if self.index_type == "flat":
            # Simple flat index for exact similarity search
            self.index = faiss.IndexFlatIP(self.dimension)  # Inner Product for cosine similarity
        elif self.index_type == "ivf":
            # IVF index for faster search with large datasets
            quantizer = faiss.IndexFlatIP(self.dimension)
            self.index = faiss.IndexIVFFlat(quantizer, self.dimension, 100)  # 100 clusters
        else:
            raise ValueError(f"Unsupported index type: {self.index_type}")
    
    def add_embedding(self, review_embedding: ReviewEmbedding) -> bool:
        """Add a new review embedding to the index"""
        
        # Check if embedding already exists
        if review_embedding.id in self.id_to_index:
            print(f"Embedding {review_embedding.id} already exists, skipping")
            return False
        
        # Prepare embedding vector
        embedding_vector = np.array([review_embedding.embedding_vector], dtype=np.float32)
        
        # Normalize for cosine similarity (required for IndexFlatIP)
        faiss.normalize_L2(embedding_vector)
        
        # Add to FAISS index
        current_index = self.index.ntotal
        self.index.add(embedding_vector)
        
        # Update metadata and mapping
        self.metadata.append(asdict(review_embedding))
        self.id_to_index[review_embedding.id] = current_index
        
        print(f"Added embedding {review_embedding.id} at index {current_index}")
        return True
    
    def add_embeddings_batch(self, review_embeddings: List[ReviewEmbedding]) -> int:
        """Add multiple embeddings in batch for better performance"""
        
        new_embeddings = []
        new_metadata = []
        
        for review_embedding in review_embeddings:
            if review_embedding.id not in self.id_to_index:
                new_embeddings.append(review_embedding.embedding_vector)
                new_metadata.append(asdict(review_embedding))
        
        if not new_embeddings:
            print("No new embeddings to add")
            return 0
        
        # Prepare embedding matrix
        embedding_matrix = np.array(new_embeddings, dtype=np.float32)
        faiss.normalize_L2(embedding_matrix)
        
        # Add to FAISS index
        start_index = self.index.ntotal
        self.index.add(embedding_matrix)
        
        # Update metadata and mapping
        for i, metadata in enumerate(new_metadata):
            current_index = start_index + i
            self.metadata.append(metadata)
            self.id_to_index[metadata['id']] = current_index
        
        print(f"Added {len(new_embeddings)} embeddings in batch")
        return len(new_embeddings)
    
    def search_similar(
        self,
        query_embedding: List[float],
        top_k: int = 5,
        min_similarity: float = 0.3
    ) -> List[Tuple[ReviewEmbedding, float]]:
        """Search for similar embeddings"""
        
        if self.index.ntotal == 0:
            print("No embeddings in index")
            return []
        
        # Prepare query vector
        query_vector = np.array([query_embedding], dtype=np.float32)
        faiss.normalize_L2(query_vector)
        
        # Search
        search_k = min(top_k * 2, self.index.ntotal)  # Search more to filter by threshold
        similarities, indices = self.index.search(query_vector, search_k)
        
        results = []
        for similarity, index in zip(similarities[0], indices[0]):
            if index == -1:  # No more results
                break
                
            if similarity >= min_similarity:
                metadata = self.metadata[index]
                review_embedding = ReviewEmbedding(**metadata)
                review_embedding.similarity_score = float(similarity)
                results.append((review_embedding, float(similarity)))
        
        # Sort by similarity (descending) and return top_k
        results.sort(key=lambda x: x[1], reverse=True)
        return results[:top_k]
    
    def save_to_files(self, index_path: str, metadata_path: str):
        """Save FAISS index and metadata to files"""
        
        # Ensure directories exist
        os.makedirs(os.path.dirname(index_path), exist_ok=True)
        os.makedirs(os.path.dirname(metadata_path), exist_ok=True)
        
        # Save FAISS index
        faiss.write_index(self.index, index_path)
        
        # Save metadata
        metadata_dict = {
            'embeddings': self.metadata,
            'id_to_index': self.id_to_index,
            'dimension': self.dimension,
            'index_type': self.index_type,
            'total_count': self.index.ntotal
        }
        
        with open(metadata_path, 'w', encoding='utf-8') as f:
            json.dump(metadata_dict, f, indent=2, ensure_ascii=False)
        
        print(f"Saved {self.index.ntotal} embeddings to {index_path}")
    
    def load_from_files(self, index_path: str, metadata_path: str) -> bool:
        """Load FAISS index and metadata from files"""
        
        if not os.path.exists(index_path) or not os.path.exists(metadata_path):
            print(f"Index or metadata file not found")
            return False
        
        try:
            # Load FAISS index
            self.index = faiss.read_index(index_path)
            
            # Load metadata
            with open(metadata_path, 'r', encoding='utf-8') as f:
                metadata_dict = json.load(f)
            
            self.metadata = metadata_dict['embeddings']
            self.id_to_index = metadata_dict['id_to_index']
            self.dimension = metadata_dict['dimension']
            self.index_type = metadata_dict['index_type']
            
            print(f"Loaded {self.index.ntotal} embeddings from {index_path}")
            return True
            
        except Exception as e:
            print(f"Error loading index: {e}")
            return False
    
    def merge_with_existing(self, other_manager: 'FAISSMemoryManager'):
        """Merge another FAISS memory manager into this one"""
        
        if other_manager.index.ntotal == 0:
            return
        
        # Extract embeddings from other manager
        other_embeddings = []
        for metadata in other_manager.metadata:
            other_embeddings.append(ReviewEmbedding(**metadata))
        
        # Add embeddings that don't already exist
        self.add_embeddings_batch(other_embeddings)
    
    def get_stats(self) -> Dict:
        """Get statistics about the memory manager"""
        
        tag_counts = {}
        reviewer_counts = {}
        
        for metadata in self.metadata:
            # Count tags
            for tag in metadata.get('tags', []):
                tag_counts[tag] = tag_counts.get(tag, 0) + 1
            
            # Count reviewers
            reviewer = metadata.get('reviewer', 'unknown')
            reviewer_counts[reviewer] = reviewer_counts.get(reviewer, 0) + 1
        
        return {
            'total_embeddings': self.index.ntotal if self.index else 0,
            'dimension': self.dimension,
            'index_type': self.index_type,
            'tag_distribution': tag_counts,
            'reviewer_distribution': reviewer_counts
        }


def create_memory_manager_from_reviews(reviews: List[Dict]) -> FAISSMemoryManager:
    """Utility function to create memory manager from review data"""
    
    manager = FAISSMemoryManager()
    
    review_embeddings = []
    for review_data in reviews:
        if 'embedding_vector' in review_data:
            review_embeddings.append(ReviewEmbedding(**review_data))
    
    if review_embeddings:
        manager.add_embeddings_batch(review_embeddings)
    
    return manager


if __name__ == "__main__":
    # Example usage
    from claude_embeddings import ClaudeEmbeddingClient, ReviewEmbeddingManager
    
    # Create sample data
    api_key = os.getenv('ANTHROPIC_API_KEY', 'dummy-key-for-testing')
    claude_client = ClaudeEmbeddingClient(api_key)
    embedding_manager = ReviewEmbeddingManager(claude_client)
    
    # Create sample embeddings
    sample_reviews = [
        {
            'code': 'if (user == null) { return; }',
            'comment': 'Should throw exception instead of silent return',
            'reviewer': 'senior-dev-1'
        },
        {
            'code': 'String password = request.getParameter("password");',
            'comment': 'Password should be handled securely, consider using char array',
            'reviewer': 'security-expert'
        }
    ]
    
    # Initialize memory manager
    memory_manager = FAISSMemoryManager()
    
    # Add sample embeddings
    for review in sample_reviews:
        pr_info = {'repo': 'test-repo', 'pr_number': 1}
        embedding = embedding_manager.create_review_embedding(
            code_chunk=review['code'],
            review_comment=review['comment'],
            pr_info=pr_info,
            reviewer=review['reviewer']
        )
        memory_manager.add_embedding(embedding)
    
    # Test search
    query_code = 'if (data == null) { return null; }'
    query_embedding = claude_client.generate_review_embedding(query_code)
    
    similar_reviews = memory_manager.search_similar(query_embedding, top_k=2)
    
    print("\nSimilar reviews found:")
    for review, similarity in similar_reviews:
        print(f"Similarity: {similarity:.3f}")
        print(f"Comment: {review.review_comment}")
        print(f"Reviewer: {review.reviewer}")
        print("---")
    
    # Show stats
    stats = memory_manager.get_stats()
    print(f"\nMemory Manager Stats: {stats}")