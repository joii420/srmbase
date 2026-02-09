"""Configuration management for SRM systems."""

from typing import Any, Dict, Optional


class Config:
    """
    Configuration manager for SRM systems.
    
    Provides a simple key-value configuration store with support for
    nested configurations and default values.
    """
    
    def __init__(self, initial_config: Optional[Dict[str, Any]] = None):
        """
        Initialize configuration.
        
        Args:
            initial_config: Optional initial configuration dictionary
        """
        self._config: Dict[str, Any] = initial_config or {}
    
    def get(self, key: str, default: Any = None) -> Any:
        """
        Get a configuration value.
        
        Args:
            key: Configuration key (supports dot notation for nested keys)
            default: Default value if key is not found
            
        Returns:
            Configuration value or default
        """
        keys = key.split(".")
        value = self._config
        
        for k in keys:
            if isinstance(value, dict) and k in value:
                value = value[k]
            else:
                return default
        
        return value
    
    def set(self, key: str, value: Any) -> None:
        """
        Set a configuration value.
        
        Args:
            key: Configuration key (supports dot notation for nested keys)
            value: Value to set
        """
        keys = key.split(".")
        config = self._config
        
        for k in keys[:-1]:
            if k not in config:
                config[k] = {}
            elif not isinstance(config[k], dict):
                raise ValueError(
                    f"Cannot set nested key '{key}': "
                    f"intermediate key '{k}' is not a dictionary"
                )
            config = config[k]
        
        config[keys[-1]] = value
    
    def has(self, key: str) -> bool:
        """
        Check if a configuration key exists.
        
        Args:
            key: Configuration key to check
            
        Returns:
            True if key exists, False otherwise
        """
        keys = key.split(".")
        value = self._config
        
        for k in keys:
            if isinstance(value, dict) and k in value:
                value = value[k]
            else:
                return False
        
        return True
    
    def to_dict(self) -> Dict[str, Any]:
        """
        Get the entire configuration as a dictionary.
        
        Returns:
            Configuration dictionary
        """
        return self._config.copy()
    
    def update(self, config: Dict[str, Any]) -> None:
        """
        Update configuration with values from another dictionary.
        
        Args:
            config: Dictionary of configuration values to update
        """
        self._config.update(config)
    
    def __repr__(self) -> str:
        """Return string representation of the configuration."""
        return f"Config({self._config})"
