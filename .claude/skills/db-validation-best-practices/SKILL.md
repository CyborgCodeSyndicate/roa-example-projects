# DB Validation Best Practices Skill

## Purpose
Teaches Claude Code how to implement robust database validation within the ROA framework.

## When to Apply
- When verifying side effects of API or UI actions.
- When setting up test data directly in the database.
- When cleaning up data after tests.

## Core Concepts

### 1. The DB Ring
- **Usage**: `quest.use(RING_OF_DB)`
- **Action**: `.query(AppQueries.QUERY_NAME)`
- **Validation**: `.validate(retrieve(StorageKeysDb.DB, ...), assertions...)`

### 2. Query Management
- **Enum**: Store all SQL in `AppQueries`.
- **Parameters**: Use `{param}` syntax for dynamic values.
- **Injection**: Use `.withParam("key", value)` at runtime.

### 3. Validation Patterns
- **Cross-Layer**:
  1. Action: UI/API creates an entity.
  2. Switch: `quest.drop().use(RING_OF_DB)`.
  3. Query: Select the entity.
  4. Assert: Verify DB state matches UI/API input.
- **JsonPath**: Use `DbResponsesJsonPaths` to extract values from query results (which are converted to JSON).

### 4. Lifecycle Hooks
- **Setup**: Use `@DbHook(when = BEFORE)` for seeding data.
- **Teardown**: Use `@Ripper` or `@DbHook(when = AFTER)` for cleanup.
- **Storage**: Use `StorageKeysDb` to pass data between hooks and tests.

## Rules
- **Read-Only Tests**: Prefer validating state over modifying it during the test body (leave modification to SUT).
- **Isolation**: Ensure queries target only the data relevant to the current test (use unique IDs).
- **Performance**: Avoid `SELECT *` on large tables; always filter by ID or index.

## Constraints
- **No Raw SQL**: Never write SQL strings inside test methods.
- **Type Safety**: Always use `AppQueries` enum constants.
