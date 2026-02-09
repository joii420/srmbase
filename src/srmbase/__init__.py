"""
srmbase - Base library for SRM (Student Resource Management) systems

This package provides base classes and utilities for building SRM systems.
"""

__version__ = "0.1.0"

from .base import BaseEntity
from .config import Config

__all__ = ["BaseEntity", "Config", "__version__"]
