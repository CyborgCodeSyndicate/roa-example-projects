# **best-practices.md**

## **Overview**
This document provides guidelines and best practices for writing maintainable, efficient, and reliable 
tests using the ROA framework. These are recommendations that improve code quality but are not strictly enforced.

For mandatory standards, see [rules.md](rules.md).
For framework fundamentals, see [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md).
For UI architecture, see [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md).

### Universal Testing Best Practices
**Test Independence**
* Each test must run independently in any order
* Tests should not depend on execution sequence
* No shared mutable state between tests
* Generate unique test data per execution to avoid conflicts

**Test Isolation**
* Use `@Journey` for setup, `@Ripper` for cleanup
* Clean up created data after test execution
* Use separate database instances or rollback transactions
* Avoid test data pollution across test runs

**Environment Agnostic Tests**
* Tests should be data-agnostic and not depend on a single environment
* Use configuration files (`test_data-{env}.properties`) for environment-specific data
* Avoid hardcoding environment-specific values (URLs, credentials, IDs)
* Tests should pass in dev, test, staging, and production environments with proper configuration

**Test Execution Time**
* Individual tests should complete within 1 minute
* Long-running tests should be moved to integration or performance test suites
* Optimize slow tests by reducing unnecessary waits or operations
* Use parallel execution for independent tests to improve overall suite speed

**Test Structure**
* Follow Arrange-Act-Assert (AAA) pattern
* One logical assertion per test method
* Use descriptive test names that explain the scenario
* Keep test methods focused on single scenarios

**Test Readability**
* Use @DisplayName for business-readable test names
* Keep quest chains readable with proper indentation
* Extract complex logic to custom service rings
* Use meaningful variable names that convey intent
* For UI tests don't provide the web url directly instead call `getUiConfig().baseUrl()` method
* Use element enum constants, never raw locators in tests
* Name element constants descriptively (e.g., `LOGIN_BUTTON` not `BTN1`)

**Test Method Length**
* Keep test methods concise (recommended: under 50 lines)
* Extract complex flows into custom service ring methods
* Break down lengthy tests into smaller, focused scenarios
* Long tests are harder to maintain and debug

### ROA Framework Best Practices
See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for Quest DSL, @Craft, @Journey, @Ripper, and validation fundamentals.

**Recommended Practices:**
* Use fluent DSL chaining for readability
* Keep quest chains focused and linear
* Use `.drop()` when changing contexts between rings
* Always end with `.complete()`

**Service Ring Usage**
* Extract reusable workflows into custom service rings
* Keep business logic out of test methods
* Define custom rings in the base.Rings class
* Name ring methods to reflect business actions (e.g., `purchaseCurrency`, `validateOrder`)

**Data Management**
* Use `@Craft` instead of building objects in tests
* Use `Late<@Craft>` for lazy instantiation when needed
* Store constants in dedicated constant classes
* Use `@StaticData` annotation for constant retrieval

**Validation Practices**
* Provide at least one validation in each test
* Use soft assertions (`.soft(true)`) for multiple related validations
* Use framework assertion builders for declarative validation
* Use `.validate(() -> {})` for custom validation logic

**Element and Endpoint Definition**
* Use enum-based definitions for UI elements, API endpoints, and DB queries
* Keep definitions centralized and organized by domain
* Use descriptive enum constant names
* Add lifecycle hooks only when needed (don't over-engineer)

**Authentication**
* Use `@AuthenticateViaApi` or `@AuthenticateViaUi` for automatic authentication
* Define credentials in dedicated classes
* Avoid manual login steps in test methods

**Preconditions and Cleanup**
* Use `@Journey` for reusable preconditions
* Use `@Ripper` for automatic cleanup
* Combine multiple journeys with order attribute
* Ensure cleanup happens even on test failure

### Performance Best Practices
**String Operations**
* Use StringBuilder for string concatenation in loops
* Use text blocks ("""...""") for multi-line strings
* Cache compiled regex patterns

**Resource Management**
* Close resources explicitly using try-with-resources
* Avoid resource leaks (connections, streams, files)
* Dispose of large objects when no longer needed

**Stream Usage**
* Use parallel streams only for CPU-intensive operations with large datasets
* Avoid side effects in stream operations
* Prefer streams over loops for declarative data processing

**Object Creation**
* Reuse immutable objects; avoid unnecessary object creation
* Use static factory methods instead of constructors when appropriate
* Avoid premature optimization; measure before optimizing

### Test Data Best Practices
**Data Builders and Factories**
* Use test data builders or factories (e.g., `@Craft` models)
* Avoid hardcoding test data; use constants or configuration
* Generate unique test data to avoid conflicts
* Use realistic but minimal test data

**Data Cleanup**
* Clean up created data using @Ripper
* Ensure cleanup happens even on test failure
* Use database transactions with rollback when appropriate
* Prevent test data accumulation in test environments

### Assertion Best Practices
**Meaningful Assertions**
* Provide meaningful assertion messages
* Assert on specific values, not just existence
* Avoid brittle assertions (e.g., exact timestamp matching)
* Use appropriate assertion types for the validation

**Soft Assertions**
* Use soft assertions for related validations
* Group related assertions together
* All soft assertions execute before reporting failures

### CI/CD Best Practices
**Build Quality**
* All builds must pass in CI/CD pipeline before merge
* CI must run: compile, test, Checkstyle, static analysis
* Failed builds must be fixed immediately
* Monitor build times; optimize if builds exceed acceptable duration

**Test Execution in CI**
* Run smoke tests on every commit
* Run full regression suite nightly or on pull requests
* Use test tags (@Smoke, @Regression) for selective execution
* Parallelize test execution for faster feedback

**Quality Gates**
**Pre-Merge Requirements**
* Code must compile successfully
* All affected tests must pass
* All new tests must pass
* Checkstyle validation must pass
* Static analysis critical issues must be resolved
* Code review approval required
* AI-assisted code quality profile must pass (if configured)

**Continuous Monitoring**
* Monitor test execution times
* Track test flakiness and stability
* Review test coverage trends
* Address failing or skipped tests promptly

### Maintainability Best Practices
**Code Organization**
* Group related tests in the same class
* Organize test classes by feature or domain
* Keep project structure consistent
* Use meaningful package names

**Reusability**
* Extract common flows into service rings
* Define reusable preconditions via `@Journey`
* Create shared constants and utilities
* Avoid code duplication across tests

**Documentation**
* Document complex business logic
* Explain non-obvious test scenarios
* Keep documentation up-to-date
* Use `@Description` for detailed test context

**Debugging Best Practices**
**Test Failures**
* Investigate failures immediately; don't ignore them
* Check Allure reports for screenshots, logs, and request/response data
* Reproduce failures locally before fixing
* Add logging for complex scenarios

**Flaky Tests**
* Identify and fix flaky tests promptly
* Add proper waits and synchronization
* Avoid hardcoded delays
* Use framework lifecycle hooks for stability

### ROA UI Framework Best Practices
See [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) for complete UI component architecture.

**Component Architecture**
* Always create all three layers when adding new UI components:
    1. Component Type Registry (`ui/types/*FieldTypes.java`)
    2. Element Definition (`ui/elements/*Fields.java`)
    3. Component Implementation (`ui/components/<type>/*Impl.java`)
* Missing any layer causes runtime component resolution failures
* Follow the three-layer architecture checklist before considering implementation complete

**Interface Method Compliance**
* Use `getType()` in ComponentType enums (e.g., `ButtonFieldTypes`)
* Use `enumImpl()` in UiElement enums (e.g., `ButtonFields`)
* Do not mix these methods up - they serve different interfaces
* Verify method names match interface contracts before compilation

**Component Implementation Standards**
* Always use `SmartWebDriver` and `SmartWebElement` APIs in component implementations
* Use `findSmartElement()` instead of `findElement()` to return correct types
* Use `getDomProperty()` instead of deprecated `getAttribute()`
* Extend `BaseComponent` and implement the component interface
* Add `@ImplementationOfType` annotation linking to component type
* Constructor must accept `SmartWebDriver` and call `super(driver)`

**UI Service Method Naming**
* When extending `UiServiceFluent`, use correct parent method names:
    - `getAlertField()` not `getAlert()`
    - `getButtonField()` not `getButton()`
    - `getInputField()` not `getInput()`
* Check parent class API documentation for correct method names
* Create wrapper methods with shorter names (e.g., `alert()` calls `getAlertField()`)

**UI Test Validation**
* Every UI test must include at least one validation
* Use component-specific validation methods:
    - Alerts: `.alert().validateValue(element, expectedText)`
    - Element state: `.button().validateIsEnabled(element)`
    - Navigation: `.browser().navigate(protectedUrl)` validates access
* Do not use `Assertion.builder()` for alert validation (use direct methods)
* Do not try to access `quest.getDriver()` for validation
* Use framework services for all validation logic

**Quest Abstraction**
* Never access WebDriver directly from Quest object
* Quest does not expose `getDriver()` or `getStorage()` methods
* Use framework services instead: `.browser()`, `.button()`, `.input()`, etc.
* Maintain high-level abstraction in test code

**Validation Pattern Selection**
* Different UI components use different validation patterns:
    - Alert: `.alert().validateValue()` (specific method)
    - API/DB: `Assertion.builder()` (generic builder)
    - Table: `.table().validate(table, Assertion.builder()...)`
* Verify the correct validation pattern for each component type
* Check component's ServiceFluent class for available validation methods

**Element Definition Best Practices**
* Use stable locators: `By.id()`, `By.cssSelector()` with stable attributes
* Avoid brittle selectors: dynamic classes, translated text, deep XPath
* Keep element enums declarative (locators + types only)
* No interaction logic, waits, or assertions in element definitions
* Use nested `Data` class only when string constants are needed for annotations

**Soft Assertions for UI**
* Use soft assertions for multiple related UI validations:
    - `.alert().validateValue(element, text, true)` for soft alert validation
    - `.checkbox().validateIsVisible(element, true)` for soft visibility checks
    - `.button().validateIsEnabled(element, true)` for soft state checks
* Soft assertions allow test to continue and report all failures at once
* Useful for form validation with multiple error messages

### ROA API Framework Best Practices

**Endpoint Definition**
* Define endpoints in dedicated enums implementing `Endpoint<T>` interface
* Use descriptive enum constant names that reflect the API operation
* Override `defaultConfiguration()` only when default behavior needs customization
* Keep endpoint definitions declarative (method, path, headers only)
* Avoid business logic in endpoint definitions

**API Validation**
* Use `Assertion.builder()` for API response validation
* Validate status codes, response body fields, and headers
* Use JsonPath for nested response navigation
* Store API responses automatically in `StorageKeysApi.API`
* Retrieve stored responses using `retrieve(StorageKeysApi.API, ENDPOINT_NAME, Response.class)`

**Request/Response DTOs**
* Use Java records for immutable response DTOs
* Use classes with setters for mutable request DTOs
* Define DTOs with proper field mappings for serialization
* Keep DTOs focused on single API endpoint contract

**API Test Structure**
* Use `.request()` for simple API calls without immediate validation
* Use `.requestAndValidate()` for API calls with inline validation
* Chain multiple API calls in logical sequence
* Use soft assertions for multiple related API validations

### ROA Database Framework Best Practices

**Database Type Definition**
* Define database types in enum implementing `DbType<T>` interface
* Keep database configuration in `Databases` enum
* Use descriptive names for database connections
* Define credentials separately in configuration files

**Query Definition**
* Define parameterized queries with clear parameter names (e.g., `{userId}`)
* Keep queries focused and single-purpose
* Use descriptive query constant names
* Store complex queries in dedicated query classes

**Database Hooks**
* Use `@DbHook` for database initialization and cleanup
* Define setup logic in `beforeAll()` or `beforeEach()`
* Define cleanup logic in `afterEach()` or `afterAll()`
* Keep database hooks minimal and focused

**Database Validation**
* Use `QUERY_RESULT` target for database validation
* Use JsonPath for result navigation
* Validate specific field values, not just row counts
* Use database validation as precondition checks when appropriate

### Forbidden Practices

**Universal (All Modules)**
* ❌ Never hardcode credentials, API keys, or tokens in test code
* ❌ Never use `System.out.println()` for logging (use logging framework)
* ❌ Never commit code with failing tests or compilation errors
* ❌ Never ignore compiler warnings without addressing or justifying
* ❌ Never use wildcard imports (e.g., `import java.util.*`)
* ❌ Never use empty catch blocks
* ❌ Never use raw types (e.g., `List` instead of `List<String>`)
* ❌ Never share mutable state between tests
* ❌ Never use `Thread.sleep()` in production code (use proper synchronization)
* ❌ Never concatenate SQL queries with user input (use parameterized queries)
* ❌ Never commit commented-out code (remove it)

**ROA Framework**
* ❌ Never forget `.complete()` at the end of Quest chains
* ❌ Never skip `.drop()` when switching between service rings
* ❌ Never build test data objects in test methods (use `@Craft`)
* ❌ Never skip validation in tests (every test needs at least one assertion)
* ❌ Never hardcode test data (use `@Craft` or configuration files)
* ❌ Never create tests longer than 50 lines without extracting to service rings
* ❌ Never create environment-dependent tests

**ROA UI Framework**
* ❌ Never try to access `quest.getDriver()` (not exposed by Quest)
* ❌ Never use `findElement()` in component implementations (use `findSmartElement()`)
* ❌ Never use `getAttribute()` in component implementations (use `getDomProperty()`)
* ❌ Never use `Assertion.builder()` for alert validation (use `.validateValue()`)
* ❌ Never implement `enumImpl()` in ComponentType enums (use `getType()`)
* ❌ Never implement `getType()` in UiElement enums (use `enumImpl()`)
* ❌ Never forget to create component implementations (three-layer architecture required)
* ❌ Never call wrong parent methods in UI service (e.g., `getAlert()` instead of `getAlertField()`)
* ❌ Never create UI tests without validation
* ❌ Never mix validation patterns between different component types
* ❌ Never use raw locators in tests (always use element enum constants)
* ❌ Never add business logic to element definitions or component implementations

**ROA API Framework**
* ❌ Never hardcode API URLs (use configuration files)
* ❌ Never parse JSON responses manually (use framework JsonPath support)
* ❌ Never ignore HTTP status codes in validation
* ❌ Never create duplicate endpoint definitions

**ROA Database Framework**
* ❌ Never concatenate SQL queries with parameters (use parameterized queries)
* ❌ Never leave database connections open (framework manages them)
* ❌ Never hardcode database credentials (use configuration files)
* ❌ Never use production database for testing

### Code Review Checklist

**Before Committing**
- [ ] Code compiles successfully: `mvn clean compile`
- [ ] All tests pass: `mvn clean test`
- [ ] Checkstyle validation passes: `mvn checkstyle:check`
- [ ] No hardcoded credentials or environment-specific data
- [ ] All Quest chains end with `.complete()`
- [ ] All tests include at least one validation
- [ ] Test names are descriptive and use `@DisplayName`
- [ ] No compiler warnings (or all justified and documented)
- [ ] No wildcard imports
- [ ] Test data uses `@Craft` models or configuration files

**UI-Specific Checks**
- [ ] All three component layers created (type, element, implementation)
- [ ] ComponentType enums use `getType()` method
- [ ] UiElement enums use `enumImpl()` method
- [ ] Component implementations use `findSmartElement()` not `findElement()`
- [ ] Component implementations use `getDomProperty()` not `getAttribute()`
- [ ] UI service methods call correct parent methods (e.g., `getAlertField()`)
- [ ] Alert validation uses `.validateValue()` not `Assertion.builder()`
- [ ] No attempts to access `quest.getDriver()`

**API-Specific Checks**
- [ ] Endpoints defined in dedicated enum classes
- [ ] Response validation uses `Assertion.builder()`
- [ ] Proper DTOs defined for request/response bodies
- [ ] No hardcoded API URLs

**DB-Specific Checks**
- [ ] Queries are parameterized (no string concatenation)
- [ ] Database hooks properly defined with `@DbHook`
- [ ] Database validation uses `QUERY_RESULT` target
- [ ] No hardcoded database credentials

**Code Quality**
- [ ] Methods under 50 lines (extract complex logic to service rings)
- [ ] Test methods under 50 lines
- [ ] No code duplication (extracted to reusable components)
- [ ] Meaningful variable and method names
- [ ] Proper Javadoc for public classes and methods

### Troubleshooting Common Issues

**Compilation Errors**

**"cannot find symbol: method getDriver()"**
* Cause: Trying to access `quest.getDriver()`
* Solution: Use framework services like `.browser().navigate()` instead

**"incompatible types: WebElement cannot be converted to SmartWebElement"**
* Cause: Using `findElement()` instead of `findSmartElement()` in component implementations
* Solution: Replace all `findElement()` with `findSmartElement()`

**"method does not override or implement a method from a supertype"**
* Cause: Using wrong method name in ComponentType (`enumImpl()` instead of `getType()`)
* Solution: ComponentType uses `getType()`, UiElement uses `enumImpl()`

**"cannot find symbol: class UiAlertAssertionTarget"**
* Cause: Trying to use `Assertion.builder()` for alert validation
* Solution: Use `.alert().validateValue(element, expectedText)` instead

**"cannot find symbol: method getAlert()"**
* Cause: Calling wrong parent method in UI service class
* Solution: Use `getAlertField()` not `getAlert()`

**Runtime Errors**

**Component resolution fails at runtime**
* Cause: Missing component implementation (only created type registry and element definition)
* Solution: Create all three layers: type, element, and implementation

**"No such element" exceptions in UI tests**
* Cause: Element not present when interaction attempted
* Solution: Add lifecycle hooks to element definition for proper synchronization

**Authentication failures**
* Cause: Incorrect credentials or authentication flow
* Solution: Verify credentials in configuration files and authentication class implementation

**Test Flakiness**

**Intermittent failures in UI tests**
* Cause: Timing issues, race conditions, or improper waits
* Solution: Add proper lifecycle hooks or use framework wait mechanisms

**Tests fail in CI but pass locally**
* Cause: Environment differences, parallel execution conflicts, or data pollution
* Solution: Ensure test independence, use unique test data, verify environment configuration
