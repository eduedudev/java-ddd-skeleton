# Conventional Commits Guide

## Introduction
This guide outlines the use of **Conventional Commits**, a standardized format for writing commit messages. Following this convention improves readability, automation, and versioning consistency in the project.

## Commit Message Format
Each commit message should follow this structure:

```
<type>(<scope>): <short description>
```

Where:
- **`type`**: The purpose of the change (e.g., `feat`, `fix`, `chore`).
- **`scope`** (optional): The part of the codebase affected (e.g., `auth`, `api`).
- **`short description`**: A brief, imperative summary of the change.

Example:
```
feat(auth): add support for multi-factor authentication
fix(database): resolve issue with connection timeout
```

## Commit Types
### 1. **Feature (`feat`)**
Used for new features or functionality.
```
feat(billing): implement support for PayPal payments
```

### 2. **Fix (`fix`)**
Used for bug fixes.
```
fix(ui): correct button alignment on mobile
```

### 3. **Chore (`chore`)**
Used for non-functional tasks (e.g., build process, dependencies).
```
chore(deps): update NestJS to v9.0.0
```

### 4. **Refactor (`refactor`)**
Used for code changes that do not add features or fix bugs.
```
refactor(api): improve error handling in GraphQL queries
```

### 5. **Docs (`docs`)**
Used for documentation updates.
```
docs(readme): update API usage instructions
```

### 6. **Style (`style`)**
Used for formatting, whitespace, or linting changes.
```
style(css): adjust margins in dashboard layout
```

### 7. **Test (`test`)**
Used for adding or updating tests.
```
test(users): add unit tests for login functionality
```

### 8. **Perf (`perf`)**
Used for performance improvements.
```
perf(cache): optimize database query caching
```

### 9. **Build (`build`)**
Used for build-related changes.
```
build(ci): update GitHub Actions workflow
```

### 10. **Revert (`revert`)**
Used to revert previous commits.
```
revert(auth): rollback OAuth2 integration
```

## Breaking Changes
If a commit introduces breaking changes, append `BREAKING CHANGE:` followed by an explanation.

Example:
```
feat(api): remove deprecated /users endpoint

BREAKING CHANGE: The /users endpoint has been removed. Use /v2/users instead.
```

## Conclusion
Using Conventional Commits ensures a structured and readable commit history. It also enables automation for versioning, changelogs, and CI/CD processes.

For automation, consider using:
- [`commitlint`](https://commitlint.js.org/) to enforce commit message format.
- [`semantic-release`](https://semantic-release.gitbook.io/) for automated versioning.

