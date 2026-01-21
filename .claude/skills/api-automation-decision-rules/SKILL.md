# API Automation Decision Rules Skill

## Purpose
Teaches Claude Code how to make decisions when generating API automation code, leveraging MCP guidance when available.

## When to Apply
- When generating API test scenarios.
- When defining API endpoints and DTOs.
- When validating API responses.

## MCP Guidance (Swagger/OpenAPI)
- **Specs**: Use OpenAPI specs to generate `AppEndpoints` and DTOs.
- **Schemas**: Map JSON schemas to Java Records or POJOs (Lombok `@Data`).
- **Auth**: Identify authentication mechanisms (Bearer, Basic, API Key) and configure `BaseQuest` or `Preconditions` accordingly.

## Decision Rules

### 1. Endpoint Definition
- **Enum**: Always use `AppEndpoints` enum.
- **Method**: Explicitly define HTTP methods.
- **Paths**: Use placeholders for path parameters (e.g., `/users/{id}`).

### 2. Data Transfer Objects (DTOs)
- **Request**: Create `*RequestDto` or `*Dto` classes.
- **Response**: Create `*ResponseDto` classes or use `JsonPath` for simple validations.
- **Builder**: Use `@Builder` for flexible object creation in tests.

### 3. Validation Strategy
- **Status**: Always assert HTTP status code first.
- **Schema**: Validate critical fields using `JsonPath` or DTO mapping.
- **Contract**: Validate headers (Content-Type) and structure.
- **Negative Tests**: Explicitly test 4xx and 5xx scenarios.

### 4. Test Data
- **Isolation**: Use unique data per test (randomized or setup via `DataCreator`).
- **Cleanup**: Use `@Ripper` or `@DbHook` to clean up created resources.

## Constraints
- **Stateless**: Tests should be independent.
- **Idempotent**: Retrying a test should not fail due to previous state (unless testing state specifically).
- **Secure**: Do not log full auth tokens or secrets.
