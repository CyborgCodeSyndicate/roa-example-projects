# **db-framework-instructions.md**

## Database Testing with ROA Framework
The ROA framework provides seamless database testing capabilities with automatic connection management, 
query execution, and declarative validation.

### Test Class Structure
**Class Annotations**
* Use `@DB` annotation on each database test class
* Use `@DbHook(when = BEFORE/AFTER, type = HookFlow)` for database initialization or cleanup
* Use `@DisplayName("...")` for descriptive test organization

**Class Inheritance**
* Extend `BaseQuest` for standard database test execution
* Extend `BaseQuestSequential` when tests must execute sequentially

### Test Method Structure
**Quest Object**
* Every database test method must accept a Quest parameter as the first argument
* The `Quest` object orchestrates all database operations via fluent API
* Never instantiate `Quest` manually; let the framework inject it

**Service Ring Management**
* Use `.use(RING_OF_DB)` to activate the database service ring
* Use `.drop()` when switching to other rings if your project has multiple different services
* Always drop before switching contexts

**Test Completion**
* Always end test methods with `.complete()` to finalize quest execution

### Database Configuration
**Database Type Definition**
* Define supported database types in a dedicated enum implementing `DbType<T>` interface
* Each database constant must specify JDBC driver and connection protocol
* Database types enable switching between database engines without changing test code
* Use enum-based definitions for type-safe database selection

**Required Methods**
* Implement `driver()` to return the JDBC Driver instance for the database
* Implement `protocol()` to return the JDBC protocol string (e.g., `jdbc:h2`, `jdbc:postgresql`, `jdbc:mysql`)
* Implement `enumImpl()` to return the enum constant itself

**Database Selection**
* Specify database type in test configuration or environment properties
* Framework selects appropriate driver and protocol based on configuration
* Tests remain database-agnostic; only configuration changes between environments

### Database Hooks
**@DbHook Annotation**
* Use `@DbHook` at class level to initialize database connections
* Specify `when = BEFORE` for initialization before all tests
* Specify `when = AFTER` for cleanup after all tests
* Define hook flows in dedicated hook classes (e.g., `DbHookFlows`)

**Hook Execution**
* `BEFORE` hooks execute before any test in the class
* `AFTER` hooks execute after all tests complete
* Use hooks for database initialization, migrations, or seeding

### Query Execution
**Query Definition**
* Define queries in dedicated query classes (e.g., `AppQueries`)
* Use `Query.of("SQL_STATEMENT")` to create query objects
* Support parameterized queries with :paramName placeholders

**Executing Queries**
* Use `.query(QUERY_NAME)` to execute a query
* Use `.query(QUERY_NAME.withParam("key", value))` for parameterized queries
* Queries execute via JDBC and return results automatically
* Query results are stored in `StorageKeysDb.DB`

### Query Validation
**Declarative Assertions**
* Use `Assertion.builder()` for declarative query result validation
* Target `QUERY_RESULT` for database validation
* Use JsonPath extractors to navigate query results
* Support all standard assertion types (IS, CONTAINS, NOT_NULL, etc.)

**Retrieving Query Results**
* Use retrieve(`StorageKeysDb.DB`, `QUERY_NAME`, `QueryResponse.class`) to get stored results
* Query results are stored with the query reference as the key
* Use JsonPath to extract specific fields from results

### Database Preconditions
**Journey with Database Validation**
* Use `@Journey` to perform database validation as precondition
* Define database preconditions in `Preconditions` enum
* Add constants as strings in `Preconditions` enum Data class for usage in `Journey` annotations
* Validate required database state before test execution
* Combine database journeys with other module journeys using order attribute if your project has multiple modules

### Database Cleanup
**@Ripper for Database Cleanup**
* Use `@Ripper` to specify database cleanup operations
* Define cleanup targets in DataCleaner enum
* Add constants as strings in `DataCleaner` enum Data class for usage in `Ripper` annotations
* Cleanup executes after test completion (success or failure)
* Ensures test data isolation and prevents database pollution

### Storage and Retrieval
**Storage Keys**
* StorageKeysDb.DB: Query result storage
* PRE_ARGUMENTS: Journey-created data storage

**Retrieving Data**
* Use retrieve(`StorageKeysDb.DB`, `QUERY_NAME`, `QueryResponse.class`) for query results
* Use retrieve(PRE_ARGUMENTS, key, Class.class) for journey-created data

### JsonPath Extractors
**Database Response Navigation**
* Define JsonPath constants in dedicated classes (e.g., `DbResponsesJsonPaths`)
* Use enum-based JsonPath definitions for reusability
* Access nested query result values via `.getJsonPath()` methods
* Use JsonPath in assertions to validate specific fields

**Validation Requirements**
* Provide at least one validation in each database test
* Use framework assertion builders with `QUERY_RESULT` target
* Validate database state after operations from other modules if your project has multiple modules
* Use soft assertions for multiple related database checks