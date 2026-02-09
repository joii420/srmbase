"""Base entity class for SRM systems."""

from datetime import datetime, timezone
from typing import Any, Dict, Optional


class BaseEntity:
    """
    Base entity class that provides common functionality for all entities.
    
    Attributes:
        id: Unique identifier for the entity
        created_at: Timestamp when the entity was created (UTC timezone-aware)
        updated_at: Timestamp when the entity was last updated (UTC timezone-aware)
    """
    
    def __init__(self, id: Optional[str] = None):
        """
        Initialize a base entity.
        
        Args:
            id: Optional unique identifier. If not provided, one should be generated.
        """
        self.id = id
        self.created_at = datetime.now(timezone.utc)
        self.updated_at = datetime.now(timezone.utc)
    
    def to_dict(self) -> Dict[str, Any]:
        """
        Convert the entity to a dictionary representation.
        
        Returns:
            Dictionary containing the entity's attributes
        """
        return {
            "id": self.id,
            "created_at": self.created_at.isoformat(),
            "updated_at": self.updated_at.isoformat(),
        }
    
    def update(self) -> None:
        """Update the entity's updated_at timestamp."""
        self.updated_at = datetime.now(timezone.utc)
    
    def __repr__(self) -> str:
        """Return a string representation of the entity."""
        return f"{self.__class__.__name__}(id={self.id})"
    
    def __eq__(self, other: object) -> bool:
        """Check equality based on entity ID."""
        if not isinstance(other, BaseEntity):
            return NotImplemented
        return self.id == other.id
    
    def __hash__(self) -> int:
        """Return hash based on entity ID."""
        return hash(self.id) if self.id else hash(id(self))
