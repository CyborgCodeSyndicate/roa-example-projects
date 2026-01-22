# **api-framework-instructions.md**

## About This Document
**This is the single source of truth for API-specific patterns and architecture in the ROA framework.**

For core framework concepts (Quest DSL, @Craft, @Journey, @Ripper, service rings, validation fundamentals), see [core-framework-instructions.md](core-framework-instructions.md).

This file contains **API-specific architecture, endpoint system, and API testing patterns only**.

---

## Prerequisites
Before implementing API tests, read [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals and public API
- Service ring management (.use(), .drop(), .complete())
- Test data management (@Craft annotation and Late<> initialization)
- Preconditions (@Journey) and cleanup (@Ripper)
- General validation requirements
- Storage and data retrieval patterns

**This file assumes you understand those core concepts.**

---

## Interface Contracts

### Endpoint Interface
**CRITICAL:** All endpoint enums must implement the `Endpoint<T>` interface.

**Required Methods:**
```java
Method method();           // HTTP method (GET, POST, PUT, DELETE, PATCH)
String url();             // Relative URL path
Enum<?> enumImpl();       // Returns this enum constant
```

**Optional Methods:**
```java
default RequestSpecification defaultConfiguration(RequestSpecification spec) {
    // Override to set common configurations (content type, base URL, headers)
    return spec;
}

default Map<String, List<String>> headers() {
    // Override to define default headers for this endpoint
    return Collections.emptyMap();
}
```

**Example:**
```java
public enum UserEndpoints implements Endpoint<UserEndpoints> {
    GET_USER(Method.GET, "/users/{userId}"),
    CREATE_USER(Method.POST, "/users"),
    UPDATE_USER(Method.PUT, "/users/{userId}"),
    DELETE_USER(Method.DELETE, "/users/{userId}");

    private final Method method;
    private final String url;

    UserEndpoints(Method method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

    @Override
    public RequestSpecification defaultConfiguration(RequestSpecification spec) {
        return spec
            .contentType(ContentType.JSON)
            .baseUri(getApiConfig().baseUrl())
            .header("Accept", "application/json");
    }

    // Nested Data class for annotation references
    public static final class Data {
        public static final String GET_USER = "GET_USER";
        public static final String CREATE_USER = "CREATE_USER";
        public static final String UPDATE_USER = "UPDATE_USER";
        public static final String DELETE_USER = "DELETE_USER";
        private Data() {}
    }
}
```

**Common Compilation Error:**
```java
// ❌ WRONG - missing enumImpl()
public enum OrderEndpoints implements Endpoint<OrderEndpoints> {
    GET_ORDER(Method.GET, "/orders/{id}");
    // Missing enumImpl() method - causes compilation error
}

// ✅ CORRECT
public enum OrderEndpoints implements Endpoint<OrderEndpoints> {
    GET_ORDER(Method.GET, "/orders/{id}");

    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
```

---

## API Test Class Structure

### Required Annotations
**Class Level:**
* `@API` annotation on each API test class (required)
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
        .use(RING_OF_API)           // Activate API service ring
        .request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
        .complete();                // MUST end with .complete()
}
```

**Multi-Module Tests:**
```java
@Test
@API
@UI
void crossModuleTest(Quest quest) {
    quest
        .use(RING_OF_API)
        .request(OrderEndpoints.CREATE_ORDER, orderDto)
        .drop()                     // ✅ Use .drop() when switching rings
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/orders")
        .complete();
}
```

See [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals
- Service ring management
- .complete() requirement
- Validation requirements

---

## Endpoint Definition

### Endpoint Structure
Define all API endpoints in dedicated enum classes implementing `Endpoint<T>` interface.

**Basic Endpoint Definition:**
```java
public enum AuthEndpoints implements Endpoint<AuthEndpoints> {
    LOGIN(Method.POST, "/auth/login"),
    LOGOUT(Method.POST, "/auth/logout"),
    REFRESH_TOKEN(Method.POST, "/auth/refresh");

    private final Method method;
    private final String url;

    AuthEndpoints(Method method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public Method method() { return method; }

    @Override
    public String url() { return url; }

    @Override
    public Enum<?> enumImpl() { return this; }

    @Override
    public RequestSpecification defaultConfiguration(RequestSpecification spec) {
        return spec
            .contentType(ContentType.JSON)
            .baseUri(getApiConfig().baseUrl());
    }
}
```

### Default Configuration
Override `defaultConfiguration()` to set common request attributes.

**Common Configurations:**
* Content type (JSON, XML, form data)
* Base URI
* Common headers (Accept, User-Agent)
* Authentication tokens (if static)
* Request/response logging

**Example with Multiple Configurations:**
```java
@Override
public RequestSpecification defaultConfiguration(RequestSpecification spec) {
    return spec
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .baseUri(getApiConfig().baseUrl())
        .header("User-Agent", "ROA-Framework/1.0")
        .header("X-API-Version", "v1")
        .log().all()  // Log all requests for debugging
        .relaxedHTTPSValidation();  // For test environments
}
```

### Header Management
Override `headers()` to define default headers applied to all requests.

**Example:**
```java
@Override
public Map<String, List<String>> headers() {
    Map<String, List<String>> headers = new HashMap<>();
    headers.put("Content-Type", List.of("application/json"));
    headers.put("Accept", List.of("application/json"));
    headers.put("X-Client-Type", List.of("automated-test"));
    return headers;
}
```

**Header Priority:**
1. Endpoint default headers (from `headers()`)
2. Default configuration headers (from `defaultConfiguration()`)
3. Test-specific headers (from `.withHeader()` in test)

Test-specific headers override default headers.

### Endpoint Organization

**Best Practices:**
* Group endpoints by domain or API version
* One enum per microservice or API module
* Use descriptive enum constant names
* Maintain consistent naming patterns

**Example: Organized by Domain:**
```
api/endpoints/
├── UserEndpoints.java       // User management APIs
├── OrderEndpoints.java      // Order management APIs
├── PaymentEndpoints.java    // Payment processing APIs
└── AuthEndpoints.java       // Authentication APIs
```

**Naming Conventions:**
```java
// ✅ Good: Clear, descriptive names
GET_USER_BY_ID
CREATE_NEW_ORDER
UPDATE_USER_PROFILE
DELETE_PAYMENT_METHOD

// ❌ Avoid: Generic, unclear names
ENDPOINT1
USER_API
GET_DATA
```

### Parameterized Endpoints

**Path Parameters:**
Use `{paramName}` placeholders in URL.

```java
GET_USER(Method.GET, "/users/{userId}"),
GET_ORDER(Method.GET, "/orders/{orderId}/items/{itemId}"),
UPDATE_STATUS(Method.PUT, "/products/{productId}/status");

// Usage in tests
.request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
.request(OrderEndpoints.GET_ORDER
    .withPathParam("orderId", "456")
    .withPathParam("itemId", "789"))
```

**Query Parameters:**
Add dynamically in tests using `.withQueryParam()`.

```java
// Endpoint definition - no query params in URL
SEARCH_USERS(Method.GET, "/users/search");

// Usage in tests
.request(UserEndpoints.SEARCH_USERS
    .withQueryParam("name", "John")
    .withQueryParam("status", "active")
    .withQueryParam("page", 1))
// Results in: /users/search?name=John&status=active&page=1
```

**Nested Data Class Pattern:**
```java
public enum ProductEndpoints implements Endpoint<ProductEndpoints> {
    GET_PRODUCT(Method.GET, "/products/{id}"),
    LIST_PRODUCTS(Method.GET, "/products");

    // Nested Data class for annotation references
    public static final class Data {
        public static final String GET_PRODUCT = "GET_PRODUCT";
        public static final String LIST_PRODUCTS = "LIST_PRODUCTS";
        private Data() {}
    }
}

// Usage in annotations
@Journey(ProductEndpoints.Data.GET_PRODUCT)
```

---

## Request Execution

### Making Requests

**Simple Request (no validation):**
```java
quest
    .use(RING_OF_API)
    .request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
    .complete();
```

**Request with Body:**
```java
@Test
void createUser(Quest quest, @Craft(model = DataCreator.Data.USER) User userDto) {
    quest
        .use(RING_OF_API)
        .request(UserEndpoints.CREATE_USER, userDto)  // userDto as request body
        .complete();
}
```

**Request with Validation:**
```java
quest
    .use(RING_OF_API)
    .requestAndValidate(
        UserEndpoints.GET_USER.withPathParam("userId", "123"),
        Assertion.builder()
            .target(STATUS)
            .type(IS)
            .expected(200)
            .build()
    )
    .complete();
```

### Request Customization

**Path Parameters:**
```java
.request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
.request(OrderEndpoints.GET_ORDER_ITEM
    .withPathParam("orderId", "456")
    .withPathParam("itemId", "789"))
```

**Query Parameters:**
```java
.request(UserEndpoints.SEARCH_USERS
    .withQueryParam("name", "John")
    .withQueryParam("status", "active")
    .withQueryParam("limit", 10))
```

**Headers:**
```java
.request(UserEndpoints.GET_USER
    .withPathParam("userId", "123")
    .withHeader("Authorization", "Bearer " + token)
    .withHeader("X-Request-ID", requestId))
```

**Combined Customization:**
```java
quest
    .use(RING_OF_API)
    .request(OrderEndpoints.SEARCH_ORDERS
        .withQueryParam("status", "pending")
        .withQueryParam("page", 1)
        .withHeader("Authorization", "Bearer " + token)
        .withHeader("X-User-ID", userId))
    .complete();
```

### Request Methods Reference

| HTTP Method | Endpoint Definition | Usage |
|-------------|-------------------|-------|
| GET | `Method.GET` | Retrieve resources |
| POST | `Method.POST` | Create resources |
| PUT | `Method.PUT` | Update entire resource |
| PATCH | `Method.PATCH` | Partial update |
| DELETE | `Method.DELETE` | Delete resources |
| HEAD | `Method.HEAD` | Retrieve headers only |
| OPTIONS | `Method.OPTIONS` | Get supported methods |

---

## Response Validation

### Assertion Builder Pattern
API validation uses `Assertion.builder()` for declarative validation.

**Basic Status Code Validation:**
```java
quest
    .use(RING_OF_API)
    .requestAndValidate(
        UserEndpoints.GET_USER.withPathParam("userId", "123"),
        Assertion.builder()
            .target(STATUS)
            .type(IS)
            .expected(200)
            .build()
    )
    .complete();
```

### Assertion Targets

| Target | Description | Use Case |
|--------|-------------|----------|
| `STATUS` | HTTP status code | Validate response status (200, 404, 500, etc.) |
| `BODY` | Response body content | Validate JSON/XML response data using JsonPath |
| `HEADER` | Response headers | Validate header values (Content-Type, Cache-Control, etc.) |

### Assertion Types

**Equality Checks:**
* `IS` - Exact match
* `NOT` - Not equal
* `EQUALS_IGNORE_CASE` - Case-insensitive match

**Comparison:**
* `GREATER_THAN` - Numeric greater than
* `LESS_THAN` - Numeric less than
* `GREATER_THAN_OR_EQUAL` - Numeric >=
* `LESS_THAN_OR_EQUAL` - Numeric <=
* `BETWEEN` - Within range

**String Operations:**
* `CONTAINS` - Contains substring
* `STARTS_WITH` - Starts with prefix
* `ENDS_WITH` - Ends with suffix
* `MATCHES_REGEX` - Matches regex pattern

**Null/Empty Checks:**
* `NOT_NULL` - Value is not null
* `NOT_EMPTY` - Collection/string not empty
* `ALL_NOT_NULL` - All values in array not null

**Collection Operations:**
* `CONTAINS_ALL` - Contains all specified values
* `CONTAINS_ANY` - Contains at least one value
* `LENGTH` - Collection/string length matches

### Status Code Validation

```java
// Success status
Assertion.builder()
    .target(STATUS)
    .type(IS)
    .expected(200)
    .build()

// Created status
Assertion.builder()
    .target(STATUS)
    .type(IS)
    .expected(201)
    .build()

// Error status
Assertion.builder()
    .target(STATUS)
    .type(IS)
    .expected(404)
    .build()
```

### Body Validation with JsonPath

**Single Field Validation:**
```java
Assertion.builder()
    .target(BODY)
    .key("$.user.name")           // JsonPath expression
    .type(IS)
    .expected("John Doe")
    .build()
```

**Nested Field Validation:**
```java
Assertion.builder()
    .target(BODY)
    .key("$.data.order.items[0].price")
    .type(GREATER_THAN)
    .expected(0)
    .build()
```

**Array Validation:**
```java
// Check array length
Assertion.builder()
    .target(BODY)
    .key("$.users")
    .type(LENGTH)
    .expected(10)
    .build()

// Check array contains value
Assertion.builder()
    .target(BODY)
    .key("$.users[*].status")
    .type(CONTAINS)
    .expected("active")
    .build()
```

**Null Check:**
```java
Assertion.builder()
    .target(BODY)
    .key("$.user.email")
    .type(NOT_NULL)
    .build()
```

### Header Validation

```java
Assertion.builder()
    .target(HEADER)
    .key("Content-Type")
    .type(CONTAINS)
    .expected("application/json")
    .build()

Assertion.builder()
    .target(HEADER)
    .key("Cache-Control")
    .type(IS)
    .expected("no-cache")
    .build()
```

### Multiple Validations

**Separate Assertions:**
```java
quest
    .use(RING_OF_API)
    .requestAndValidate(
        UserEndpoints.GET_USER.withPathParam("userId", "123"),
        Assertion.builder()
            .target(STATUS)
            .type(IS)
            .expected(200)
            .build(),
        Assertion.builder()
            .target(BODY)
            .key("$.user.name")
            .type(NOT_NULL)
            .build(),
        Assertion.builder()
            .target(BODY)
            .key("$.user.email")
            .type(CONTAINS)
            .expected("@example.com")
            .build()
    )
    .complete();
```

### Soft Assertions

**Use `.soft(true)` to collect all failures:**
```java
quest
    .use(RING_OF_API)
    .requestAndValidate(
        UserEndpoints.GET_USER.withPathParam("userId", "123"),
        Assertion.builder()
            .target(BODY)
            .key("$.user.name")
            .type(IS)
            .expected("John Doe")
            .soft(true)  // Continues if this fails
            .build(),
        Assertion.builder()
            .target(BODY)
            .key("$.user.email")
            .type(IS)
            .expected("john@example.com")
            .soft(true)  // Continues if this fails
            .build(),
        Assertion.builder()
            .target(BODY)
            .key("$.user.status")
            .type(IS)
            .expected("active")
            .soft(true)  // Reports all failures together
            .build()
    )
    .complete();
```

---

## Storage and Retrieval

### Automatic Storage
All API responses are automatically stored in `StorageKeysApi.API` with the endpoint reference as the key.

**Storage happens automatically:**
```java
quest
    .use(RING_OF_API)
    .request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
    // Response automatically stored with key: UserEndpoints.GET_USER
    .complete();
```

### Retrieving Responses

**Basic Retrieval:**
```java
Response response = retrieve(StorageKeysApi.API, UserEndpoints.GET_USER, Response.class);
```

**Extract to DTO:**
```java
// Get response and map to DTO
Response response = retrieve(StorageKeysApi.API, UserEndpoints.GET_USER, Response.class);
UserDto user = response.getBody().as(UserDto.class);

// Access DTO fields
String userName = user.getName();
String userEmail = user.getEmail();
```

**Extract Specific Fields:**
```java
Response response = retrieve(StorageKeysApi.API, UserEndpoints.GET_USER, Response.class);

// Using JsonPath
String userName = response.getBody().jsonPath().getString("user.name");
int userId = response.getBody().jsonPath().getInt("user.id");
List<String> roles = response.getBody().jsonPath().getList("user.roles");
```

**Use in Subsequent Requests:**
```java
quest
    .use(RING_OF_API)
    // First request - create user
    .request(UserEndpoints.CREATE_USER, newUser)

    // Retrieve created user ID
    .validate(() -> {
        Response createResponse = retrieve(StorageKeysApi.API, UserEndpoints.CREATE_USER, Response.class);
        String userId = createResponse.getBody().jsonPath().getString("user.id");

        // Store for later use
        // Use in next request
    })

    // Second request - get created user
    .request(UserEndpoints.GET_USER.withPathParam("userId", userId))
    .complete();
```

### JsonPath Extractors

**Define Reusable JsonPath Constants:**
```java
public class UserResponsePaths {
    public static final String USER_ID = "$.user.id";
    public static final String USER_NAME = "$.user.name";
    public static final String USER_EMAIL = "$.user.email";
    public static final String USER_ROLES = "$.user.roles";
    public static final String USER_CREATED_AT = "$.user.createdAt";
}

// Usage
String userId = response.getBody().jsonPath().getString(UserResponsePaths.USER_ID);
```

**Enum-Based JsonPath:**
```java
public enum ApiJsonPaths {
    USER_ID("$.user.id"),
    USER_NAME("$.user.name"),
    ORDER_TOTAL("$.order.total"),
    ITEMS_COUNT("$.order.items.length()");

    private final String path;

    ApiJsonPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

// Usage
String userId = response.getBody().jsonPath().getString(ApiJsonPaths.USER_ID.getPath());
```

---

## Authentication

### @AuthenticateViaApi
Automatic API authentication before test execution.

**Basic Usage:**
```java
@Test
@AuthenticateViaApi(
    credentials = AdminCredentials.class,
    type = ApiAuthFlow.class
)
void authenticatedTest(Quest quest) {
    // Already authenticated when test starts
    quest
        .use(RING_OF_API)
        .request(UserEndpoints.GET_PROTECTED_DATA)
        .complete();
}
```

**Credentials Class:**
```java
public class AdminCredentials implements Credentials {
    @Override
    public String username() {
        return Data.testData().adminUsername();
    }

    @Override
    public String password() {
        return Data.testData().adminPassword();
    }
}
```

**Authentication Flow Implementation:**
```java
public class ApiAuthFlow implements ApiAuth {
    @Override
    public void authenticate(Quest quest, Credentials credentials) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(credentials.username());
        loginRequest.setPassword(credentials.password());

        quest
            .use(RING_OF_API)
            .request(AuthEndpoints.LOGIN, loginRequest)
            .validate(() -> {
                Response response = retrieve(StorageKeysApi.API, AuthEndpoints.LOGIN, Response.class);
                String token = response.getBody().jsonPath().getString("token");

                // Store token for subsequent requests
                // Token can be accessed in tests or added to headers automatically
            })
            .drop();
    }
}
```

**Session Caching (Optional):**
```java
@AuthenticateViaApi(
    credentials = AdminCredentials.class,
    type = ApiAuthFlow.class,
    cacheCredentials = true  // Reuse authentication across tests
)
```

See [core-framework-instructions.md](core-framework-instructions.md) for general authentication patterns.

---

## API Test Data Management

See [core-framework-instructions.md](core-framework-instructions.md) for @Craft fundamentals.

### API-Specific @Craft Usage

**Request DTOs:**
```java
@Test
void createUser(Quest quest, @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest userRequest) {
    quest
        .use(RING_OF_API)
        .request(UserEndpoints.CREATE_USER, userRequest)
        .requestAndValidate(
            UserEndpoints.CREATE_USER,
            Assertion.builder()
                .target(STATUS)
                .type(IS)
                .expected(201)
                .build()
        )
        .complete();
}
```

**Late Initialization:**
```java
@Test
void updateUserAfterCreation(
    Quest quest,
    @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest createRequest,
    Late<@Craft(model = DataCreator.Data.USER_UPDATE)> updateRequestLate) {

    quest
        .use(RING_OF_API)
        // Create user
        .request(UserEndpoints.CREATE_USER, createRequest)

        // Get created user ID
        .validate(() -> {
            Response response = retrieve(StorageKeysApi.API, UserEndpoints.CREATE_USER, Response.class);
            String userId = response.getBody().jsonPath().getString("user.id");
        })

        // Update user with runtime data
        .request(
            UserEndpoints.UPDATE_USER.withPathParam("userId", userId),
            updateRequestLate.create()  // Lazy initialization
        )
        .complete();
}
```

---

## API Configuration

### Environment-Specific URLs
```java
// ✅ CORRECT: Use configuration
.request(UserEndpoints.GET_USER.withPathParam("userId", "123"))

// In endpoint definition
@Override
public RequestSpecification defaultConfiguration(RequestSpecification spec) {
    return spec.baseUri(getApiConfig().baseUrl());
}

// ❌ WRONG: Hardcoded URL
.request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
// With hardcoded baseUri in endpoint
```

### Configuration Properties
**test_data-dev.properties:**
```properties
api.base.url=http://localhost:8080/api/v1
api.timeout=10000
api.retry.count=3
```

**Accessing Configuration:**
```java
getApiConfig().baseUrl()       // API base URL
getApiConfig().timeout()       // Request timeout
Data.testData().apiKey()       // API keys from config
```

---

## Common API Testing Patterns

### Create-Read-Update-Delete (CRUD) Pattern
```java
@Test
void fullCrudFlow(
    Quest quest,
    @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest createRequest,
    @Craft(model = DataCreator.Data.USER_UPDATE) UserRequest updateRequest) {

    quest
        .use(RING_OF_API)

        // CREATE
        .requestAndValidate(
            UserEndpoints.CREATE_USER, createRequest,
            Assertion.builder().target(STATUS).type(IS).expected(201).build()
        )

        // Extract user ID
        .validate(() -> {
            Response response = retrieve(StorageKeysApi.API, UserEndpoints.CREATE_USER, Response.class);
            String userId = response.getBody().jsonPath().getString("user.id");
        })

        // READ
        .requestAndValidate(
            UserEndpoints.GET_USER.withPathParam("userId", userId),
            Assertion.builder().target(STATUS).type(IS).expected(200).build(),
            Assertion.builder().target(BODY).key("$.user.name").type(IS).expected(createRequest.getName()).build()
        )

        // UPDATE
        .requestAndValidate(
            UserEndpoints.UPDATE_USER.withPathParam("userId", userId), updateRequest,
            Assertion.builder().target(STATUS).type(IS).expected(200).build()
        )

        // DELETE
        .requestAndValidate(
            UserEndpoints.DELETE_USER.withPathParam("userId", userId),
            Assertion.builder().target(STATUS).type(IS).expected(204).build()
        )

        .complete();
}
```

### Search and Filter Pattern
```java
@Test
@AuthenticateViaApi(credentials = AdminCredentials.class, type = ApiAuthFlow.class)
void searchUsers(Quest quest) {
    quest
        .use(RING_OF_API)
        .requestAndValidate(
            UserEndpoints.SEARCH_USERS
                .withQueryParam("status", "active")
                .withQueryParam("role", "admin")
                .withQueryParam("limit", 10),
            Assertion.builder()
                .target(STATUS)
                .type(IS)
                .expected(200)
                .build(),
            Assertion.builder()
                .target(BODY)
                .key("$.users")
                .type(LENGTH)
                .expected(10)
                .soft(true)
                .build(),
            Assertion.builder()
                .target(BODY)
                .key("$.users[*].status")
                .type(CONTAINS_ALL)
                .expected(List.of("active"))
                .soft(true)
                .build()
        )
        .complete();
}
```

### Pagination Pattern
```java
@Test
void paginatedResults(Quest quest) {
    quest
        .use(RING_OF_API)

        // First page
        .requestAndValidate(
            UserEndpoints.LIST_USERS
                .withQueryParam("page", 1)
                .withQueryParam("limit", 20),
            Assertion.builder().target(STATUS).type(IS).expected(200).build(),
            Assertion.builder().target(BODY).key("$.data.length()").type(IS).expected(20).build(),
            Assertion.builder().target(BODY).key("$.pagination.currentPage").type(IS).expected(1).build()
        )

        // Second page
        .requestAndValidate(
            UserEndpoints.LIST_USERS
                .withQueryParam("page", 2)
                .withQueryParam("limit", 20),
            Assertion.builder().target(STATUS).type(IS).expected(200).build(),
            Assertion.builder().target(BODY).key("$.pagination.currentPage").type(IS).expected(2).build()
        )

        .complete();
}
```

### Error Handling Pattern
```java
@Test
void errorHandling(Quest quest) {
    quest
        .use(RING_OF_API)

        // Not found
        .requestAndValidate(
            UserEndpoints.GET_USER.withPathParam("userId", "nonexistent"),
            Assertion.builder().target(STATUS).type(IS).expected(404).build(),
            Assertion.builder().target(BODY).key("$.error.message").type(CONTAINS).expected("not found").build()
        )

        // Unauthorized
        .requestAndValidate(
            UserEndpoints.GET_PROTECTED_DATA,
            Assertion.builder().target(STATUS).type(IS).expected(401).build()
        )

        // Validation error
        .requestAndValidate(
            UserEndpoints.CREATE_USER, invalidUser,
            Assertion.builder().target(STATUS).type(IS).expected(400).build(),
            Assertion.builder().target(BODY).key("$.errors").type(NOT_EMPTY).build()
        )

        .complete();
}
```

### Cross-Module Integration Pattern
```java
@Test
@API
@DB
void apiAndDatabaseConsistency(Quest quest, @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest userRequest) {
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
        .requestAndValidate(
            DbQueries.GET_USER_BY_ID.withParam("userId", userId),
            Assertion.builder()
                .target(QUERY_RESULT)
                .key("$[0].name")
                .type(IS)
                .expected(userRequest.getName())
                .build()
        )
        .complete();
}
```

---

## Quest API Surface for API Testing

**Quest maintains high-level abstraction and does NOT expose internal details.**

### ✅ Available Methods
```java
quest
    .use(RING_OF_API)
    .request(UserEndpoints.GET_USER.withPathParam("userId", "123"))
    .requestAndValidate(endpoint, assertions)
    .validate(() -> { /* custom validation */ })
    .complete();
```

### ❌ NOT Available
```java
// ❌ These methods don't exist
quest.getDriver()          // Method doesn't exist
quest.getStorage()         // Method doesn't exist
quest.getConfiguration()   // Method doesn't exist

// ❌ WRONG: Trying to access internals
.validate(() -> {
    Storage storage = quest.getStorage();  // Compilation error
})

// ✅ CORRECT: Use framework services
Response response = retrieve(StorageKeysApi.API, endpoint, Response.class);
```

**Rule:** Always use service ring methods and framework retrieval functions. Never attempt to access Quest internals.

---

## Preconditions and Cleanup

See [core-framework-instructions.md](core-framework-instructions.md) for @Journey and @Ripper fundamentals.

### API-Specific Journey Example
```java
@Test
@Journey(Preconditions.Data.CREATE_USER_VIA_API)
@JourneyData(model = DataCreator.Data.USER_REQUEST)
void testWithApiPrecondition(Quest quest) {
    // User already created via API before test starts
    quest
        .use(RING_OF_API)
        .requestAndValidate(
            UserEndpoints.LIST_USERS,
            Assertion.builder()
                .target(BODY)
                .key("$.users")
                .type(NOT_EMPTY)
                .build()
        )
        .complete();
}
```

### API-Specific Ripper Example
```java
@Test
@Ripper(DataCleaner.Data.DELETE_USER_VIA_API)
void testWithApiCleanup(Quest quest, @Craft(model = DataCreator.Data.USER_REQUEST) UserRequest userRequest) {
    quest
        .use(RING_OF_API)
        .request(UserEndpoints.CREATE_USER, userRequest)
        // @Ripper automatically deletes user after test
        .complete();
}
```

---

## Summary: Key Takeaways

### Endpoint Definition (MANDATORY)
* Define endpoints in enums implementing `Endpoint<T>` interface
* Implement required methods: `method()`, `url()`, `enumImpl()`
* Override `defaultConfiguration()` for common settings
* Override `headers()` for default headers
* Use nested `Data` class for annotation references

### Request Execution (MANDATORY)
* Use `.request(endpoint)` for execution without validation
* Use `.requestAndValidate(endpoint, assertions...)` for inline validation
* Customize with `.withPathParam()`, `.withQueryParam()`, `.withHeader()`
* Responses automatically stored in `StorageKeysApi.API`

### Validation (MANDATORY)
* Use `Assertion.builder()` for declarative validation
* Target: `STATUS` for status codes, `BODY` for JSON data, `HEADER` for headers
* Use JsonPath for body field extraction
* Use `.soft(true)` for soft assertions

### Storage and Retrieval
* Responses automatically stored with endpoint as key
* Use `retrieve(StorageKeysApi.API, endpoint, Response.class)` for retrieval
* Extract to DTOs: `response.getBody().as(DtoClass.class)`
* Extract fields: `response.getBody().jsonPath().getString(path)`

### Quest Abstraction (MANDATORY)
* Never access `quest.getDriver()` or `quest.getStorage()` (don't exist)
* Always use service ring methods
* Use framework retrieval functions

### References
* Core framework concepts → [core-framework-instructions.md](core-framework-instructions.md)
* Mandatory standards → [rules.md](../../rules/rules.md)
* Best practices → [best-practices.md](../../rules/best-practices.md)
