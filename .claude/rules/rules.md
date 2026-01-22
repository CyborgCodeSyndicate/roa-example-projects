# **rules.md**

## **Overview**
This document defines the mandatory coding standards and conventions for the ROA test framework project. 
These rules apply to all Java code and are enforced through code reviews, Checkstyle, and CI/CD pipelines.

**Java Version:** Java 17+

### Java Language Rules
**Java Version Compliance**
* Use Java 17+ language features and APIs
* Leverage modern Java features: records, sealed classes, pattern matching, text blocks

**Null Safety**
* Never return null from public methods; use `Optional<T>` for potentially absent values
* Validate method parameters for null using `Objects.requireNonNull()`
* Use `@NonNull` and `@Nullable` annotations where the framework supports them
* Prefer empty collections over null for collection return types

**Exception Handling**
* Catch specific exceptions, not generic Exception or Throwable
* Never use empty catch blocks; log or rethrow exceptions appropriately
* Use try-with-resources for AutoCloseable resources (streams, connections, files)
* Create custom exceptions for domain-specific error scenarios
* Include meaningful error messages with context (what failed, why, relevant data)

**Immutability**
* Prefer immutable objects; make fields final whenever possible
* Use immutable collections via `List.of()`, `Map.of()`, `Set.of()`
* Avoid setters in data models unless mutability is required
* Use Java records for simple immutable data carriers

**Collections**
* Use interface types for declarations (List, Map, Set) not implementations (ArrayList, HashMap)
* Initialize collections with expected capacity when size is known
* Use Streams API for collection processing and transformations
* Avoid modifying collections while iterating; use iterators or streams

**Generics**
* Use generics for type safety; avoid raw types
* Prefer `<T>` over `<?>` when type parameter is used in method body
* Use bounded wildcards (? extends T, ? super T) appropriately
* Avoid unchecked casts; use type-safe alternatives

**Enums**
* Use enums for fixed sets of constants
* Implement interfaces in enums when behavior is shared
* Make enum constructors private
* Use enum methods to encapsulate enum-specific logic

### Code Style & Formatting
**Naming Conventions**
* Classes: PascalCase (e.g., `UserManagementTest`, `DataCreator`)
* Methods: camelCase (e.g., `createUser`, `validateResponse`)
* Variables: camelCase (e.g., `userId`, `expectedValue`)
* Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)
* Packages: lowercase with dots (e.g., `io.cyborgcode.framework.xxx`)
* Test methods: descriptive names with underscores allowed (e.g., `createUser_withValidData_returnsSuccess`)

**Class Structure Order**
* Static constants
* Static variables
* Instance variables (private, protected, public)
* Constructors
* Static methods
* Public methods
* Protected methods
* Private methods
* Nested classes/enums

**Method Ordering**
* Private methods at the bottom
* Protected methods in the middle
* Public methods at the top (after constructors and static methods)
* This ordering reflects visibility: most visible (public) appears first, least visible (private) appears last

**Access Modifiers**
* Use private by default for fields and methods
* Use protected only when inheritance is intended
* Use public only for API exposed to external consumers
* Minimize public surface area of classes

**Import Statements**
* No wildcard imports (import java.util.*); import specific classes
* Organize imports in groups: Java standard library, third-party libraries, project imports
* Remove unused imports
* Use static imports for constants and utility methods

**Method Design**
* Methods should do one thing and do it well (Single Responsibility Principle)
* Limit method length to 30 lines; extract longer logic into helper methods
* Limit method parameters to 4; use parameter objects for more
* Return early to reduce nesting depth
* Use meaningful method names that describe behavior

**Class Design**
* Keep classes focused on a single responsibility
* Limit class size to 300 lines; split larger classes
* Prefer composition over inheritance
* Make classes final unless designed for extension
* Hide implementation details; expose minimal public API

### Security Practices
**Credential Management**
* Never hardcode credentials (usernames, passwords, API keys, tokens) in code
* Store credentials in configuration files outside source control (.properties, .env)
* Use environment variables for sensitive configuration
* Encrypt sensitive data in configuration files
* Use secret management tools for production environments

**Input Validation**
* Validate all external input (API requests, file uploads, user input)
* Use whitelisting over blacklisting for input validation
* Sanitize input before use in queries, commands, or output
* Validate data types, ranges, formats, and lengths
* Reject invalid input with clear error messages

**SQL Injection Prevention**
* Use parameterized queries; never concatenate SQL strings
* Use framework query builders or ORM tools
* Validate and sanitize user input before database operations
* Apply least privilege principle to database user permissions

**Logging Security**
* Never log sensitive data (passwords, tokens, credit cards, PII)
* Sanitize log messages to remove sensitive information
* Use appropriate log levels (DEBUG, INFO, WARN, ERROR)
* Avoid logging full stack traces in production; log error IDs instead

**Dependency Management**
* Keep dependencies up-to-date to patch security vulnerabilities
* Use dependency scanning tools (OWASP Dependency-Check, Snyk)
* Review third-party library licenses and security advisories
* Minimize dependency count; avoid unnecessary libraries
* Pin dependency versions; avoid version ranges in production

**Code Security**
* Avoid reflection unless absolutely necessary; it bypasses type safety
* Validate file paths to prevent directory traversal attacks
* Use secure random number generation (SecureRandom) for security-sensitive operations
* Follow principle of least privilege; grant minimal permissions

### Code Quality Tools
**Checkstyle Enforcement**
* Use the ROA custom Checkstyle configuration for all code
* Enable Checkstyle in IntelliJ IDEA: Settings → Tools → Checkstyle
* Ensure the ROA Checkstyle file is active and selected
* All code must pass Checkstyle validation before merge

**Static Analysis**
* Use static analysis tools: SonarQube, SpotBugs, PMD
* Address critical and high-severity issues before merge
* Configure IDE to highlight code quality issues
* Run static analysis in CI/CD pipeline

**Code Review**
* All code must pass peer review before merge
* Review for correctness, readability, security, performance
* Check test coverage and quality
* Provide constructive feedback

### Code Documentation Rules
**Comments:**
* NEVER add narrative comments explaining step-by-step what code does
* NEVER add "thinking process" comments showing reasoning
* NEVER use comments as substitutes for clear code
* Code should be self-documenting through proper naming and structure

**Forbidden comment patterns:**
```text
// Get user from database
// Click the submit button  
// TODO: refactor this later
// This is needed because...
```

**When comments are acceptable:**
* JavaDoc on public APIs (required)
* Complex algorithms requiring mathematical or domain explanation
* Known workarounds for framework bugs (with ticket reference)
* Regex patterns with explanation of what they match

**Enforcement:**
* Code reviews will reject PRs with unnecessary narrative comments
* Use descriptive names instead of comments
* Extract complex logic to well-named methods

### Build & Compilation
**Maven Build Lifecycle**
* Run `mvn clean install` before committing code to ensure clean build
* Run `mvn compile` to verify code compiles without running tests
* Run `mvn test` to execute all tests locally before push
* Run `mvn checkstyle:check` to validate code style compliance

**Pre-Commit Validation**
* Ensure code compiles successfully: mvn clean compile
* Run all tests and verify they pass: mvn clean test
* Validate Checkstyle compliance: `mvn checkstyle:check`
* Never commit code that doesn't compile or has failing tests

**Dependency Management**
* Run mvn `dependency:tree` to verify dependency conflicts
* Keep dependencies up-to-date; review security advisories
* Remove unused dependencies to keep builds lean

### Git Commit Practices
**Commit Messages**
* Use imperative mood: "Add feature" not "Added feature"
* First line: brief summary (max 50 characters)
* Second line: blank
* Third line onward: detailed explanation if needed
* Reference issue/ticket numbers in commits

**Commit Size**
* Keep commits small and focused
* One logical change per commit
* Avoid mixing refactoring with feature changes
* Commit working code; don't break builds

## ROA Framework Rules

See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for complete Quest DSL and framework documentation.

### Quest DSL Requirements (Mandatory Enforcement)
* All Quest chains MUST end with `.complete()` (compilation requirement)
* Use `.drop()` when switching between service ring contexts (prevents context errors)
* Quest parameter MUST be first parameter in test methods
* Never attempt to access `quest.getDriver()` (method does not exist)
* Never attempt to access `quest.getStorage()` (method does not exist)

### Component Architecture (UI)
See [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) for complete UI component architecture documentation.

**Mandatory Enforcement:**
* ComponentType enums MUST implement `getType()` (not `enumImpl()`)
* UiElement enums MUST implement `enumImpl()` (not `getType()`)
* ALL three layers required (Type Registry, Element Definition, Implementation)
* Component implementations MUST use `findSmartElement()` (not `findElement()`)
* Component implementations MUST use `getDomProperty()` (not `getAttribute()`)
* Component implementations MUST extend `BaseComponent`
* Component implementations MUST have `@ImplementationOfType` annotation
* Constructor MUST accept `SmartWebDriver` and call `super(driver)`

**UI Service Method Naming (Mandatory):**
* When extending `UiServiceFluent`, call correct parent methods:
    - `getAlertField()` not `getAlert()`
    - `getButtonField()` not `getButton()`
    - `getInputField()` not `getInput()`
    - `getSelectField()` not `getSelect()`

### Validation Rules
**Component-Specific Validation (Mandatory)**
* Alert validation MUST use `.alert().validateValue()` (not `Assertion.builder()`)
* API/DB validation MUST use `Assertion.builder()`
* Table validation MUST use `.table().validate(table, Assertion.builder()...)`
* Never import non-existent classes: `UiAlertAssertionTarget`, `UiAlertAssertionTypes`

**Mandatory Test Validation**
* Every test method MUST include at least one validation
* Tests without validation are incomplete and should not be merged

### Data Management Rules
See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for @Craft details.

**Mandatory Enforcement:**
* Use `@Craft` for test data injection (not inline object construction in tests)
* Use `Late<@Craft>` for lazy instantiation when needed
* Never hardcode test data values in test methods
* Store test data in configuration files or DataCreator enums

### Authentication Rules
See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for authentication fundamentals.

**Mandatory Enforcement:**
* Use `@AuthenticateViaApi` or `@AuthenticateViaUi` for authentication
* Never implement manual login steps in test methods when authentication annotations exist
* Store credentials in configuration files (never in code)

### Precondition and Cleanup Rules
See [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) for @Journey and @Ripper fundamentals.

**Mandatory Enforcement:**
* Use `@Journey` for reusable preconditions
* Use `@Ripper` for automatic cleanup
* Never rely on manual cleanup that can be skipped on test failure

### Forbidden Practices

**Universal (All Code)**
* ❌ Never hardcode credentials or sensitive data
* ❌ Never use System.out.println() for logging; use logging framework
* ❌ Never catch Throwable or generic Exception without rethrowing
* ❌ Never use empty catch blocks
* ❌ Never use raw types (e.g., List instead of List<String>)
* ❌ Never ignore compiler warnings; address or suppress with justification
* ❌ Never use reflection when type-safe alternatives exist
* ❌ Never concatenate SQL queries with user input
* ❌ Never commit commented-out code; remove it
* ❌ Never use mutable static variables
* ❌ Never use Thread.sleep() in production code; use proper synchronization
* ❌ Never ignore test failures; fix or disable with reason
* ❌ Never use wildcard imports
* ❌ Never leave comments in the code (e.g., // TODO, // FIXME, // NOTE)

**ROA Framework**
* ❌ Never forget `.complete()` at end of Quest chains (compilation requirement)
* ❌ Never skip `.drop()` when switching service rings (causes context errors)
* ❌ Never try to access `quest.getDriver()` (method does not exist)
* ❌ Never try to access `quest.getStorage()` (method does not exist)

**ROA UI Framework**
* ❌ Never implement `enumImpl()` in ComponentType enums (use `getType()`)
* ❌ Never implement `getType()` in UiElement enums (use `enumImpl()`)
* ❌ Never use `findElement()` in component implementations (use `findSmartElement()`)
* ❌ Never use `getAttribute()` in component implementations (use `getDomProperty()`)
* ❌ Never use `Assertion.builder()` for alert validation (causes compilation error)
* ❌ Never call wrong parent methods (e.g., `getAlert()` instead of `getAlertField()`)
* ❌ Never create only 2 of 3 component layers (causes runtime failure)
* ❌ Never use raw locators in tests (always use element enum constants)
* ❌ Never create tests without validation

**ROA API Framework**
* ❌ Never hardcode API URLs in code (use configuration files)
* ❌ Never parse JSON manually (use framework JsonPath support)

**ROA Database Framework**
* ❌ Never concatenate SQL with parameters (use parameterized queries)
* ❌ Never hardcode database credentials (use configuration files)
