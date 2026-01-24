# **core-framework-instructions.md**

## About This Document
**This is the single source of truth for core ROA framework concepts.**

Module-specific files (ui-framework-instructions.md, api-framework-instructions.md, db-framework-instructions.md) reference these core concepts and add module-specific details.

## Core Framework Principles
The ROA (Ring of Abstraction) test framework is built on fluent API design, type-safe data injection, 
declarative validation, and automated lifecycle management.

### Module-Specific Instructions
**The ROA framework supports modular architecture with independent modules:**
* Projects may have one or more modules (testing technologies) (ui directory | api directory | db directory):
* Each module has its own service ring with module-specific operations
* Module-specific patterns and contracts are documented in separate instruction files

**Module Documentation Structure:**
* **Core Framework** (this file): Universal concepts applicable to all modules
* **Module-Specific Instructions**: Module-specific patterns, components, and validation strategies
    - `ui-framework-instructions.md` - UI testing patterns (if UI module exists)
    - `api-framework-instructions.md` - API testing patterns (if API module exists)
    - `db-framework-instructions.md` - Database testing patterns (if DB module exists)

**When working with a module:**
1. Read this core framework instructions file first
2. Read the module-specific instructions file for detailed patterns
3. Check module-specific examples for implementation guidance
4. Follow module-specific validation patterns (they differ by module)

**Important:** Not all projects have all modules. Only use modules that exist in your project structure.

### Test Class Structure
**Class Inheritance**
* Extend `BaseQuest` for standard test execution
* Extend `BaseQuestSequential` when tests must execute sequentially within the class

### Test Method Structure
**Quest Object**
* Every test method must accept a Quest parameter as the first argument
* The Quest object orchestrates all test operations via fluent API
* Never instantiate Quest manually; let the framework inject it
* Quest provides a minimal public API to maintain high-level abstraction
* Quest does NOT expose internal framework details (driver, storage, configuration)

### Test Categorization
**Test Tags**
* Use `@Smoke` annotation for smoke tests that verify critical functionality
* Use `@Regression` annotation for regression tests that verify existing functionality
* Tags enable selective test execution in CI/CD pipelines or local runs
* Multiple tags can be applied to the same test method
* Use Maven profiles or test runners to filter tests by tags

**Tag Usage**
```java
@Test
@Smoke
void criticalLoginFlow(Quest quest) {
    // Critical path test
}
```

```java
@Test
@Regression
void detailedUserManagement(Quest quest) {
    // Regression coverage test
}
```

```java
@Test
@Smoke
@Regression
void completeCheckoutFlow(Quest quest) {
    // Both smoke and regression
}
```

**Service Ring Management**
* Use `.use(RING_NAME)` to activate a service ring (`RING_OF_API`, `RING_OF_UI`, `RING_OF_DB`, or custom rings)
* Service rings provide fluent API methods for module-specific operations
* Use `.drop()` when changing from one ring context to another in multi-module projects
* Always drop before switching to a different ring to avoid context conflicts
* All interactions with the system under test MUST go through service rings (never through Quest directly)

**Test Completion**
* Always end test methods with .complete() to finalize quest execution
* `.complete()` triggers framework cleanup and reporting

**Validation Requirements**
* Provide at least one validation in each test (assertion, soft assertion, or custom validation lambda)
* Use framework assertion builders for declarative validation
* Different modules use different validation patterns (check module-specific instructions)
* Use soft assertions (`.soft(true)`) when validating multiple related fields
* Use `.validate(() -> {})` for custom validation logic not covered by assertion types
* All validations must use service ring methods, never direct Quest access

### Test Data Management
**@Craft Annotation**
* Use `@Craft(model = DataCreator.Data.MODEL_NAME)` for test data injection instead of building objects in tests
* Define all Craft models in DataCreator enum class
* Add constants as strings in DataCreator enum Data class for usage in `@Craft` annotations
* Craft models are eagerly instantiated before test execution
* Use `Late<@Craft>` for lazy instantiation when model depends on runtime data
* Call `.create()` on `Late<>` instances to materialize the model on-demand

**Static Constants**
* Use dedicated constant classes for values expected across multiple tests
* Organize constants by domain (e.g., TestConstants.Users, TestConstants.Pagination)
* Use `@StaticData` annotation on tests to retrieve constants when needed
* Never hardcode magic strings or numbers in test methods

**Configuration Properties**
* Store environment-specific test data (username, password, URLs) in `test_data-{env}.properties`
* Use `Data.testData()` to retrieve configuration values
* Configuration MUST support the environment model requested by the user

**OWNER Library Integration**
* DataProperties interface MUST use OWNER library annotations for property management
* Use `@Key("property.name")` to map properties to interface methods
* Use `@DefaultValue("value")` for fallback values when properties are missing
* Use `@Config.Sources("classpath:test_data-${env}.properties")` for environment-specific files

**Service Logic & Reusability**
* Use custom service rings to encapsulate complex multi-step workflows
* Create domain-specific ring methods instead of repeating logic in tests
* Define custom rings in the base.Rings class
* Extract common flows into custom ring methods for reusability

### Creating Custom Service Rings
**Custom Ring Pattern:**
* Extend `FluentService<T>` where `T` is your ring class (enables fluent return)
* Accept `SuperQuest` in constructor and store for ring access
* Call `postQuestSetupInitialization()` in constructor for framework hooks
* Return `this` from methods to maintain fluent chain
* Access other rings via `quest.use(RING_CLASS)`

**Custom Ring Template:**
```java
public class CustomService extends FluentService<CustomService> {
    private final Quest quest;

    public CustomService(SuperQuest quest) {
        this.quest = quest;
        postQuestSetupInitialization();  // Framework initialization hook
    }

    // Domain-specific flow
    public CustomService createUserWorkflow(User user) {
        // Use other rings
        quest.use(RING_OF_API)
                .request(AppEndpoints.CREATE_USER, user)
                .drop();

        // Custom logic
        String userId = retrieve(StorageKeysApi.API, AppEndpoints.CREATE_USER, Response.class)
                .jsonPath().getString("id");

        // Store derived data
        QuestHolder.get().getStorage().put(UserKeys.CREATED_USER_ID, userId);

        return this;  // Fluent return
    }

    // Validation wrapper
    public CustomService validateUserExists(String username) {
        validate(() -> {
            Response response = retrieve(StorageKeysApi.API, AppEndpoints.GET_USER, Response.class);
            assertEquals(username, response.jsonPath().getString("username"));
        });
        return this;
    }
}
```

**Registering Custom Rings:**
```java
// In base/Rings.java
public static final Class<CustomService> RING_OF_CUSTOM = CustomService.class;
```

**Using Custom Rings in Tests:**
```java
@Test
void complexWorkflow(Quest quest, @Craft(model = DataCreator.Data.USER) User user) {
    quest.use(RING_OF_CUSTOM)
            .createUserWorkflow(user)
            .validateUserExists(user.getUsername())
            .complete();
}
```

**Benefits:**
* Encapsulates multi-step flows (login → navigate → verify)
* Reusable across tests
* Keeps test bodies high-level and readable
* Testable in isolation

### Test Lifecycle Management
**@Journey - Preconditions**
* Use `@Journey` annotation strictly for precondition actions executed before test runtime
* Define reusable preconditions in `Preconditions` enum class
* Add constants as strings in `Preconditions` enum Data class for usage in `@Journey` annotations
* Use `@JourneyData` to pass @Craft models to journeys
* Use order attribute to control execution sequence when using multiple journeys
* Access journey-created data via retrieve(PRE_ARGUMENTS, key, Class.class)

**@Ripper - Cleanup**
* Use `@Ripper` annotation strictly for cleanup operations after test execution
* Define cleanup targets in `DataCleaner` enum
* Add constants as strings in DataCleaner enum Data class for usage in `@Ripper` annotations
* `@Ripper` executes after test completion (success or failure)
* Ensures test data isolation and prevents database/state pollution

### Late<T> - Deferred Data Creation
**When to Use Late<T>:**
* Model depends on data produced during test execution (API response, intercepted value, DB result)
* Model requires values from earlier quest steps
* Model depends on runtime-determined values unavailable at test start

**Late<T> Lifecycle:**
1. Framework injects `Late<T>` instance at test start (does not call DataCreator yet)
2. Test executes quest steps that produce required data (API calls, DB queries, UI interactions)
3. Required data gets stored in Storage (automatic by framework)
4. Test calls `lateModel.create()` to trigger DataCreator invocation
5. DataCreator reads from Storage via `QuestHolder.get().getStorage()` to build model

**Example - API Response Dependency:**
```java
// DataCreator for late model
public static User derivedUser() {
    SuperQuest quest = QuestHolder.get();
    Response baseResponse = quest.getStorage().sub(StorageKeysApi.API).get(CREATE_BASE_USER, Response.class);
    String baseId = baseResponse.jsonPath().getString("id");
    return User.builder().parentId(baseId).name("derived").build();
}

// Test using Late<T>
@Test
@Craft(model = DataCreator.Data.BASE_USER)
void testLatePattern(Quest quest, @Craft(model = DataCreator.Data.DERIVED_USER) Late<User> derived) {
    quest.use(RING_OF_API)
            .request(CREATE_BASE_USER, baseUser)  // Creates base, stores response
            .drop();

    User derivedUser = derived.create();  // Now safe - base response exists in storage
    quest.use(RING_OF_API).request(CREATE_DERIVED_USER, derivedUser).complete();
}
```

**Late<T> with UI Interception:**
```java
// DataCreator reading intercepted response
public static Order orderFromIntercept() {
    SuperQuest quest = QuestHolder.get();
    String price = DataExtractorFunctions.responseBodyExtraction(
            RequestsInterceptor.INTERCEPT_PRICE.getEndpointSubString(),
            "$.price",
            "for(;;);"
    ).apply(quest.getStorage());
    return Order.builder().price(price).build();
}

// Test with @InterceptRequests + Late<T>
@Test
@InterceptRequests(requestUrlSubStrings = { RequestsInterceptor.Data.INTERCEPT_PRICE })
void testInterceptedOrder(Quest quest, @Craft(model = DataCreator.Data.ORDER) Late<Order> lateOrder) {
    quest.use(RING_OF_UI)
            .button().click(ButtonFields.CALCULATE_PRICE)  // Triggers intercepted request
            .drop();

    Order order = lateOrder.create();  // Price extracted from intercepted response
    quest.use(RING_OF_API).request(CREATE_ORDER, order).complete();
}
```
**Rule:** Use eager `@Craft` when data is self-contained. Use `Late<@Craft>` when `DataCreator` needs Storage access.

### Advanced Features
**Late Initialization**
* Use `Late<@Craft>` for lazy model instantiation
* Call `.create()` to materialize the model at the point of use
* Useful when model creation depends on runtime data or execution order

**Interceptors**
* Use interceptors to modify requests/responses or add cross-cutting concerns
* Define interceptors for logging, authentication token injection, or request modification
* Register interceptors at configuration level or per-test as needed
* Interceptors execute automatically during request/response lifecycle

### Storage and Data Retrieval
**Framework Storage Mechanism**
* Framework automatically stores data from various operations (API responses, UI table data, query results, journey outputs)
* Use retrieve(storageKey, identifier, Class.class) to access stored data
* Storage persists throughout quest execution within a single test
* Different storage keys for different contexts: `StorageKeysApi.API`, `StorageKeysDb.DB`, `PRE_ARGUMENTS`
* **Important:** Quest does not expose the storage mechanism directly via `quest.getStorage()`. Always use `retrieve()` functions with appropriate storage keys.

**Retrieval Patterns**
```java
// Retrieve API response
Response response = retrieve(StorageKeysApi.API, ENDPOINT_NAME, Response.class);
```

```java
// Retrieve journey-created data
User user = retrieve(PRE_ARGUMENTS, DataCreator.USER, User.class);
```

```java
// Retrieve database query results
QueryResponse results = retrieve(StorageKeysDb.DB, QUERY_NAME, QueryResponse.class);
```

### Storage Hierarchy
**Root vs Sub-Storage**
* Root storage: `quest.getStorage()` - shared cross-module data (PRE_ARGUMENTS, ARGUMENTS, STATIC_DATA)
* Sub-storage: `quest.getStorage().sub(MODULE_KEY)` - module-isolated data (API responses, UI elements, DB results)
* Each adapter uses its own sub-storage namespace to prevent key collisions

**Storage Organization:**
| Storage Namespace | Key | Purpose | Access Pattern |
|-------------------|-----|---------|----------------|
| Root | `PRE_ARGUMENTS` | Journey outputs | `retrieve(PRE_ARGUMENTS, journeyKey, Class)` |
| Root | `ARGUMENTS` | @Craft parameters | Automatic by framework |
| `StorageKeysApi.API` | Endpoint enums | API responses | `retrieve(StorageKeysApi.API, ENDPOINT, Response.class)` |
| `StorageKeysUi.UI` | Element enums | UI component values | `retrieve(elementEnum, Class)` via DefaultStorage |
| `StorageKeysDb.DB` | Query enums | Query results | `retrieve(StorageKeysDb.DB, QUERY, QueryResponse.class)` |

**Why Sub-Storage Matters:**
* Prevents key collision when different modules use same enum names
* Enables parallel test execution with isolated per-thread storage
* Framework automatically routes data to correct sub-storage based on ring context


### Accessing Quest from Utility Classes
**QuestHolder Pattern**
* Use `QuestHolder.get()` to access the current Quest from anywhere (DataCreator, DataCleaner, Journey implementations)
* Returns `SuperQuest` instance with full access to Storage, rings, and Quest API
* Thread-safe - each test thread has its own Quest instance
* Enables quest-aware utilities without passing Quest as parameters

**Usage in DataCreator:**
```java
public final class DataCreatorFunctions {
    public static CreateUserDto dynamicUser() {
        SuperQuest quest = QuestHolder.get();
        String baseValue = quest.getStorage().get(UserKeys.BASE_NAME, String.class);
        return CreateUserDto.builder().name(baseValue + "_generated").build();
    }
}
```
**Usage in Journey:**
```java
public class Preconditions implements PreQuestJourney<Seller> {
    @Override
    public Seller journey() {
        SuperQuest quest = QuestHolder.get();
        quest.use(RING_OF_API).request(CREATE_USER);
        return quest.getStorage().get(StorageKeysApi.API, CREATE_USER, Seller.class);
    }
}
```

### Assertion Framework
**Module-Specific Validation:**
* Different modules may use different validation APIs
* Some modules use `Assertion.builder()` pattern (API, DB)
* Some modules use direct validation methods (UI inputs, buttons, alerts, selects, etc.)
* Always consult module-specific instructions for correct validation patterns
* Never assume validation patterns are universal across all modules

**Assertion Builder**
* Use `Assertion.builder()` for declarative, fluent validation
* All assertions require: target, type, and expected value
* Optional: soft assertions, custom messages, JsonPath keys

**Common Assertion Targets**
* `STATUS`: HTTP status codes (API)
* `BODY`: Response body content (API)
* `HEADER`: Response headers (API)
* `QUERY_RESULT`: Database query results (DB)
* `TABLE_VALUES`: Table data values (UI)
* `ROW_VALUES`: Specific row values (UI)

**Common Assertion Types**
* Equality: `IS, NOT, EQUALS_IGNORE_CASE`
* Comparison: `GREATER_THAN, LESS_THAN, BETWEEN`
* String: `CONTAINS, STARTS_WITH, ENDS_WITH, MATCHES_REGEX`
* Collection: `CONTAINS_ALL, CONTAINS_ANY, LENGTH, NOT_EMPTY`
* Null checks: `NOT_NULL, ALL_NOT_NULL`

**Soft Assertions**
* Use `.soft(true)` to collect multiple assertion failures
* All soft assertions in a test execute before reporting failures
* Useful for validating multiple related fields in one pass

### Custom Validation
**Lambda Validation**
* Use `.validate(() -> {})` for custom validation logic not covered by assertion types
* Access to full JUnit assertion library inside lambda
* Useful for complex business logic validation or multi-step checks

```java
.validate(() -> {
    assertEquals(expected, actual, "Custom validation message");
    assertTrue(condition, "Condition not met");
})
```

**Soft Assertion Lambda**
* Use `.validate(softAssertions -> {})` for custom soft assertions
* Collect multiple custom assertion failures

### Test Execution Control
**Sequential Execution**
* Extend BaseQuestSequential when test order matters within a class
* Tests execute in declaration order (top to bottom)
* Use sparingly; prefer independent tests

**Parallel Execution**
* Extend `BaseQuest` for parallel-capable tests
* Tests must be isolated with no shared mutable state
* Better CI/CD performance with parallel execution

### Error Handling
**Framework Error Management**
* Framework catches and reports errors with context
* Quest operations fail fast on critical errors
* Use `.validate()` for expected validation failures
* Never catch framework exceptions in test code

### Logging and Reporting
**Allure Integration**
* Use `@Description("...")` for detailed test descriptions in reports
* Use `@DisplayName("...")` for readable test names
* Framework automatically captures screenshots on UI test failures
* API requests/responses automatically attached to reports
* Database queries automatically logged

### Environment Configuration
**Environment Profiles**
* Support multiple environments via `test_data-{env}.properties`
* Switch environments using Maven profiles or system properties
* Environment-specific URLs, credentials, and timeouts
* Default to dev environment if not specified

### Best Practices
**Framework Abstraction**
* Never attempt to access Quest internals directly
* Always use service ring methods for all operations
* Respect module-specific patterns and contracts
* Read module-specific instructions before implementing module code

**Test Independence**
* Each test must run independently in any order
* Use `@Journey` for setup, `@Ripper` for cleanup
* No shared mutable state between tests
* Generate unique test data per execution

**Readability**
* Use descriptive test method names
* Use `@DisplayName` for business-readable names
* Keep quest chains readable with proper indentation
* Extract complex logic to custom service rings

**Maintainability**
* Define reusable components in enums (endpoints, elements, queries)
* Use constants for magic strings and numbers
* Keep tests focused on single scenarios
* Reuse preconditions via `@Journey`
