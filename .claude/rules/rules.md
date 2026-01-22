# **rules.md**

## **Overview**
This document defines the **mandatory coding standards and conventions** for the ROA test framework project. 
These rules apply to all Java code and are **enforced through code reviews, Checkstyle, and CI/CD pipelines**.

**For detailed framework concepts and patterns**, see:
* [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) - Core framework fundamentals
* [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) - UI architecture details

**For recommended practices (not enforced)**, see:
* [.claude/rules/best-practices.md](best-practices.md) - Recommended patterns and optimizations

**This file contains only MANDATORY, ENFORCED rules.**

---

**Java Version:** Java 17+

---

## Java Language Rules

### Java Version Compliance
* Use Java 17+ language features and APIs
* Leverage modern Java features: records, sealed classes, pattern matching, text blocks
* Avoid deprecated APIs; use modern alternatives

### Null Safety
* Never return null from public methods; use `Optional<T>` for potentially absent values
* Validate method parameters for null using `Objects.requireNonNull()`
* Use `@NonNull` and `@Nullable` annotations where the framework supports them
* Prefer empty collections over null for collection return types

### Exception Handling
* Catch specific exceptions, not generic Exception or Throwable
* Never use empty catch blocks; log or rethrow exceptions appropriately
* Use try-with-resources for AutoCloseable resources (streams, connections, files)
* Create custom exceptions for domain-specific error scenarios
* Include meaningful error messages with context (what failed, why, relevant data)

### Immutability
* Prefer immutable objects; make fields final whenever possible
* Use immutable collections via `List.of()`, `Map.of()`, `Set.of()`
* Avoid setters in data models unless mutability is required
* Use Java records for simple immutable data carriers

### Collections
* Use interface types for declarations (List, Map, Set) not implementations (ArrayList, HashMap)
* Initialize collections with expected capacity when size is known
* Use Streams API for collection processing and transformations
* Avoid modifying collections while iterating; use iterators or streams

### Generics
* Use generics for type safety; avoid raw types
* Prefer `<T>` over `<?>` when type parameter is used in method body
* Use bounded wildcards (? extends T, ? super T) appropriately
* Avoid unchecked casts; use type-safe alternatives

### Enums
* Use enums for fixed sets of constants
* Implement interfaces in enums when behavior is shared
* Make enum constructors private
* Use enum methods to encapsulate enum-specific logic

---

## Code Style & Formatting

### Naming Conventions
* **Classes:** PascalCase (e.g., `UserManagementTest`, `DataCreator`)
* **Methods:** camelCase (e.g., `createUser`, `validateResponse`)
* **Variables:** camelCase (e.g., `userId`, `expectedValue`)
* **Constants:** UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)
* **Packages:** lowercase with dots (e.g., `io.cyborgcode.framework.xxx`)
* **Test methods:** descriptive names with underscores allowed (e.g., `createUser_withValidData_returnsSuccess`)

### Class Structure Order
1. Static constants
2. Static variables
3. Instance variables (private, protected, public)
4. Constructors
5. Static methods
6. Public methods
7. Protected methods
8. Private methods
9. Nested classes/enums

### Method Ordering
* **Private methods** at the bottom (least visible)
* **Protected methods** in the middle
* **Public methods** at the top after constructors (most visible)
* This ordering reflects visibility: most visible appears first

### Access Modifiers
* Use **private** by default for fields and methods
* Use **protected** only when inheritance is intended
* Use **public** only for API exposed to external consumers
* Minimize public surface area of classes

### Import Statements
* **No wildcard imports** (import java.util.*); import specific classes
* Organize imports in groups: Java standard library, third-party libraries, project imports
* Remove unused imports
* Use static imports for constants and utility methods

### Method Design
* Methods should do one thing and do it well (Single Responsibility Principle)
* Limit method length to **30 lines**; extract longer logic into helper methods
* Limit method parameters to **4**; use parameter objects for more
* Return early to reduce nesting depth
* Use meaningful method names that describe behavior

### Class Design
* Keep classes focused on a single responsibility
* Limit class size to **300 lines**; split larger classes
* Prefer composition over inheritance
* Make classes final unless designed for extension
* Hide implementation details; expose minimal public API

---

## Security Practices

### Credential Management
* **Never hardcode credentials** (usernames, passwords, API keys, tokens) in code
* Store credentials in configuration files outside source control (.properties, .env)
* Use environment variables for sensitive configuration
* Encrypt sensitive data in configuration files
* Use secret management tools for production environments

### Input Validation
* Validate all external input (API requests, file uploads, user input)
* Use whitelisting over blacklisting for input validation
* Sanitize input before use in queries, commands, or output
* Validate data types, ranges, formats, and lengths
* Reject invalid input with clear error messages

### SQL Injection Prevention
* **Use parameterized queries**; never concatenate SQL strings
* Use framework query builders or ORM tools
* Validate and sanitize user input before database operations
* Apply least privilege principle to database user permissions

### Logging Security
* **Never log sensitive data** (passwords, tokens, credit cards, PII)
* Sanitize log messages to remove sensitive information
* Use appropriate log levels (DEBUG, INFO, WARN, ERROR)
* Avoid logging full stack traces in production; log error IDs instead

### Dependency Management
* Keep dependencies up-to-date to patch security vulnerabilities
* Use dependency scanning tools (OWASP Dependency-Check, Snyk)
* Review third-party library licenses and security advisories
* Minimize dependency count; avoid unnecessary libraries
* Pin dependency versions; avoid version ranges in production

### Code Security
* Avoid reflection unless absolutely necessary; it bypasses type safety
* Validate file paths to prevent directory traversal attacks
* Use secure random number generation (SecureRandom) for security-sensitive operations
* Follow principle of least privilege; grant minimal permissions

---

## Code Quality Tools

### Checkstyle Enforcement
* Use the ROA custom Checkstyle configuration for all code
* Enable Checkstyle in IntelliJ IDEA: Settings → Tools → Checkstyle
* Ensure the ROA Checkstyle file is active and selected
* **All code must pass Checkstyle validation before merge**

### Static Analysis
* Use static analysis tools: SonarQube, SpotBugs, PMD
* Address critical and high-severity issues before merge
* Configure IDE to highlight code quality issues
* Run static analysis in CI/CD pipeline

### Code Review
* All code must pass peer review before merge
* Review for correctness, readability, security, performance
* Check test coverage and quality
* Provide constructive feedback

---

## Code Documentation Rules

### Comments Policy
* **NEVER add narrative comments** explaining step-by-step what code does
* **NEVER add "thinking process" comments** showing reasoning
* **NEVER use comments as substitutes** for clear code
* Code should be **self-documenting** through proper naming and structure

### Forbidden Comment Patterns
```java
// ❌ Get user from database
// ❌ Click the submit button  
// ❌ TODO: refactor this later
// ❌ This is needed because...
// ❌ Temporary fix for bug
// ❌ NOTE: This might not work in all cases
```

### When Comments Are Acceptable
* **JavaDoc on public APIs** (required for framework classes)
* **Complex algorithms** requiring mathematical or domain explanation
* **Known workarounds** for framework bugs (with ticket reference)
* **Regex patterns** with explanation of what they match

**Example of acceptable comment:**
```java
// Regex matches ISO 8601 date format: YYYY-MM-DD
Pattern datePattern = Pattern.compile("\d{4}-\d{2}-\d{2}");

// Workaround for Selenium bug #12345: explicit wait required
// TODO: Remove when upgrading to Selenium 5.0+ (Ticket: PROJ-456)
```

### Enforcement
* Code reviews will reject PRs with unnecessary narrative comments
* Use descriptive names instead of comments
* Extract complex logic to well-named methods

---

## Build & Compilation

### Maven Build Lifecycle
* Run `mvn clean install` before committing code to ensure clean build
* Run `mvn compile` to verify code compiles without running tests
* Run `mvn test` to execute all tests locally before push
* Run `mvn checkstyle:check` to validate code style compliance

### Pre-Commit Validation
* Ensure code compiles successfully: `mvn clean compile`
* Run all tests and verify they pass: `mvn clean test`
* Validate Checkstyle compliance: `mvn checkstyle:check`
* **Never commit code that doesn't compile or has failing tests**

### Dependency Management
* Run `mvn dependency:tree` to verify dependency conflicts
* Keep dependencies up-to-date; review security advisories
* Remove unused dependencies to keep builds lean

---

## Git Commit Practices

### Commit Messages
* Use imperative mood: "Add feature" not "Added feature"
* First line: brief summary (max 50 characters)
* Second line: blank
* Third line onward: detailed explanation if needed
* Reference issue/ticket numbers in commits

**Example:**
```
Add user authentication feature

- Implement JWT token generation
- Add login endpoint with validation
- Update security configuration

Closes #123
```

### Commit Size
* Keep commits small and focused
* One logical change per commit
* Avoid mixing refactoring with feature changes
* Commit working code; don't break builds

---

## ROA Framework Rules

### Core Framework Documentation
See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for complete documentation on:
- Quest DSL fundamentals
- Service ring management
- @Craft annotation
- @Journey and @Ripper
- Validation patterns
- Storage and retrieval

### Quest DSL Requirements (MANDATORY)

**Quest Completion:**
* All Quest chains **MUST end with `.complete()`** (compilation requirement)
* Missing `.complete()` causes compilation error

**Service Ring Management:**
* Use `.use(RING_NAME)` to activate a service ring
* Use `.drop()` when switching between service rings in multi-module projects
* Missing `.drop()` causes context conflicts

**Quest Parameter:**
* Quest parameter **MUST be first parameter** in test methods
* Never instantiate Quest manually; let framework inject it

**Quest Abstraction:**
* Quest does NOT expose `quest.getDriver()` (method does not exist)
* Quest does NOT expose `quest.getStorage()` (method does not exist)
* Quest does NOT expose internal framework details
* Always use service ring methods for all operations

**Example:**
```java
@Test
void exampleTest(Quest quest) {  // ✅ Quest as first parameter
    quest
        .use(RING_OF_UI)          // ✅ Activate ring
        .button().click(ButtonFields.LOGIN)
        .drop()                    // ✅ Drop before switching rings
        .use(RING_OF_API)
        .request(Endpoints.GET_USER)
        .complete();               // ✅ MUST end with complete()
}
```

### Test Data Management Rules (MANDATORY)

**@Craft Annotation:**
* Use `@Craft(model = DataCreator.Data.MODEL_NAME)` for test data injection
* Never build test data objects inline in test methods
* Define all Craft models in `DataCreator` enum class
* Use `Late<@Craft>` for lazy instantiation when needed

**Configuration:**
* Store environment-specific data in `test_data-{env}.properties`
* Never hardcode test data values in test methods
* Use `Data.testData()` to retrieve configuration values

### Validation Rules (MANDATORY)

**Every Test Must Have Validation:**
* Every test method **MUST include at least one validation**
* Tests without validation are incomplete and will be rejected in code review
* Use framework assertion builders or component-specific validation methods

**Module-Specific Validation Patterns:**
* **UI components** (button, input, alert, checkbox, etc.) → Use component-specific validation methods
* **API** → Use `Assertion.builder()`
* **Database** → Use `Assertion.builder()`
* **UI Tables** → Use `Assertion.builder()` with table-specific targets

See module-specific instructions for detailed validation patterns.

### Preconditions and Cleanup Rules (MANDATORY)

**@Journey (Preconditions):**
* Use `@Journey` annotation for precondition actions
* Define reusable preconditions in `Preconditions` enum
* Use `@JourneyData` to pass @Craft models to journeys

**@Ripper (Cleanup):**
* Use `@Ripper` annotation for automatic cleanup
* Define cleanup targets in `DataCleaner` enum
* Ensures test isolation and prevents data pollution

See [core-framework-instructions.md](../instructions/core-framework-instructions.md) for detailed usage.

---

## ROA UI Framework Rules

### UI Framework Documentation
See [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) for complete documentation on:
- Three-layer component architecture
- Interface contracts (ComponentType vs UiElement)
- Smart API requirements
- Element definitions and lifecycle hooks
- Component validation patterns
- Table operations

### Three-Layer Architecture (MANDATORY)

**All Three Layers Required:**
Every UI component **MUST have all three layers** or runtime component resolution fails:

1. **Component Type Registry** (`ui/types/*FieldTypes.java`) - Technology identifiers
2. **Element Definition** (`ui/elements/*Fields.java`) - Locators and lifecycle hooks
3. **Component Implementation** (`ui/components/<type>/*Impl.java`) - Interaction logic

**Missing any layer causes runtime failure.**

### Interface Contracts (MANDATORY)

**ComponentType Interface:**
* ComponentType enums **MUST implement `getType()`** (NOT `enumImpl()`)
* Used in: `ui/types/*FieldTypes.java`

```java
// ✅ CORRECT
public enum ButtonFieldTypes implements ButtonComponentType {
    BOOTSTRAP_BUTTON_TYPE;

    @Override
    public Enum<?> getType() { return this; }  // ✅ Correct method name
}

// ❌ WRONG - causes compilation error
@Override
public Enum<?> enumImpl() { return this; }  // ❌ Wrong method name
```

**UiElement Interface:**
* UiElement enums **MUST implement `enumImpl()`** (NOT `getType()`)
* Used in: `ui/elements/*Fields.java`

```java
// ✅ CORRECT
public enum ButtonFields implements ButtonUiElement {
    LOGIN_BUTTON(By.id("login"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE);

    @Override
    public Enum<?> enumImpl() { return this; }  // ✅ Correct method name
}

// ❌ WRONG - causes compilation error
@Override
public Enum<?> getType() { return this; }  // ❌ Wrong method name
```

**Rule:** Types use `getType()`, Elements use `enumImpl()`. **Mixing them causes compilation errors.**

### Smart API Requirements (MANDATORY)

**Component implementations MUST use Smart API:**

| ❌ Standard Selenium API | ✅ Smart API (REQUIRED) |
|--------------------------|--------------------------|
| `driver.findElement()` | `driver.findSmartElement()` |
| `element.findElement()` | `element.findSmartElement()` |
| `element.getAttribute()` | `element.getDomProperty()` |

**Component Implementation Requirements:**
* Extend `BaseComponent`
* Implement the component interface (Button, Input, Select, etc.)
* Add `@ImplementationOfType(TypeClass.Data.TYPE_NAME)` annotation
* Constructor MUST accept `SmartWebDriver` and call `super(driver)`

**Example:**
```java
@ImplementationOfType(ButtonFieldTypes.Data.BOOTSTRAP_BUTTON_TYPE)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

    public ButtonBootstrapImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    public void click(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);  // ✅ Smart API
        button.click();
    }
}
```

### UI Component Validation (MANDATORY)

**Component-Specific Validation Methods:**
* UI components (alert, button, input, checkbox, select, etc.) **MUST use component-specific validation methods**
* **DO NOT use `Assertion.builder()` for UI component validation**
* `Assertion.builder()` is only for API/DB/Table validation

**Examples:**
```java
// ✅ CORRECT: Component-specific validation
.alert().validateValue(AlertFields.ERROR, "Invalid input")
.button().validateIsEnabled(ButtonFields.SUBMIT)
.input().validateValue(InputFields.USERNAME, "admin")
.checkbox().validateIsSelected(CheckboxFields.TERMS)

// ❌ WRONG: Using Assertion.builder() for alerts (causes compilation error)
.alert().validate(
    AlertFields.ERROR,
    Assertion.builder()  // ❌ Won't compile for alerts
        .target(ALERT_TEXT)
        .type(IS)
        .expected("Invalid input")
        .build()
)
```

**Classes that don't exist (DO NOT IMPORT):**
* ❌ `io.cyborgcode.roa.ui.validator.UiAlertAssertionTarget` - doesn't exist
* ❌ `io.cyborgcode.roa.ui.validator.UiAlertAssertionTypes` - doesn't exist

### UI Service Method Naming (MANDATORY)

When extending `UiServiceFluent`, call correct parent methods:

| Component | ✅ Correct Parent Method | ❌ Wrong Method |
|-----------|-------------------------|-----------------|
| Alert | `getAlertField()` | `getAlert()` |
| Button | `getButtonField()` | `getButton()` |
| Input | `getInputField()` | `getInput()` |
| Select | `getSelectField()` | `getSelect()` |

**Wrong method names cause compilation errors.**

---

## ROA API Framework Rules

### API Endpoint Definition (MANDATORY)
* Define endpoints in dedicated enums implementing `Endpoint<T>` interface
* Use descriptive enum constant names that reflect the API operation
* Keep endpoint definitions declarative (method, path, headers only)
* Avoid business logic in endpoint definitions

### API Validation (MANDATORY)
* Use `Assertion.builder()` for API response validation
* Validate status codes, response body fields, and headers
* Use JsonPath for nested response navigation

---

## ROA Database Framework Rules

### Database Query Definition (MANDATORY)
* Define parameterized queries with clear parameter names (e.g., `{userId}`)
* Use descriptive query constant names
* **Never concatenate SQL queries with user input** (use parameterized queries)

### Database Validation (MANDATORY)
* Use `Assertion.builder()` for database validation
* Use `QUERY_RESULT` target for database validation
* Use JsonPath for result navigation

---

## Forbidden Practices

### Universal (All Code)
* ❌ Never hardcode credentials, API keys, or tokens in code
* ❌ Never use `System.out.println()` for logging (use logging framework)
* ❌ Never catch `Throwable` or generic `Exception` without rethrowing
* ❌ Never use empty catch blocks
* ❌ Never use raw types (e.g., `List` instead of `List<String>`)
* ❌ Never ignore compiler warnings without addressing or justifying
* ❌ Never use reflection when type-safe alternatives exist
* ❌ Never concatenate SQL queries with user input (SQL injection risk)
* ❌ Never commit commented-out code (remove it)
* ❌ Never use mutable static variables
* ❌ Never use `Thread.sleep()` in production code (use proper synchronization)
* ❌ Never ignore test failures (fix or disable with reason)
* ❌ Never use wildcard imports (e.g., `import java.util.*`)
* ❌ Never leave TODO/FIXME comments in committed code
* ❌ Never commit code with failing tests or compilation errors

### ROA Framework
* ❌ Never forget `.complete()` at end of Quest chains (compilation error)
* ❌ Never skip `.drop()` when switching service rings (causes context errors)
* ❌ Never try to access `quest.getDriver()` (method does not exist)
* ❌ Never try to access `quest.getStorage()` (method does not exist)
* ❌ Never create tests without validation (mandatory requirement)

### ROA UI Framework
* ❌ Never implement `enumImpl()` in ComponentType enums (use `getType()`)
* ❌ Never implement `getType()` in UiElement enums (use `enumImpl()`)
* ❌ Never use `findElement()` in component implementations (use `findSmartElement()`)
* ❌ Never use `getAttribute()` in component implementations (use `getDomProperty()`)
* ❌ Never use `Assertion.builder()` for UI component validation (use component methods)
* ❌ Never call wrong parent methods (e.g., `getAlert()` instead of `getAlertField()`)
* ❌ Never create only 2 of 3 component layers (causes runtime failure)
* ❌ Never use raw locators in tests (always use element enum constants)
* ❌ Never import non-existent classes (`UiAlertAssertionTarget`, `UiAlertAssertionTypes`)

### ROA API Framework
* ❌ Never hardcode API URLs in code (use configuration files)
* ❌ Never parse JSON manually (use framework JsonPath support)

### ROA Database Framework
* ❌ Never concatenate SQL with parameters (use parameterized queries)
* ❌ Never hardcode database credentials (use configuration files)

---

## Enforcement

### Pre-Merge Requirements
All of the following **MUST pass before code can be merged**:

1. ✅ Code compiles successfully (`mvn clean compile`)
2. ✅ All tests pass (`mvn clean test`)
3. ✅ Checkstyle validation passes (`mvn checkstyle:check`)
4. ✅ Static analysis critical issues resolved
5. ✅ Code review approval received
6. ✅ No hardcoded credentials or sensitive data
7. ✅ No forbidden practices present
8. ✅ All tests include validation

### CI/CD Pipeline
* Build fails if any pre-merge requirement is not met
* Failed builds must be fixed immediately
* Do not bypass CI/CD checks

### Code Review Checklist
Reviewers must verify:
- [ ] Code follows naming conventions
- [ ] No wildcard imports
- [ ] No hardcoded credentials or sensitive data
- [ ] Proper exception handling
- [ ] No empty catch blocks
- [ ] Checkstyle passes
- [ ] Tests include validation
- [ ] Quest chains end with `.complete()`
- [ ] UI components use Smart API
- [ ] Interface contracts correct (getType() vs enumImpl())
- [ ] No forbidden practices present

---

## Summary

**This file defines MANDATORY, ENFORCED rules only.**

For detailed framework concepts, patterns, and architecture:
* [core-framework-instructions.md](../instructions/core-framework-instructions.md)
* [ui-framework-instructions.md](../instructions/ui-framework-instructions.md)

For recommended practices (not enforced):
* [best-practices.md](best-practices.md)

**All rules in this file are enforced through:**
* Checkstyle
* Code reviews
* CI/CD pipelines
* Static analysis tools

**Non-compliance will result in build failures and merge rejection.**
