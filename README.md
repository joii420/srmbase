# srmbase

A base library for SRM (Student Resource Management) systems providing foundational classes and utilities.

## Features

- **BaseEntity**: Base class for all entities with common functionality
  - Automatic timestamp management (created_at, updated_at)
  - Dictionary serialization
  - Equality and hashing support
  
- **Config**: Flexible configuration management
  - Nested configuration support with dot notation
  - Default values
  - Easy updates and queries

## Installation

Install the package in development mode:

```bash
pip install -e .
```

Or with development dependencies:

```bash
pip install -e ".[dev]"
```

## Usage

### BaseEntity

Create entities by extending the `BaseEntity` class:

```python
from srmbase import BaseEntity

class Student(BaseEntity):
    def __init__(self, id, name, email):
        super().__init__(id)
        self.name = name
        self.email = email
    
    def to_dict(self):
        data = super().to_dict()
        data.update({
            "name": self.name,
            "email": self.email
        })
        return data

# Create a student
student = Student(id="123", name="John Doe", email="john@example.com")
print(student.to_dict())
```

### Config

Manage application configuration:

```python
from srmbase import Config

# Initialize with values
config = Config({
    "database": {
        "host": "localhost",
        "port": 5432
    }
})

# Get values (supports dot notation)
host = config.get("database.host")  # "localhost"
timeout = config.get("database.timeout", 30)  # 30 (default)

# Set values
config.set("database.timeout", 60)
config.set("app.name", "My SRM App")

# Check if key exists
if config.has("database.host"):
    print("Database host is configured")
```

## Development

Install development dependencies:

```bash
pip install -e ".[dev]"
```

Run tests:

```bash
pytest
```

Run tests with coverage:

```bash
pytest --cov=srmbase --cov-report=html
```

## Requirements

- Python 3.8 or higher

## License

MIT
