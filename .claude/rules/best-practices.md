# **best-practices.md**

## **Overview**
This document provides **guidelines and best practices** for writing maintainable, efficient, and reliable tests using the ROA framework. These are **recommendations that improve code quality** but are **not strictly enforced**.

**For mandatory standards**, see:
* [rules.md](rules.md) - Enforced coding standards

**For framework fundamentals**, see:
* [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) - Core framework concepts
* [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) - UI architecture details
* [.claude/instructions/api-framework-instructions.md](../instructions/api-framework-instructions.md) - API architecture details
* [.claude/instructions/db-framework-instructions.md](../instructions/db-framework-instructions.md) - DB architecture details

**For code examples**, see:
* [.claude/ui-test-examples.md](../ui-test-examples.md) - Comprehensive UI testing examples

---

## Universal Testing Best Practices

### Test Independence
* Each test must run independently in any order
* Tests should not depend on execution sequence
* No shared mutable state between tests
* Generate unique test data per execution to avoid conflicts

**Why:** Independent tests are reliable, parallelizable, and easier to debug.

### Test Isolation
* Use `@Journey` for setup, `@Ripper` for cleanup
* Clean up created data after test execution
* Use separate database instances or rollback transactions
* Avoid test data pollution across test runs

**Why:** Isolated tests prevent cascading failures and false positives/negatives.

### Environment Agnostic Tests
* Tests should be data-agnostic and not depend on a single environment
* Use configuration files (`test_data-{env}.properties`) for environment-specific data
* Avoid hardcoding environment-specific values (URLs, credentials, IDs)
* Tests should pass in dev, test, staging, and production environments with proper configuration

**Why:** Environment-agnostic tests are portable and reduce maintenance burden.

### Test Execution Time
* Individual tests should complete within **1 minute**
* Long-running tests should be moved to integration or performance test suites
* Optimize slow tests by reducing unnecessary waits or operations
* Use parallel execution for independent tests to improve overall suite speed

**Why:** Fast tests provide quick feedback and encourage frequent test execution.

### Test Structure
* Follow **Arrange-Act-Assert (AAA)** pattern
* One logical assertion per test method
* Use descriptive test names that explain the scenario
* Keep test methods focused on single scenarios

**Why:** Well-structured tests are easier to read, understand, and maintain.

**Example:**
```java
@Test
void createUser_withValidData_returnsSuccess(Quest quest, @Craft(...) User user) {
    // Arrange: Set up preconditions (handled by @Craft)

    // Act: Perform the action
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/users/new")
        .insertion().insertData(user)
        .button().click(ButtonFields.SAVE)

    // Assert: Verify the outcome
        .alert().validateValue(AlertFields.SUCCESS, "User created successfully")
        .complete();
}
```

### Test Readability
* Use `@DisplayName` for business-readable test names
* Keep quest chains readable with proper indentation
* Extract complex logic to custom service rings
* Use meaningful variable names that convey intent
* For UI tests, use `getUiConfig().baseUrl()` instead of hardcoded URLs
* Use element enum constants, never raw locators in tests
* Name element constants descriptively (e.g., `LOGIN_BUTTON` not `BTN1`)

**Why:** Readable tests serve as living documentation and reduce cognitive load.

### Test Method Length
* Keep test methods concise (recommended: **under 50 lines**)
* Extract complex flows into custom service ring methods
* Break down lengthy tests into smaller, focused scenarios
* Long tests are harder to maintain and debug

**Why:** Short, focused tests are easier to understand and less likely to contain hidden bugs.

---

## ROA Framework Best Practices

See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for:
- Quest DSL fundamentals
- Service ring management
- @Craft annotation details
- @Journey and @Ripper patterns
- Validation requirements

### Quest DSL Chaining

**Recommended Practices:**
* Use fluent DSL chaining for readability
* Keep quest chains focused and linear
* Use `.drop()` when changing contexts between rings
* Always end with `.complete()`
* Indent chained methods for visual clarity

**Example:**
```java
quest
    .use(RING_OF_UI)
    .browser().navigate(getUiConfig().baseUrl())
    .input().insert(InputFields.USERNAME, "admin")
    .button().click(ButtonFields.LOGIN)
    .alert().validateValue(AlertFields.SUCCESS, "Logged in")
    .complete();
```

**Why:** Fluent chains improve readability and make test flow obvious.

### Service Ring Usage
* Extract reusable workflows into custom service rings
* Keep business logic out of test methods
* Define custom rings in the `base.Rings` class
* Name ring methods to reflect business actions (e.g., `purchaseCurrency`, `validateOrder`)

**Example:**
```java
// Bad: Repeating logic in tests
@Test
void test1(Quest quest) {
    quest.use(RING_OF_UI)
        .browser().navigate(url)
        .input().insert(field1, value1)
        .input().insert(field2, value2)
        .button().click(submit)
        .complete();
}

@Test
void test2(Quest quest) {
    quest.use(RING_OF_UI)
        .browser().navigate(url)
        .input().insert(field1, value1)
        .input().insert(field2, value2)
        .button().click(submit)
        .complete();
}

// Good: Extract to custom ring
@Test
void test1(Quest quest) {
    quest.use(RING_OF_CUSTOM)
        .fillLoginForm("user1", "pass1")
        .submitForm()
        .complete();
}

@Test
void test2(Quest quest) {
    quest.use(RING_OF_CUSTOM)
        .fillLoginForm("user2", "pass2")
        .submitForm()
        .complete();
}
```

**Why:** Custom rings reduce duplication and improve maintainability.

### Data Management
* Use `@Craft` instead of building objects in tests
* Use `Late<@Craft>` for lazy instantiation when needed
* Store constants in dedicated constant classes
* Use `@StaticData` annotation for constant retrieval

**Why:** Centralized data management reduces duplication and improves maintainability.

### Validation Practices
* Provide at least one validation in each test (mandatory)
* Use soft assertions for multiple related validations
* Use framework assertion builders for declarative validation
* Use `.validate(() -> {})` for custom validation logic

**Example: Soft Assertions**
```java
quest
    .use(RING_OF_UI)
    .browser().navigate(getUiConfig().baseUrl() + "/profile")
    // All validations execute even if one fails
    .input().validateValue(InputFields.USERNAME, "admin", true)
    .input().validateValue(InputFields.EMAIL, "admin@example.com", true)
    .button().validateIsEnabled(ButtonFields.SAVE, true)
    .complete();
```

**Why:** Soft assertions provide comprehensive feedback when multiple validations fail.

### Authentication
* Use `@AuthenticateViaApi` or `@AuthenticateViaUi` for automatic authentication
* Define credentials in dedicated classes
* Avoid manual login steps in test methods
* Consider session caching for test suite performance

**Why:** Automatic authentication reduces boilerplate and improves test consistency.

### Preconditions and Cleanup
* Use `@Journey` for reusable preconditions
* Use `@Ripper` for automatic cleanup
* Combine multiple journeys with order attribute
* Ensure cleanup happens even on test failure

**Why:** Proper setup and cleanup ensure test isolation and prevent data pollution.

---

## ROA UI Framework Best Practices

See [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) for:
- Three-layer component architecture
- Interface contracts (ComponentType vs UiElement)
- Smart API requirements
- Lifecycle hooks
- Component validation patterns
- Table operations

### Component Architecture

**Always Create All Three Layers:**
When adding new UI components, ensure you create:
1. **Component Type Registry** (`ui/types/*FieldTypes.java`)
2. **Element Definition** (`ui/elements/*Fields.java`)
3. **Component Implementation** (`ui/components/<type>/*Impl.java`)

**Why:** Missing any layer causes runtime component resolution failures.

**Checklist:**
- [ ] Created ComponentType enum with `getType()` method
- [ ] Created UiElement enum with `enumImpl()` method
- [ ] Created component implementation with `@ImplementationOfType`
- [ ] Component implementation uses Smart API (`findSmartElement`, `getDomProperty`)
- [ ] Tested component in actual test

### Element Definition Best Practices

**Stable Locators:**
* Use stable locators: `By.id()`, `By.cssSelector()` with stable attributes
* Avoid brittle selectors: dynamic classes, translated text, deep XPath
* Keep element enums declarative (locators + types only)
* No interaction logic, waits, or assertions in element definitions

**Example:**
```java
// Good: Stable locators
LOGIN_BUTTON(By.id("login-btn"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE)
USERNAME_FIELD(By.cssSelector("input[data-testid='username']"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE)

// Avoid: Brittle locators
LOGIN_BUTTON(By.xpath("//div[3]/div[2]/button[1]"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE)
USERNAME_FIELD(By.linkText("Username"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE)
```

**Why:** Stable locators reduce test maintenance when UI changes.

### Lifecycle Hooks

**When to Add Hooks:**
* Add `beforeAction` hooks when element needs explicit wait before interaction
* Add `afterAction` hooks when you need to wait for state change after interaction
* Start without hooks; add only when tests fail due to timing issues

**Example:**
```java
// Element requires wait before click (dynamic loading)
SUBMIT_BUTTON(By.id("submit"), 
              ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
              SharedUi.WAIT_TO_BE_CLICKABLE)

// Element disappears after click (delete action)
DELETE_BUTTON(By.id("delete"),
              ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
              SharedUi.WAIT_TO_BE_CLICKABLE,
              ButtonFields::waitForRemoval)
```

**Why:** Hooks improve test stability without cluttering test code.

### UI Component Validation

**Use Component-Specific Methods:**
* Different UI components use different validation patterns
* Alert: `.alert().validateValue()` (specific method)
* Button: `.button().validateIsEnabled()` (specific method)
* Input: `.input().validateValue()` (specific method)
* API/DB: `Assertion.builder()` (generic builder)
* Table: `.table().validate(table, Assertion.builder()...)` (hybrid)

**Example:**
```java
// Correct: Component-specific validation
.button().validateIsEnabled(ButtonFields.SUBMIT)
.alert().validateValue(AlertFields.SUCCESS, "Done")
.input().validateValue(InputFields.USERNAME, "admin")

// Wrong: Using Assertion.builder() for alerts
.alert().validate(AlertFields.SUCCESS, Assertion.builder()...)
```

**Why:** Component-specific methods provide better type safety and clearer intent.

### Soft Assertions for UI

**Use Soft Assertions for Multiple Related Checks:**
```java
quest
    .use(RING_OF_UI)
    .browser().navigate(getUiConfig().baseUrl() + "/form")
    // All validations execute before reporting failures
    .input().validateValue(InputFields.FIELD1, "expected1", true)
    .input().validateValue(InputFields.FIELD2, "expected2", true)
    .button().validateIsEnabled(ButtonFields.SUBMIT, true)
    .complete();
```

**Why:** Soft assertions provide complete failure picture instead of failing on first error.

---

## Performance Best Practices

### String Operations
* Use `StringBuilder` for string concatenation in loops
* Use text blocks for multi-line strings
* Cache compiled regex patterns

**Example:**
```java
// Inefficient: String concatenation in loop
String result = "";
for (String item : items) {
    result += item + ", ";
}

// Efficient: StringBuilder
StringBuilder result = new StringBuilder();
for (String item : items) {
    result.append(item).append(", ");
}
```

**Why:** StringBuilder avoids creating multiple intermediate String objects.

### Resource Management
* Close resources explicitly using try-with-resources
* Avoid resource leaks (connections, streams, files)
* Dispose of large objects when no longer needed

**Example:**
```java
// Try-with-resources ensures cleanup
try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    return reader.lines().collect(Collectors.toList());
}
```

**Why:** Proper resource management prevents memory leaks and connection exhaustion.

### Stream Usage
* Use parallel streams only for CPU-intensive operations with large datasets
* Avoid side effects in stream operations
* Prefer streams over loops for declarative data processing

**Example:**
```java
// Declarative data processing with streams
List<String> activeUsers = users.stream()
    .filter(User::isActive)
    .map(User::getName)
    .collect(Collectors.toList());
```

**Why:** Streams provide cleaner, more maintainable code for data transformations.

### Object Creation
* Reuse immutable objects; avoid unnecessary object creation
* Use static factory methods instead of constructors when appropriate
* Avoid premature optimization; measure before optimizing

**Why:** Reducing object creation reduces garbage collection overhead.

---

## Test Data Best Practices

### Data Builders and Factories
* Use test data builders or factories (e.g., `@Craft` models)
* Avoid hardcoding test data; use constants or configuration
* Generate unique test data to avoid conflicts
* Use realistic but minimal test data

**Example:**
```java
// Good: Using @Craft for test data
@Test
void createOrder(@Craft(model = DataCreator.Data.ORDER) Order order) {
    quest.use(RING_OF_CUSTOM)
        .createOrder(order)
        .complete();
}

// Avoid: Hardcoded test data
@Test
void createOrder(Quest quest) {
    Order order = new Order();
    order.setName("Test Order");
    order.setAmount(100.0);
}
```

**Why:** Data builders provide consistent, maintainable test data.

### Data Cleanup
* Clean up created data using `@Ripper`
* Ensure cleanup happens even on test failure
* Use database transactions with rollback when appropriate
* Prevent test data accumulation in test environments

**Why:** Proper cleanup prevents test data pollution and ensures test isolation.

---

## Assertion Best Practices

### Meaningful Assertions
* Provide meaningful assertion messages
* Assert on specific values, not just existence
* Avoid brittle assertions (e.g., exact timestamp matching)
* Use appropriate assertion types for the validation

**Example:**
```java
// Good: Specific assertion with message
.validate(() -> {
    assertEquals("John Doe", user.getName(), "User name should match expected value");
})

// Avoid: Generic assertion without context
.validate(() -> {
    assertTrue(user.getName() != null);
})
```

**Why:** Meaningful assertions provide clear failure messages for faster debugging.

### Soft Assertions
* Use soft assertions for related validations
* Group related assertions together
* All soft assertions execute before reporting failures

**Why:** Soft assertions provide comprehensive failure information.

---

## CI/CD Best Practices

### Build Quality
* All builds must pass in CI/CD pipeline before merge
* CI must run: compile, test, Checkstyle, static analysis
* Failed builds must be fixed immediately
* Monitor build times; optimize if builds exceed acceptable duration

**Why:** Continuous integration ensures code quality and prevents broken builds.

### Test Execution in CI
* Run smoke tests on every commit
* Run full regression suite nightly or on pull requests
* Use test tags (`@Smoke`, `@Regression`) for selective execution
* Parallelize test execution for faster feedback

**Example:**
```bash
# Run only smoke tests
mvn test -Dgroups="smoke"

# Run regression tests
mvn test -Dgroups="regression"

# Run all tests in parallel
mvn test -DparallelMode=classes -DthreadCount=4
```

**Why:** Strategic test execution provides fast feedback without sacrificing coverage.

### Quality Gates

**Pre-Merge Requirements:**
* Code must compile successfully
* All affected tests must pass
* All new tests must pass
* Checkstyle validation must pass
* Static analysis critical issues must be resolved
* Code review approval required

**Why:** Quality gates prevent problematic code from entering main branch.

**Continuous Monitoring:**
* Monitor test execution times
* Track test flakiness and stability
* Review test coverage trends
* Address failing or skipped tests promptly

**Why:** Continuous monitoring identifies quality degradation early.

---

## Maintainability Best Practices

### Code Organization
* Group related tests in the same class
* Organize test classes by feature or domain
* Keep project structure consistent
* Use meaningful package names

**Example:**
```
src/test/java/com/example/
├── user/
│   ├── UserRegistrationTest.java
│   ├── UserLoginTest.java
│   └── UserProfileTest.java
├── order/
│   ├── OrderCreationTest.java
│   └── OrderCancellationTest.java
```

**Why:** Organized code is easier to navigate and maintain.

### Reusability
* Extract common flows into service rings
* Define reusable preconditions via `@Journey`
* Create shared constants and utilities
* Avoid code duplication across tests

**Why:** Reusable components reduce maintenance burden and improve consistency.

### Documentation
* Document complex business logic
* Explain non-obvious test scenarios
* Keep documentation up-to-date
* Use `@Description` for detailed test context

**Example:**
```java
@Test
@Description("Verifies that premium users can access exclusive features " +
             "after subscription renewal. Tests both immediate access and " +
             "delayed synchronization scenarios.")
void premiumUser_accessExclusiveFeatures() {
    // Test implementation
}
```

**Why:** Documentation helps team members understand test intent and business rules.

---

## Debugging Best Practices

### Test Failures
* Investigate failures immediately; don't ignore them
* Check Allure reports for screenshots, logs, and request/response data
* Reproduce failures locally before fixing
* Add logging for complex scenarios

**Why:** Quick failure investigation prevents test suite degradation.

### Flaky Tests
* Identify and fix flaky tests promptly
* Add proper waits and synchronization (lifecycle hooks)
* Avoid hardcoded delays
* Use framework lifecycle hooks for stability

**Example:**
```java
// Avoid: Hardcoded delay
Thread.sleep(2000);
button.click();

// Better: Use lifecycle hooks
SUBMIT_BUTTON(By.id("submit"),
              ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
              SharedUi.WAIT_TO_BE_CLICKABLE)
```

**Why:** Flaky tests erode confidence in the test suite and waste time.

### Logging Best Practices
* Use appropriate log levels (DEBUG for detailed info, INFO for important events)
* Log meaningful information (what action, what data, what result)
* Avoid logging sensitive data (passwords, tokens, PII)
* Use structured logging for easier parsing

**Why:** Good logging accelerates debugging and issue investigation.

---

## Common Anti-Patterns to Avoid

### Test Anti-Patterns

**Test Interdependence:**
```java
// Bad: Test2 depends on Test1
@Test
@Order(1)
void test1_createUser() { /* creates user */ }

@Test
@Order(2)
void test2_updateUser() { /* assumes user exists */ }

// Good: Independent tests
@Test
@Journey(Preconditions.Data.CREATE_USER)
void test_updateUser() { /* user created in journey */ }
```

**Hardcoded Test Data:**
```java
// Bad
.input().insert(InputFields.USERNAME, "testuser123")
.input().insert(InputFields.EMAIL, "test@example.com")

// Good
@Test
void test(@Craft(model = DataCreator.Data.USER) User user) {
    .input().insert(InputFields.USERNAME, user.getUsername())
    .input().insert(InputFields.EMAIL, user.getEmail())
}
```

**Unclear Test Names:**
```java
// Bad
@Test
void test1() { /* unclear purpose */ }

@Test
void testButton() { /* which button? */ }

// Good
@Test
void loginButton_whenClicked_redirectsToHomepage() { }

@Test
void submitOrder_withInvalidData_showsValidationError() { }
```

### Code Anti-Patterns

**Excessive Duplication:**
```java
// Bad: Same logic repeated
quest.use(RING_OF_UI)
    .browser().navigate(url)
    .input().insert(field1, value1)
    .input().insert(field2, value2)
    .button().click(submit)

// Good: Extract to custom ring
quest.use(RING_OF_CUSTOM)
    .performCommonWorkflow(value1, value2)
```

**God Test:**
```java
// Bad: Testing too much
@Test
void testEntireApplication() {
    // 200 lines testing everything
}

// Good: Focused tests
@Test
void registration_withValidData_createsUser() { }

@Test
void login_withValidCredentials_redirectsToHomepage() { }
```

---

## Summary: Key Recommendations

### Test Design
* Keep tests independent, isolated, and focused
* Use descriptive names and clear structure (AAA pattern)
* Target 1-minute execution time per test
* Extract complex logic to custom rings

### Framework Usage
* Use Quest DSL chaining for readability
* Use `@Craft` for test data
* Use `@Journey` for preconditions
* Use `@Ripper` for cleanup
* Use component-specific validation methods

### UI Testing
* Always create all three component layers
* Use stable locators
* Add lifecycle hooks only when needed
* Use Smart API in component implementations

### Code Quality
* Follow Checkstyle rules consistently
* Write self-documenting code
* Use meaningful names
* Keep methods short and focused

### Performance
* Optimize slow tests
* Use parallel execution
* Clean up test data properly
* Monitor and fix flaky tests

### CI/CD
* Run smoke tests on every commit
* Run full regression suite regularly
* Fix failing builds immediately
* Monitor test suite health

---

## References

**Mandatory Standards:**
* [rules.md](rules.md) - Enforced coding standards

**Framework Fundamentals:**
* [core-framework-instructions.md](../instructions/core-framework-instructions.md) - Core concepts
* [ui-framework-instructions.md](../instructions/ui-framework-instructions.md) - UI architecture

**Examples:**
* [ui-test-examples.md](../ui-test-examples.md) - Comprehensive code examples

---

**Remember:** Best practices are recommendations to improve code quality. They are not strictly enforced but following them will result in more maintainable, reliable, and efficient test suites.
