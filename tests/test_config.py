"""Tests for configuration functionality."""

import pytest
from srmbase.config import Config


class TestConfig:
    """Test cases for Config class."""
    
    def test_initialization_empty(self):
        """Test config initialization without initial values."""
        config = Config()
        assert config.to_dict() == {}
    
    def test_initialization_with_values(self):
        """Test config initialization with initial values."""
        initial = {"key": "value"}
        config = Config(initial)
        assert config.to_dict() == {"key": "value"}
    
    def test_get_simple_key(self):
        """Test getting a simple configuration value."""
        config = Config({"key": "value"})
        assert config.get("key") == "value"
    
    def test_get_nested_key(self):
        """Test getting a nested configuration value."""
        config = Config({"parent": {"child": "value"}})
        assert config.get("parent.child") == "value"
    
    def test_get_with_default(self):
        """Test getting a non-existent key with default value."""
        config = Config()
        assert config.get("nonexistent", "default") == "default"
    
    def test_set_simple_key(self):
        """Test setting a simple configuration value."""
        config = Config()
        config.set("key", "value")
        assert config.get("key") == "value"
    
    def test_set_nested_key(self):
        """Test setting a nested configuration value."""
        config = Config()
        config.set("parent.child", "value")
        assert config.get("parent.child") == "value"
    
    def test_set_nested_key_with_conflict(self):
        """Test setting nested key when intermediate key is not a dict."""
        config = Config({"parent": "string_value"})
        with pytest.raises(ValueError) as exc_info:
            config.set("parent.child", "value")
        assert "not a dictionary" in str(exc_info.value)
    
    def test_has_existing_key(self):
        """Test checking for existing key."""
        config = Config({"key": "value"})
        assert config.has("key") is True
    
    def test_has_nonexistent_key(self):
        """Test checking for non-existent key."""
        config = Config()
        assert config.has("nonexistent") is False
    
    def test_has_key_with_none_value(self):
        """Test checking for key with None value."""
        config = Config({"key": None})
        assert config.has("key") is True
        assert config.get("key") is None
    
    def test_update(self):
        """Test updating configuration."""
        config = Config({"key1": "value1"})
        config.update({"key2": "value2"})
        
        assert config.get("key1") == "value1"
        assert config.get("key2") == "value2"
    
    def test_to_dict_deep_copy(self):
        """Test that to_dict returns a deep copy."""
        config = Config({"nested": {"key": "value"}})
        config_dict = config.to_dict()
        
        # Mutate the returned dict
        config_dict["nested"]["key"] = "modified"
        
        # Original config should be unchanged
        assert config.get("nested.key") == "value"
    
    def test_repr(self):
        """Test config string representation."""
        config = Config({"key": "value"})
        assert "key" in repr(config)
        assert "value" in repr(config)
