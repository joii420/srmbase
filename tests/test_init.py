"""Test package initialization."""

import srmbase


def test_version():
    """Test that version is defined."""
    assert hasattr(srmbase, "__version__")
    assert isinstance(srmbase.__version__, str)


def test_imports():
    """Test that main classes are importable."""
    assert hasattr(srmbase, "BaseEntity")
    assert hasattr(srmbase, "Config")


def test_all_exports():
    """Test that __all__ contains expected exports."""
    assert "BaseEntity" in srmbase.__all__
    assert "Config" in srmbase.__all__
    assert "__version__" in srmbase.__all__
