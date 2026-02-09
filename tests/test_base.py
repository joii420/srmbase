"""Tests for base entity functionality."""

import pytest
from datetime import datetime
from srmbase.base import BaseEntity


class TestBaseEntity:
    """Test cases for BaseEntity class."""
    
    def test_initialization(self):
        """Test entity initialization."""
        entity = BaseEntity(id="test-id")
        assert entity.id == "test-id"
        assert isinstance(entity.created_at, datetime)
        assert isinstance(entity.updated_at, datetime)
    
    def test_initialization_without_id(self):
        """Test entity initialization without ID."""
        entity = BaseEntity()
        assert entity.id is None
        assert isinstance(entity.created_at, datetime)
        assert isinstance(entity.updated_at, datetime)
    
    def test_to_dict(self):
        """Test entity to dictionary conversion."""
        entity = BaseEntity(id="test-id")
        result = entity.to_dict()
        
        assert result["id"] == "test-id"
        assert "created_at" in result
        assert "updated_at" in result
        assert isinstance(result["created_at"], str)
        assert isinstance(result["updated_at"], str)
    
    def test_update(self):
        """Test entity update method."""
        entity = BaseEntity(id="test-id")
        original_updated_at = entity.updated_at
        
        # Small delay to ensure timestamp changes
        import time
        time.sleep(0.01)
        
        entity.update()
        assert entity.updated_at > original_updated_at
    
    def test_repr(self):
        """Test entity string representation."""
        entity = BaseEntity(id="test-id")
        assert repr(entity) == "BaseEntity(id=test-id)"
    
    def test_equality(self):
        """Test entity equality comparison."""
        entity1 = BaseEntity(id="test-id")
        entity2 = BaseEntity(id="test-id")
        entity3 = BaseEntity(id="other-id")
        
        assert entity1 == entity2
        assert entity1 != entity3
    
    def test_hash(self):
        """Test entity hashing."""
        entity1 = BaseEntity(id="test-id")
        entity2 = BaseEntity(id="test-id")
        
        assert hash(entity1) == hash(entity2)
        
        # Entities without IDs should still be hashable
        entity3 = BaseEntity()
        assert isinstance(hash(entity3), int)
