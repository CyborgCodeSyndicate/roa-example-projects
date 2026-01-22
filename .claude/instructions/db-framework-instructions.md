# **db-framework-instructions.md**

## About This Document
**This is the single source of truth for database-specific patterns and architecture in the ROA framework.**

For core framework concepts (Quest DSL, @Craft, @Journey, @Ripper, service rings, validation fundamentals), see [core-framework-instructions.md](core-framework-instructions.md).

This file contains **database-specific architecture, query system, and database testing patterns only**.

---

## Prerequisites
Before implementing database tests, read [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals and public API
- Service ring management (.use(), .drop(), .complete())
- Test data management (@Craft annotation and Late<> initialization)
- Preconditions (@Journey) and cleanup (@Ripper)
- General validation requirements
- Storage and data retrieval patterns

**This file assumes you understand those core concepts.**

---

## Interface Contracts

### DbType Interface
**CRITICAL:** All database type enums must implement the `DbType<T>` interface.

**Required Methods:**
```java
Driver driver();          // JDBC Driver instance
String protocol();        // JDBC protocol string (jdbc:h2, jdbc:postgresql, etc.)
Enum<?> enumImpl();       // Returns this enum constant
```

**Example:**
```java
public enum Databases implements DbType<Databases> {
    H2_DATABASE(new org.h2.Driver(), "jdbc:h2"),
    POSTGRESQL(new org.postgresql.Driver(), "jdbc:postgresql"),
    MYSQL(new com.mysql.cj.jdbc.Driver(), "jdbc:mysql");

    private final Driver driver;
    private final String protocol;

    Databases(Driver driver, String protocol) {
        this.driver = driver;
        this.protocol = protocol;
    }

    @Override
    public Driver driver() {
        return driver;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

    // Nested Data class for annotation references
    public static final class Data {
        public static final String H2_DATABASE = "H2_DATABASE";
        public static final String POSTGRESQL = "POSTGRESQL";
        public static final String MYSQL = "MYSQL";
        private Data() {}
    }
}
```

**Common Compilation Error:**
```java
// ❌ WRONG - missing enumImpl()
public enum Databases implements DbType<Databases> {
    H2_DATABASE(new org.h2.Driver(), "jdbc:h2");
    // Missing enumImpl() method - causes compilation error
}

// ✅ CORRECT
public enum Databases implements DbType<Databases> {
    H2_DATABASE(new org.h2.Driver(), "jdbc:h2");

    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
```

---

## Database Test Class Structure

### Required Annotations
**Class Level:**
* `@DB` annotation on each database test class (required)
* `@DbHook(when = BEFORE/AFTER, type = HookFlow)` for database initialization/cleanup (optional)
* `@DisplayName("...")` for descriptive test organization (recommended)

**Class Inheritance:**
* Extend `BaseQuest` for standard parallel-capable tests
* Extend `BaseQuestSequential` when tests must execute in order (rare)

See [core-framework-instructions.md](core-framework-instructions.md) for inheritance details.

### Test Method Structure
**Basic Requirements:**
```java
@Test
void testMethodName(Quest quest) {  // Quest parameter MUST be first
    quest
        .use(RING_OF_DB)            // Activate database service ring
        .query(AppQueries.GET_USER.withParam("userId", "123"))
        .complete();                // MUST end with .complete()
}
```

**Multi-Module Tests:**
```java
@Test
@DB
@API
void crossModuleTest(Quest quest) {
    quest
        .use(RING_OF_API)
        .request(UserEndpoints.CREATE_USER, userDto)
        .drop()                     // ✅ Use .drop() when switching rings
        .use(RING_OF_DB)
        .query(AppQueries.VERIFY_USER_EXISTS.withParam("userId", userId))
        .complete();
}
```

See [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals
- Service ring management
- .complete() requirement
- Validation requirements

---

## Database Type Definition

### Database Type Structure
Define supported database types in dedicated enum implementing `DbType<T>` interface.

**Purpose:**
* Enable database engine switching without code changes
* Provide type-safe database selection
* Centralize JDBC driver and protocol configuration
* Support multiple databases in same project

**Complete Example:**
```java
public enum Databases implements DbType<Databases> {
    // In-memory database for testing
    H2_MEM(new org.h2.Driver(), "jdbc:h2:mem"),

    // File-based H2 database
    H2_FILE(new org.h2.Driver(), "jdbc:h2:file"),

    // PostgreSQL database
    POSTGRESQL(new org.postgresql.Driver(), "jdbc:postgresql"),

    // MySQL database
    MYSQL(new com.mysql.cj.jdbc.Driver(), "jdbc:mysql"),

    // Oracle database
    ORACLE(new oracle.jdbc.OracleDriver(), "jdbc:oracle:thin");

    private final Driver driver;
    private final String protocol;

    Databases(Driver driver, String protocol) {
        this.driver = driver;
        this.protocol = protocol;
    }

    @Override
    public Driver driver() {
        return driver;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

    public static final class Data {
        public static final String H2_MEM = "H2_MEM";
        public static final String H2_FILE = "H2_FILE";
        public static final String POSTGRESQL = "POSTGRESQL";
        public static final String MYSQL = "MYSQL";
        public static final String ORACLE = "ORACLE";
        private Data() {}
    }
}
```

### Database Selection
Database type is selected via configuration, keeping tests database-agnostic.

**Configuration (test_data-dev.properties):**
```properties
db.type=H2_MEM
db.host=localhost
db.port=5432
db.name=testdb
db.username=testuser
db.password=testpass
```

**Connection String Construction:**
Framework automatically constructs JDBC URL:
```
{protocol}://{host}:{port}/{database}
```

Examples:
```
jdbc:h2:mem:testdb
jdbc:postgresql://localhost:5432/testdb
jdbc:mysql://localhost:3306/testdb
```

### Supported Databases

| Database | Driver Class | Protocol | Typical Use |
|----------|-------------|----------|-------------|
| H2 (Memory) | `org.h2.Driver` | `jdbc:h2:mem` | Unit tests, fast execution |
| H2 (File) | `org.h2.Driver` | `jdbc:h2:file` | Persistent test data |
| PostgreSQL | `org.postgresql.Driver` | `jdbc:postgresql` | Integration tests |
| MySQL | `com.mysql.cj.jdbc.Driver` | `jdbc:mysql` | Integration tests |
| Oracle | `oracle.jdbc.OracleDriver` | `jdbc:oracle:thin` | Enterprise tests |
| SQL Server | `com.microsoft.sqlserver.jdbc.SQLServerDriver` | `jdbc:sqlserver` | Enterprise tests |

---

## Database Hooks (@DbHook)

### Purpose
`@DbHook` annotation enables database initialization and cleanup at class level.

**Use Cases:**
* Database schema creation
* Data seeding before tests
* Database cleanup after tests
* Migration execution
* Test data setup

### Hook Timing

**BEFORE Hooks:**
Execute before any test method in the class runs.

```java
@DB
@DbHook(when = BEFORE, type = DbSetupFlow.class)
class UserDatabaseTest extends BaseQuest {
    // All tests have database initialized
}
```

**AFTER Hooks:**
Execute after all test methods in the class complete.

```java
@DB
@DbHook(when = AFTER, type = DbCleanupFlow.class)
class UserDatabaseTest extends BaseQuest {
    // Database cleaned up after all tests
}
```

**Combined Hooks:**
```java
@DB
@DbHook(when = BEFORE, type = DbSetupFlow.class)
@DbHook(when = AFTER, type = DbCleanupFlow.class)
class UserDatabaseTest extends BaseQuest {
    // Setup before, cleanup after
}
```

### Hook Implementation

**Setup Hook Example:**
```java
public class DbSetupFlow implements DbHookFlow {
    @Override
    public void execute(Quest quest) {
        quest
            .use(RING_OF_DB)
            // Create schema
            .query(DbSetupQueries.CREATE_USERS_TABLE)
            .query(DbSetupQueries.CREATE_ORDERS_TABLE)

            // Seed initial data
            .query(DbSetupQueries.INSERT_DEFAULT_USERS)
            .query(DbSetupQueries.INSERT_DEFAULT_ROLES)

            .drop();
    }
}
```

**Cleanup Hook Example:**
```java
public class DbCleanupFlow implements DbHookFlow {
    @Override
    public void execute(Quest quest) {
        quest
            .use(RING_OF_DB)
            // Drop tables in reverse order (foreign keys)
            .query(DbCleanupQueries.DROP_ORDERS_TABLE)
            .query(DbCleanupQueries.DROP_USERS_TABLE)
            .drop();
    }
}
```

**Migration Hook Example:**
```java
public class DbMigrationFlow implements DbHookFlow {
    @Override
    public void execute(Quest quest) {
        quest
            .use(RING_OF_DB)
            .query(DbMigrationQueries.V1_CREATE_SCHEMA)
            .query(DbMigrationQueries.V2_ADD_COLUMNS)
            .query(DbMigrationQueries.V3_CREATE_INDEXES)
            .drop();
    }
}
```

---

## Query Definition and Execution

### Query Structure
Define queries in dedicated query classes or enums.

**Basic Query Definition:**
```java
public class UserQueries {
    public static final Query GET_USER_BY_ID = Query.of(
        "SELECT * FROM users WHERE id = :userId"
    );

    public static final Query GET_ALL_USERS = Query.of(
        "SELECT * FROM users"
    );

    public static final Query GET_ACTIVE_USERS = Query.of(
        "SELECT * FROM users WHERE status = 'active'"
    );

    public static final Query INSERT_USER = Query.of(
        "INSERT INTO users (name, email, status) VALUES (:name, :email, :status)"
    );

    public static final Query UPDATE_USER = Query.of(
        "UPDATE users SET name = :name, email = :email WHERE id = :userId"
    );

    public static final Query DELETE_USER = Query.of(
        "DELETE FROM users WHERE id = :userId"
    );
}
```

**Enum-Based Query Definition:**
```java
public enum AppQueries {
    GET_USER_BY_ID("SELECT * FROM users WHERE id = :userId"),
    GET_USER_BY_EMAIL("SELECT * FROM users WHERE email = :email"),
    COUNT_USERS("SELECT COUNT(*) as count FROM users"),
    GET_ORDERS_BY_USER("SELECT * FROM orders WHERE user_id = :userId");

    private final String sql;

    AppQueries(String sql) {
        this.sql = sql;
    }

    public Query withParam(String key, Object value) {
        return Query.of(sql).withParam(key, value);
    }

    public Query build() {
        return Query.of(sql);
    }

    public static final class Data {
        public static final String GET_USER_BY_ID = "GET_USER_BY_ID";
        public static final String GET_USER_BY_EMAIL = "GET_USER_BY_EMAIL";
        public static final String COUNT_USERS = "COUNT_USERS";
        public static final String GET_ORDERS_BY_USER = "GET_ORDERS_BY_USER";
        private Data() {}
    }
}
```

### Parameterized Queries

**Parameter Syntax:**
Use `:paramName` placeholders in SQL.

```java
// Single parameter
Query.of("SELECT * FROM users WHERE id = :userId")
     .withParam("userId", "123");

// Multiple parameters
Query.of("SELECT * FROM users WHERE name = :name AND status = :status")
     .withParam("name", "John")
     .withParam("status", "active");

// Complex query with text block
String complexQuery = """
    SELECT u.*, o.order_count 
    FROM users u
    LEFT JOIN (
        SELECT user_id, COUNT(*) as order_count 
        FROM orders 
        WHERE created_at > :startDate
        GROUP BY user_id
    ) o ON u.id = o.user_id
    WHERE u.status = :status
    """;

Query.of(complexQuery)
    .withParam("startDate", "2024-01-01")
    .withParam("status", "active");
```

### Query Execution

**Simple Query (no parameters):**
```java
quest
    .use(RING_OF_DB)
    .query(UserQueries.GET_ALL_USERS)
    .complete();
```

**Parameterized Query:**
```java
quest
    .use(RING_OF_DB)
    .query(UserQueries.GET_USER_BY_ID.withParam("userId", "123"))
    .complete();
```

**Multiple Queries:**
```java
quest
    .use(RING_OF_DB)
    .query(UserQueries.GET_USER_BY_ID.withParam("userId", "123"))
    .query(OrderQueries.GET_ORDERS_BY_USER.withParam("userId", "123"))
    .query(PaymentQueries.GET_PAYMENTS_BY_USER.withParam("userId", "123"))
    .complete();
```

**Query with Validation:**
```java
quest
    .use(RING_OF_DB)
    .queryAndValidate(
        UserQueries.GET_USER_BY_ID.withParam("userId", "123"),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].name")
            .type(IS)
            .expected("John Doe")
            .build()
    )
    .complete();
```

### Query Organization

**Best Practices:**
* Group queries by table or domain
* One query class per database table or feature
* Use descriptive query names
* Keep queries focused and single-purpose

**Example: Organized by Table:**
```
db/queries/
├── UserQueries.java         // User table queries
├── OrderQueries.java        // Order table queries
├── ProductQueries.java      // Product table queries
└── AuditQueries.java        // Audit log queries
```

**Example: Organized by Feature:**
```
db/queries/
├── UserManagementQueries.java    // User CRUD operations
├── OrderProcessingQueries.java   // Order workflow queries
├── ReportingQueries.java         // Analytics and reports
└── SetupQueries.java             // Schema and seed data
```

---

## Query Result Validation

### Assertion Builder Pattern
Database validation uses `Assertion.builder()` with `QUERY_RESULT` target.

**Basic Query Result Validation:**
```java
quest
    .use(RING_OF_DB)
    .queryAndValidate(
        UserQueries.GET_USER_BY_ID.withParam("userId", "123"),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].name")
            .type(IS)
            .expected("John Doe")
            .build()
    )
    .complete();
```

### Assertion Target

**QUERY_RESULT:**
The only target for database validation. Validates query result data using JsonPath.

### JsonPath for Query Results

**Query results are returned as JSON array:**
```json
[
  {
    "id": "123",
    "name": "John Doe",
    "email": "john@example.com",
    "status": "active"
  }
]
```

**JsonPath Syntax:**

| JsonPath | Description | Example |
|----------|-------------|---------|
| `$[0].field` | First row, specific field | `$[0].name` |
| `$[*].field` | All rows, specific field | `$[*].status` |
| `$.length()` | Row count | `$.length()` |
| `$[0]` | Entire first row | `$[0]` |

### Validation Examples

**Single Field Validation:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .key("$[0].name")
    .type(IS)
    .expected("John Doe")
    .build()
```

**Multiple Field Validation:**
```java
quest
    .use(RING_OF_DB)
    .queryAndValidate(
        UserQueries.GET_USER_BY_ID.withParam("userId", "123"),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].name")
            .type(IS)
            .expected("John Doe")
            .build(),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].email")
            .type(IS)
            .expected("john@example.com")
            .build(),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].status")
            .type(IS)
            .expected("active")
            .build()
    )
    .complete();
```

**Row Count Validation:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .type(LENGTH)
    .expected(10)
    .build()
```

**Null Check:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .key("$[0].email")
    .type(NOT_NULL)
    .build()
```

**Empty Result Check:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .type(NOT_EMPTY)
    .build()
```

**All Rows Validation:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .key("$[*].status")
    .type(CONTAINS_ALL)
    .expected(List.of("active"))
    .build()
```

**Numeric Comparison:**
```java
Assertion.builder()
    .target(QUERY_RESULT)
    .key("$[0].age")
    .type(GREATER_THAN)
    .expected(18)
    .build()
```

### Soft Assertions

**Use `.soft(true)` to collect all failures:**
```java
quest
    .use(RING_OF_DB)
    .queryAndValidate(
        UserQueries.GET_USER_BY_ID.withParam("userId", "123"),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].name")
            .type(IS)
            .expected("John Doe")
            .soft(true)
            .build(),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].email")
            .type(CONTAINS)
            .expected("@example.com")
            .soft(true)
            .build(),
        Assertion.builder()
            .target(QUERY_RESULT)
            .key("$[0].status")
            .type(IS)
            .expected("active")
            .soft(true)
            .build()
    )
    .complete();
```

---

## Storage and Retrieval

### Automatic Storage
Query results are automatically stored in `StorageKeysDb.DB` with the query reference as the key.

**Storage happens automatically:**
```java
quest
    .use(RING_OF_DB)
    .query(UserQueries.GET_USER_BY_ID.withParam("userId", "123"))
    // Result automatically stored with key: UserQueries.GET_USER_BY_ID
    .complete();
```

### Retrieving Query Results

**Basic Retrieval:**
```java
QueryResponse response = retrieve(StorageKeysDb.DB, UserQueries.GET_USER_BY_ID, QueryResponse.class);
```

**Extract to DTO:**
```java
// Get response and map to DTO
QueryResponse response = retrieve(StorageKeysDb.DB, UserQueries.GET_USER_BY_ID, QueryResponse.class);
UserDto user = response.getBody().as(UserDto[].class)[0];

// Access DTO fields
String userName = user.getName();
String userEmail = user.getEmail();
```

**Extract Specific Fields:**
```java
QueryResponse response = retrieve(StorageKeysDb.DB, UserQueries.GET_USER_BY_ID, QueryResponse.class);

// Using JsonPath
String userName = response.getBody().jsonPath().getString("$[0].name");
int userId = response.getBody().jsonPath().getInt("$[0].id");
String status = response.getBody().jsonPath().getString("$[0].status");
```

**Use in Subsequent Operations:**
```java
quest
    .use(RING_OF_DB)
    // First query - get user
    .query(UserQueries.GET_USER_BY_ID.withParam("userId", "123"))

    // Extract user data
    .validate(() -> {
        QueryResponse response = retrieve(StorageKeysDb.DB, UserQueries.GET_USER_BY_ID, QueryResponse.class);
        String userName = response.getBody().jsonPath().getString("$[0].name");
        String userEmail = response.getBody().jsonPath().getString("$[0].email");

        // Use extracted data in assertions or next queries
    })

    // Second query using extracted data
    .query(OrderQueries.GET_ORDERS_BY_USER.withParam("userId", "123"))
    .complete();
```

### JsonPath Extractors

**Define Reusable JsonPath Constants:**
```java
public class DbResponsePaths {
    // User query paths
    public static final String USER_ID = "$[0].id";
    public static final String USER_NAME = "$[0].name";
    public static final String USER_EMAIL = "$[0].email";
    public static final String USER_STATUS = "$[0].status";

    // Order query paths
    public static final String ORDER_ID = "$[0].order_id";
    public static final String ORDER_TOTAL = "$[0].total";
    public static final String ORDER_STATUS = "$[0].status";

    // Aggregation paths
    public static final String ROW_COUNT = "$.length()";
    public static final String ALL_STATUSES = "$[*].status";
}

// Usage
String userId = response.getBody().jsonPath().getString(DbResponsePaths.USER_ID);
```

**Enum-Based JsonPath:**
```java
public enum DbJsonPaths {
    USER_ID("$[0].id"),
    USER_NAME("$[0].name"),
    ORDER_TOTAL("$[0].total"),
    ROW_COUNT("$.length()");

    private final String path;

    DbJsonPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

// Usage
String userId = response.getBody().jsonPath().getString(DbJsonPaths.USER_ID.getPath());
```

---

## Database Configuration

### Environment-Specific Configuration

**Configuration Properties (test_data-dev.properties):**
```properties
# Database connection
db.type=H2_MEM
db.host=localhost
db.port=5432
db.name=testdb
db.username=testuser
db.password=testpass

# Connection pool
db.pool.size=10
db.pool.timeout=30000

# Query timeout
db.query.timeout=10000
```

**Accessing Configuration:**
```java
getDbConfig().type()          // Database type
getDbConfig().host()          // Database host
getDbConfig().port()          // Database port
getDbConfig().name()          // Database name
Data.testData().dbUsername()  // Database username from config
Data.testData().dbPassword()  // Database password from config
```

### Connection Management
Framework automatically manages database connections.

**Connection Lifecycle:**
1. Connection opened when first query executes
2. Connection reused within same test
3. Connection closed when test completes
4. Connection pool used for parallel tests

---

## Common Database Testing Patterns

### Data Verification Pattern
Verify data exists after creation via other modules.

```java
@Test
@API
@DB
void verifyUserInDatabase(
    Quest quest,
    @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest userRequest) {

    quest
        // Create user via API
        .use(RING_OF_API)
        .request(UserEndpoints.CREATE_USER, userRequest)
        .validate(() -> {
            Response response = retrieve(StorageKeysApi.API, UserEndpoints.CREATE_USER, Response.class);
            String userId = response.getBody().jsonPath().getString("user.id");
        })
        .drop()

        // Verify in database
        .use(RING_OF_DB)
        .queryAndValidate(
            UserQueries.GET_USER_BY_ID.withParam("userId", userId),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].name")
                .type(IS)
                .expected(userRequest.getName())
                .build(),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].email")
                .type(IS)
                .expected(userRequest.getEmail())
                .build()
        )
        .complete();
}
```

### Data Cleanup Pattern
Clean up database after test.

```java
@Test
@DB
@Ripper(DataCleaner.Data.DELETE_TEST_USER)
void createAndCleanupUser(Quest quest) {
    quest
        .use(RING_OF_DB)
        .query(UserQueries.INSERT_USER
            .withParam("name", "Test User")
            .withParam("email", "test@example.com")
            .withParam("status", "active"))

        // Verify creation
        .queryAndValidate(
            UserQueries.GET_USER_BY_EMAIL.withParam("email", "test@example.com"),
            Assertion.builder()
                .target(QUERY_RESULT)
                .type(NOT_EMPTY)
                .build()
        )
        // @Ripper automatically deletes user after test
        .complete();
}
```

### Data Aggregation Pattern
Validate aggregated data and counts.

```java
@Test
@DB
void validateUserStatistics(Quest quest) {
    String aggregateQuery1 = "SELECT COUNT(*) as count FROM users WHERE status = 'active'";
    String aggregateQuery2 = """
        SELECT user_id, COUNT(*) as order_count, SUM(total) as total_spent
        FROM orders
        GROUP BY user_id
        HAVING COUNT(*) > :minOrders
        """;

    quest
        .use(RING_OF_DB)

        // Count active users
        .queryAndValidate(
            Query.of(aggregateQuery1),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].count")
                .type(GREATER_THAN)
                .expected(0)
                .build()
        )

        // Get order statistics
        .queryAndValidate(
            Query.of(aggregateQuery2).withParam("minOrders", 5),
            Assertion.builder()
                .target(QUERY_RESULT)
                .type(NOT_EMPTY)
                .build(),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[*].order_count")
                .type(GREATER_THAN)
                .expected(5)
                .build()
        )

        .complete();
}
```

### Data Consistency Pattern
Validate referential integrity and consistency.

```java
@Test
@DB
void validateDataConsistency(Quest quest) {
    String query1 = """
        SELECT o.id, o.user_id
        FROM orders o
        LEFT JOIN users u ON o.user_id = u.id
        WHERE u.id IS NULL
        """;

    String query2 = """
        SELECT o.id, o.total, SUM(oi.price * oi.quantity) as calculated_total
        FROM orders o
        JOIN order_items oi ON o.id = oi.order_id
        GROUP BY o.id, o.total
        HAVING o.total != SUM(oi.price * oi.quantity)
        """;

    quest
        .use(RING_OF_DB)

        // Verify all orders have valid user IDs
        .queryAndValidate(
            Query.of(query1),
            Assertion.builder()
                .target(QUERY_RESULT)
                .type(LENGTH)
                .expected(0)
                .build()
        )

        // Verify all order totals match item sums
        .queryAndValidate(
            Query.of(query2),
            Assertion.builder()
                .target(QUERY_RESULT)
                .type(LENGTH)
                .expected(0)
                .build()
        )

        .complete();
}
```

### Data Migration Validation Pattern
Verify database schema and migrations.

```java
@Test
@DB
@DbHook(when = BEFORE, type = DbMigrationFlow.class)
void validateSchemaAfterMigration(Quest quest) {
    String tableQuery = "SELECT COUNT(*) as count FROM information_schema.tables WHERE table_name = 'users'";
    String columnQuery = """
        SELECT COUNT(*) as count 
        FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'email'
        """;
    String indexQuery = """
        SELECT COUNT(*) as count 
        FROM information_schema.statistics 
        WHERE table_name = 'users' AND index_name = 'idx_users_email'
        """;

    quest
        .use(RING_OF_DB)

        // Verify table exists
        .queryAndValidate(
            Query.of(tableQuery),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].count")
                .type(IS)
                .expected(1)
                .build()
        )

        // Verify column exists
        .queryAndValidate(
            Query.of(columnQuery),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].count")
                .type(IS)
                .expected(1)
                .build()
        )

        // Verify index exists
        .queryAndValidate(
            Query.of(indexQuery),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].count")
                .type(IS)
                .expected(1)
                .build()
        )

        .complete();
}
```

### Cross-Module Data Flow Pattern
Validate data flow across API, UI, and database.

```java
@Test
@API
@UI
@DB
void validateCompleteDataFlow(
    Quest quest,
    @Craft(model = DataCreator.Data.ORDER_REQUEST) OrderRequest orderRequest) {

    quest
        // Create order via API
        .use(RING_OF_API)
        .request(OrderEndpoints.CREATE_ORDER, orderRequest)
        .validate(() -> {
            Response response = retrieve(StorageKeysApi.API, OrderEndpoints.CREATE_ORDER, Response.class);
            String orderId = response.getBody().jsonPath().getString("order.id");
        })
        .drop()

        // Verify in database
        .use(RING_OF_DB)
        .queryAndValidate(
            OrderQueries.GET_ORDER_BY_ID.withParam("orderId", orderId),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].status")
                .type(IS)
                .expected("pending")
                .build()
        )
        .drop()

        // Verify in UI
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/orders/" + orderId)
        .alert().validateValue(AlertFields.ORDER_STATUS, "Pending")

        .complete();
}
```

---

## Quest API Surface for Database Testing

**Quest maintains high-level abstraction and does NOT expose internal details.**

### ✅ Available Methods
```java
quest
    .use(RING_OF_DB)
    .query(UserQueries.GET_USER_BY_ID.withParam("userId", "123"))
    .queryAndValidate(query, assertions)
    .validate(() -> { /* custom validation */ })
    .complete();
```

### ❌ NOT Available
```java
// ❌ These methods don't exist
quest.getDriver()          // Method doesn't exist
quest.getStorage()         // Method doesn't exist
quest.getConnection()      // Method doesn't exist

// ❌ WRONG: Trying to access internals
.validate(() -> {
    Connection conn = quest.getConnection();  // Compilation error
})

// ✅ CORRECT: Use framework services
QueryResponse response = retrieve(StorageKeysDb.DB, query, QueryResponse.class);
```

**Rule:** Always use service ring methods and framework retrieval functions. Never attempt to access Quest internals.

---

## Preconditions and Cleanup

See [core-framework-instructions.md](core-framework-instructions.md) for @Journey and @Ripper fundamentals.

### Database-Specific Journey Example
```java
@Test
@Journey(Preconditions.Data.SEED_TEST_DATA)
@DB
void testWithDatabasePrecondition(Quest quest) {
    // Test data already seeded before test starts
    quest
        .use(RING_OF_DB)
        .queryAndValidate(
            UserQueries.GET_ALL_USERS,
            Assertion.builder()
                .target(QUERY_RESULT)
                .type(NOT_EMPTY)
                .build()
        )
        .complete();
}
```

### Database-Specific Ripper Example
```java
@Test
@Ripper(DataCleaner.Data.CLEANUP_TEST_DATA)
@DB
void testWithDatabaseCleanup(Quest quest) {
    quest
        .use(RING_OF_DB)
        .query(UserQueries.INSERT_USER
            .withParam("name", "Test User")
            .withParam("email", "test@example.com")
            .withParam("status", "active"))
        // @Ripper automatically cleans up test data after test
        .complete();
}
```

---

## Summary: Key Takeaways

### Database Type Definition (MANDATORY)
* Define database types in enum implementing `DbType<T>` interface
* Implement required methods: `driver()`, `protocol()`, `enumImpl()`
* Use nested `Data` class for annotation references
* Support multiple databases for environment flexibility

### Query Definition (MANDATORY)
* Define queries in dedicated classes or enums
* Use parameterized queries with `:paramName` syntax
* Keep queries focused and single-purpose
* Never concatenate SQL with parameters (SQL injection risk)

### Query Execution (MANDATORY)
* Use `.query(query)` for execution without validation
* Use `.queryAndValidate(query, assertions...)` for inline validation
* Customize with `.withParam("key", value)` for parameters
* Results automatically stored in `StorageKeysDb.DB`

### Validation (MANDATORY)
* Use `Assertion.builder()` with `QUERY_RESULT` target
* Use JsonPath for result field extraction
* Use `.soft(true)` for soft assertions
* Validate row counts, field values, and data consistency

### Database Hooks (@DbHook)
* Use `@DbHook(when = BEFORE)` for setup
* Use `@DbHook(when = AFTER)` for cleanup
* Implement `DbHookFlow` interface
* Use for schema creation, seeding, migrations

### Storage and Retrieval
* Query results automatically stored with query as key
* Use `retrieve(StorageKeysDb.DB, query, QueryResponse.class)` for retrieval
* Extract to DTOs or use JsonPath
* Use extracted data in subsequent operations

### Quest Abstraction (MANDATORY)
* Never access `quest.getConnection()` or `quest.getStorage()` (don't exist)
* Always use service ring methods
* Use framework retrieval functions

### References
* Core framework concepts → [core-framework-instructions.md](core-framework-instructions.md)
* Mandatory standards → [rules.md](../../rules/rules.md)
* Best practices → [best-practices.md](../../rules/best-practices.md)
