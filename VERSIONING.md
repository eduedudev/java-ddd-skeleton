# Versioning Guide

## Introduction
This guide describes the versioning scheme used in the project, based on **Semantic Versioning (SemVer)**. This system helps maintain clarity in changes and compatibility in updates.

## Versioning Format
The versioning system follows this format:

```
MAJOR.MINOR.PATCH
```

Where:

- **MAJOR** (`X.0.0`): Incompatible changes with previous versions.
- **MINOR** (`0.X.0`): New features compatible with previous versions.
- **PATCH** (`0.0.X`): Bug fixes and minor improvements without affecting compatibility.

## Version Update Rules
### 1. Major Changes (`MAJOR`)
- Removal of functionalities or endpoints.
- Changes in database structure without automatic migrations.
- API modifications that break compatibility with previous versions.
- Example:
  ```
  chore(release): bump version to 3.0.0 due to breaking changes
  ```

### 2. Minor Changes (`MINOR`)
- Adding a new feature without affecting existing ones.
- API improvements without removing or modifying previous structures.
- Example:
  ```
  feat(auth): add support for Google authentication
  ```

### 3. Patches (`PATCH`)
- Bug fixes without modifying functionality.
- Performance improvements or internal refactorings without affecting the API.
- Example:
  ```
  fix(database): fix user validation error
  ```