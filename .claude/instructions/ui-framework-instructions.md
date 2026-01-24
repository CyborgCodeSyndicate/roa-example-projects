# **ui-framework-instructions.md**

## About This Document
**This is the single source of truth for UI-specific patterns and architecture in the ROA framework.**

For core framework concepts (Quest DSL, @Craft, @Journey, @Ripper, service rings, validation fundamentals), see [core-framework-instructions.md](core-framework-instructions.md).

This file contains **UI-specific architecture, component system, and UI testing patterns only**.

---

## Prerequisites
Before implementing UI components or tests, read [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals and public API
- Service ring management (.use(), .drop(), .complete())
- Test data management (@Craft annotation and Late<> initialization)
- Preconditions (@Journey) and cleanup (@Ripper)
- General validation requirements
- Storage and data retrieval patterns

**This file assumes you understand those core concepts.**

---

## Interface Contracts

### ComponentType vs UiElement
**CRITICAL:** Two different interfaces require two different method names. **Mixing them causes compilation errors.**

**ComponentType** (ui/types/*FieldTypes):
```java
@Override
public Enum<?> getType() { return this; }  // ✅ MUST use getType()
```

**UiElement** (ui/elements/*Fields):
```java
@Override
public Enum<?> enumImpl() { return this; }  // ✅ MUST use enumImpl()
```

**Rule:** Types use `getType()`, Elements use `enumImpl()`.

**Common Compilation Error:**
```java
// ❌ WRONG - causes "method does not override or implement a method from a supertype"
public enum ButtonFieldTypes implements ButtonComponentType {
    BOOTSTRAP_BUTTON_TYPE;

    @Override
    public Enum<?> enumImpl() { return this; }  // ❌ Wrong method name
}

// ✅ CORRECT
public enum ButtonFieldTypes implements ButtonComponentType {
    BOOTSTRAP_BUTTON_TYPE;

    @Override
    public Enum<?> getType() { return this; }  // ✅ Correct for ComponentType
}
```

---

## UI Test Class Structure

### Required Annotations
**Class Level:**
* `@UI` annotation on each UI test class (required)
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
        .use(RING_OF_UI)           // Activate UI service ring
        .button().click(ButtonFields.LOGIN_BUTTON)
        .complete();                // MUST end with .complete()
}
```

**Multi-Module Tests:**
```java
@Test
@UI
@API
void crossModuleTest(Quest quest) {
    quest
        .use(RING_OF_UI)
        .button().click(ButtonFields.CREATE_ORDER)
        .drop()                     // ✅ Use .drop() when switching rings
        .use(RING_OF_API)
        .request(OrderEndpoints.GET_ORDER)
        .complete();
}
```

See [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals
- Service ring management
- .complete() requirement
- Validation requirements

---

## Three-Layer Component Architecture

**CRITICAL REQUIREMENT:** Every UI component MUST have all three layers or runtime component resolution fails.

### The Three Layers

#### 1. Component Type Registry (ui/types/*FieldTypes.java)
Defines technology identifiers for UI frameworks.

```java
public enum ButtonFieldTypes implements ButtonComponentType {
    BOOTSTRAP_BUTTON_TYPE,
    VA_BUTTON_TYPE,
    MATERIAL_BUTTON_TYPE;

    // Nested Data class for annotation references
    public static final class Data {
        public static final String BOOTSTRAP_BUTTON_TYPE = "BOOTSTRAP_BUTTON_TYPE";
        public static final String VA_BUTTON_TYPE = "VA_BUTTON_TYPE";
        public static final String MATERIAL_BUTTON_TYPE = "MATERIAL_BUTTON_TYPE";
        private Data() {}
    }

    @Override
    public Enum<?> getType() {  // ✅ MUST use getType() for ComponentType
        return this;
    }
}
```

#### 2. Element Definition (ui/elements/*Fields.java)
Declares specific UI elements with locators and types.

```java
public enum ButtonFields implements ButtonUiElement {
    // Minimal declaration - locator + type
    SIGN_IN_BUTTON(By.id("sign-in"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),

    // With before hook for synchronization
    SUBMIT_BUTTON(By.cssSelector("button[type='submit']"), 
                  ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
                  SharedUi.WAIT_TO_BE_CLICKABLE),

    // With before and after hooks
    DELETE_BUTTON(By.id("delete"), 
                  ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
                  SharedUi.WAIT_TO_BE_CLICKABLE, 
                  ButtonFields::waitForRemoval);

    private final By locator;
    private final ButtonComponentType type;
    private final ContextConsumer<By> beforeAction;
    private final ContextConsumer<By> afterAction;

    // Multiple constructors for flexibility
    ButtonFields(By locator, ButtonComponentType type) {
        this(locator, type, null, null);
    }

    ButtonFields(By locator, ButtonComponentType type, ContextConsumer<By> beforeAction) {
        this(locator, type, beforeAction, null);
    }

    ButtonFields(By locator, ButtonComponentType type, 
                 ContextConsumer<By> beforeAction, 
                 ContextConsumer<By> afterAction) {
        this.locator = locator;
        this.type = type;
        this.beforeAction = beforeAction;
        this.afterAction = afterAction;
    }

    @Override
    public By locator() { return locator; }

    @Override
    public ButtonComponentType componentType() { return type; }

    @Override
    public ContextConsumer<By> beforeAction() { return beforeAction; }

    @Override
    public ContextConsumer<By> afterAction() { return afterAction; }

    @Override
    public Enum<?> enumImpl() { return this; }  // ✅ MUST use enumImpl() for UiElement

    // Nested Data class for annotation references
    public static final class Data {
        public static final String SIGN_IN_BUTTON = "SIGN_IN_BUTTON";
        public static final String SUBMIT_BUTTON = "SUBMIT_BUTTON";
        public static final String DELETE_BUTTON = "DELETE_BUTTON";
        private Data() {}
    }

    // Custom after hook example
    private static void waitForRemoval(By locator) {
        // Wait logic for element removal
    }
}
```

#### 3. Component Implementation (ui/components/<type>/*Impl.java)
Provides actual interaction logic for specific UI frameworks.

```java
@ImplementationOfType(ButtonFieldTypes.Data.BOOTSTRAP_BUTTON_TYPE)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

    public ButtonBootstrapImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    public void click(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);  // ✅ Use Smart API
        button.click();
    }

    @Override
    public void doubleClick(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);
        Actions actions = new Actions(driver.getWebDriver());
        actions.doubleClick(button.getWebElement()).perform();
    }

    @Override
    public boolean isEnabled(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);
        return button.isEnabled();
    }

    @Override
    public boolean isVisible(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);
        return button.isDisplayed();
    }
}
```

### Component Implementation Requirements

**Smart API (MANDATORY):**

| ❌ Standard Selenium API | ✅ Smart API                    | Why                                      |
|--------------------------|----------------------------------|------------------------------------------|
| `driver.findElement()`   | `driver.findSmartElement()`      | Returns SmartWebElement                  |
| `element.findElement()`  | `element.findSmartElement()`     | Nested search with Smart API             |
| `element.getAttribute()` | `element.getDomProperty()`       | Modern DOM property access (not deprecated) |

**Required Implementation Details:**
* Extend `BaseComponent`
* Implement the component interface (Button, Input, Select, etc.)
* Add `@ImplementationOfType(ComponentTypeClass.Data.TYPE_NAME)` annotation
* Constructor MUST accept `SmartWebDriver` and call `super(driver)`
* Use `findSmartElement()` instead of `findElement()`
* Use `getDomProperty()` instead of `getAttribute()`

### Component Resolution Flow

```
Test Code:
quest.use(RING_OF_UI)
     .button().click(ButtonFields.SIGN_IN_BUTTON)

↓

Framework Resolution:
1. Reads ButtonFields.SIGN_IN_BUTTON.componentType()
   → Returns ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE

2. Finds implementation with @ImplementationOfType(BOOTSTRAP_BUTTON_TYPE)
   → Resolves to ButtonBootstrapImpl

3. Executes lifecycle:
   → beforeAction() if defined (wait for clickability)
   → ButtonBootstrapImpl.click(locator)
   → afterAction() if defined (wait for state change)
```

**Why All Three Layers Are Required:**
* Missing Type Registry → No technology identifier
* Missing Element Definition → No locators to interact with
* Missing Implementation → Runtime component resolution fails

---

## UI Locator Strategies

### Locator Best Practices
**Preferred Locators (Stable):**
* `By.id("unique-id")` - Most stable; use when elements have unique IDs
* `By.cssSelector("[data-testid='element']")` - Stable with test-specific attributes
* `By.tagName("vaadin-button")` - Good for technology-specific components
* `By.cssSelector("button.primary-action")` - Stable with semantic classes

**Avoid (Brittle):**
* `By.xpath("//div[3]/span[2]/button")` - Position-dependent, breaks easily
* `By.className("btn")` - Too generic, not unique
* `By.linkText("Click Here")` - Breaks with translated text

### Locator Type Reference

| Locator Type | Use Case | Stability | Performance |
|--------------|----------|-----------|-------------|
| `By.id()` | Elements with unique IDs | ⭐⭐⭐⭐⭐ | ⚡⚡⚡⚡⚡ |
| `By.cssSelector()` | Most web elements | ⭐⭐⭐⭐ | ⚡⚡⚡⚡ |
| `By.tagName()` | Technology-specific tags | ⭐⭐⭐⭐ | ⚡⚡⚡⚡ |
| `By.name()` | Form elements with name | ⭐⭐⭐ | ⚡⚡⚡⚡ |
| `By.xpath()` | Complex DOM relationships | ⭐⭐ | ⚡⚡ |
| `By.className()` | Shared styling classes | ⭐⭐ | ⚡⚡⚡ |
| `By.linkText()` | Link text (avoid i18n) | ⭐ | ⚡⚡⚡ |

### Dynamic Locators
When element properties change at runtime, build locators dynamically:

```java
public By getDynamicButton(String buttonId) {
    return By.cssSelector("button[data-id='" + buttonId + "']");
}

// Or use String.format for readability
public By getDynamicRow(int rowIndex) {
    return By.cssSelector(String.format("tr[data-row='%d']", rowIndex));
}
```

---

## Element Definition Patterns

### Basic Element Structure
```java
public enum InputFields implements InputUiElement {
    USERNAME(By.id("username"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    PASSWORD(By.id("password"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    EMAIL(By.cssSelector("input[type='email']"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE);

    private final By locator;
    private final InputComponentType type;

    InputFields(By locator, InputComponentType type) {
        this.locator = locator;
        this.type = type;
    }

    @Override
    public By locator() { return locator; }

    @Override
    public InputComponentType componentType() { return type; }

    @Override
    public Enum<?> enumImpl() { return this; }  // ✅ Required for UiElement
}
```

### Elements with Lifecycle Hooks
```java
public enum ButtonFields implements ButtonUiElement {
    // Before hook: Wait for element to be clickable
    SUBMIT(By.id("submit"), 
           ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
           SharedUi.WAIT_TO_BE_CLICKABLE),

    // Before + After hooks: Wait before click, wait for removal after
    DELETE(By.id("delete"), 
           ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
           SharedUi.WAIT_TO_BE_CLICKABLE,
           ButtonFields::waitForRemoval);

    private final By locator;
    private final ButtonComponentType type;
    private final ContextConsumer<By> beforeAction;
    private final ContextConsumer<By> afterAction;

    ButtonFields(By locator, ButtonComponentType type, 
                 ContextConsumer<By> beforeAction) {
        this(locator, type, beforeAction, null);
    }

    ButtonFields(By locator, ButtonComponentType type,
                 ContextConsumer<By> beforeAction,
                 ContextConsumer<By> afterAction) {
        this.locator = locator;
        this.type = type;
        this.beforeAction = beforeAction;
        this.afterAction = afterAction;
    }

    @Override
    public By locator() { return locator; }

    @Override
    public ButtonComponentType componentType() { return type; }

    @Override
    public ContextConsumer<By> beforeAction() { return beforeAction; }

    @Override
    public ContextConsumer<By> afterAction() { return afterAction; }

    @Override
    public Enum<?> enumImpl() { return this; }

    private static void waitForRemoval(By locator) {
        // Custom wait logic for element removal
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
```

### Nested Data Class Pattern
Use nested `Data` class for annotation-based references:

```java
public enum AlertFields implements AlertUiElement {
    SUCCESS_MESSAGE(By.cssSelector(".alert-success"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE),
    ERROR_MESSAGE(By.cssSelector(".alert-danger"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE);

    // ... enum implementation ...

    // Nested Data class for string constants
    public static final class Data {
        public static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";
        public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
        private Data() {}  // Prevent instantiation
    }
}
```

**Usage in annotations:**
```java
@Journey(AlertFields.Data.SUCCESS_MESSAGE)
void precondition(Quest quest) {
    // ...
}
```

---

## Lifecycle Hooks

### Before Hooks
Execute pre-interaction logic (waits, state checks, overlay dismissal).

**Common Use Cases:**
* Wait for element to be clickable
* Wait for element to be visible
* Dismiss overlays or loading indicators
* Ensure element is in the correct state

**Example: Shared Wait Strategies**
```java
public enum SharedUi {
    WAIT_TO_BE_CLICKABLE,
    WAIT_FOR_VISIBILITY,
    WAIT_FOR_LOADING,
    DISMISS_OVERLAY;

    public ContextConsumer<By> asConsumer(By locator) {
        return switch (this) {
            case WAIT_TO_BE_CLICKABLE -> SharedUiFunctions::waitForClickable;
            case WAIT_FOR_VISIBILITY -> SharedUiFunctions::waitForVisible;
            case WAIT_FOR_LOADING -> SharedUiFunctions::waitForLoadingComplete;
            case DISMISS_OVERLAY -> SharedUiFunctions::dismissOverlay;
        };
    }
}
```

### After Hooks
Execute post-interaction logic (wait for state changes, page transitions).

**Common Use Cases:**
* Wait for element removal (delete actions)
* Wait for page navigation
* Wait for loading indicators to appear/disappear
* Verify state change after action

**Example: Custom After Hook**
```java
private static void waitForRemoval(By locator) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
}

// Usage in element definition
DELETE_BUTTON(By.id("delete"), 
              ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE,
              SharedUi.WAIT_TO_BE_CLICKABLE,
              ButtonFields::waitForRemoval)
```

### When to Use Hooks

**✅ Add Hooks When:**
* Element requires explicit wait for interaction
* Dealing with asynchronous UI updates
* Page has loading overlays or spinners
* Element state changes after interaction

**❌ Don't Add Hooks When:**
* Element is immediately available
* Framework's implicit waits are sufficient
* Over-engineering stable interactions

**Best Practice:** Start without hooks. Add only when tests fail due to timing issues.

---

## UI Component System

### Available Components

| Component | Interface | Entry Method | Common Operations |
|-----------|-----------|--------------|-------------------|
| Browser | `Browser` | `.browser()` | navigate, back, forward, refresh, switchTab |
| Button | `Button` | `.button()` | click, doubleClick, isEnabled, isVisible |
| Input | `Input` | `.input()` | insert, clear, getValue |
| Select | `Select` | `.select()` | selectOption, getSelectedOption |
| Radio | `Radio` | `.radio()` | select, isSelected |
| Checkbox | `Checkbox` | `.checkbox()` | check, uncheck, isChecked |
| Alert | `Alert` | `.alert()` | validateValue, accept, dismiss, getText |
| Link | `Link` | `.link()` | click, getHref |
| List | `List` | `.list()` | select, getSelectedItem |
| Table | `Table` | `.table()` | readTable, readRow, validate, clickElementInCell |

### Component Usage Pattern
```java
@Test
void componentUsageExample(Quest quest) {
    quest
        .use(RING_OF_UI)
        // Browser navigation
        .browser().navigate(getUiConfig().baseUrl())

        // Input fields
        .input().insert(InputFields.USERNAME, "admin")
        .input().insert(InputFields.PASSWORD, "password")

        // Button interaction
        .button().click(ButtonFields.LOGIN_BUTTON)

        // Alert validation
        .alert().validateValue(AlertFields.SUCCESS, "Login successful")

        // Select dropdown
        .select().selectOption(SelectFields.COUNTRY, "USA")

        // Checkbox
        .checkbox().check(CheckboxFields.TERMS)

        // Radio button
        .radio().select(RadioFields.PAYMENT_METHOD)

        .complete();
}
```

---

## UI Component Validation

**CRITICAL:** UI components use **component-specific validation methods**, NOT `Assertion.builder()`.

### Validation Pattern Differences

| Module | Validation Pattern | Example |
|--------|-------------------|---------|
| **UI Components** | Direct validation methods | `.alert().validateValue(element, text)` |
| **API** | Assertion.builder() | `Assertion.builder().target(STATUS).type(IS)...` |
| **DB** | Assertion.builder() | `Assertion.builder().target(QUERY_RESULT)...` |
| **UI Tables** | Assertion.builder() | `.table().validate(table, Assertion.builder()...)` |

### Component Validation Methods

#### Alert Validation
```java
// ✅ CORRECT: Use direct validation methods
.alert().validateValue(AlertFields.ERROR_MESSAGE, "Invalid input")
.alert().validateIsVisible(AlertFields.SUCCESS_MESSAGE)
.alert().validateIsHidden(AlertFields.ERROR_MESSAGE)

// With soft assertion (continues on failure)
.alert().validateValue(AlertFields.ERROR_MESSAGE, "Invalid input", true)

// ❌ WRONG: Don't use Assertion.builder() for alerts
.alert().validate(
    AlertFields.ERROR_MESSAGE,
    Assertion.builder()  // ❌ Causes compilation error
        .target(ALERT_TEXT)  // ❌ UiAlertAssertionTarget doesn't exist
        .type(IS)
        .expected("Invalid input")
        .build()
)
```

#### Button Validation
```java
.button().validateIsEnabled(ButtonFields.SUBMIT_BUTTON)
.button().validateIsVisible(ButtonFields.SUBMIT_BUTTON)
.button().validateIsDisabled(ButtonFields.SUBMIT_BUTTON)

// Soft assertions
.button().validateIsEnabled(ButtonFields.SUBMIT_BUTTON, true)
```

#### Input Validation
```java
.input().validateValue(InputFields.USERNAME, "expected-value")
.input().validateIsEnabled(InputFields.PASSWORD)

// Soft assertions
.input().validateValue(InputFields.USERNAME, "expected-value", true)
```

#### Checkbox Validation
```java
.checkbox().validateIsSelected(CheckboxFields.TERMS)
.checkbox().validateIsEnabled(CheckboxFields.NEWSLETTER)
.checkbox().validateIsVisible(CheckboxFields.CONSENT)

// Soft assertions
.checkbox().validateIsSelected(CheckboxFields.TERMS, true)
```

#### Select Validation
```java
.select().validateSelectedOption(SelectFields.COUNTRY, "USA")
.select().validateIsEnabled(SelectFields.LANGUAGE)
.select().validateIsVisible(SelectFields.CURRENCY)

// Soft assertions
.select().validateSelectedOption(SelectFields.COUNTRY, "USA", true)
```

### Common Validation Errors

**❌ ERROR: Trying to use Assertion.builder() for alerts**
```java
// ❌ This causes compilation error
import io.cyborgcode.roa.ui.validator.UiAlertAssertionTarget;  // Doesn't exist
import io.cyborgcode.roa.ui.validator.UiAlertAssertionTypes;   // Doesn't exist

.alert().validate(
    AlertFields.ERROR,
    Assertion.builder()
        .target(ALERT_TEXT)  // ❌ Won't compile
        .type(IS)
        .expected("Error")
        .build()
)
```

**✅ CORRECT: Use direct validation**
```java
.alert().validateValue(AlertFields.ERROR, "Error")
```

**❌ ERROR: Trying to access WebDriver from Quest**
```java
// ❌ Quest doesn't expose getDriver()
.validate(() -> {
    WebDriver driver = quest.getDriver();  // Method doesn't exist
    driver.findElement(By.id("element"));
})
```

**✅ CORRECT: Use framework services**
```java
.button().validateIsVisible(ButtonFields.ELEMENT)
```

---

## Table Operations

### Reading Tables

#### Read Entire Table
```java
// Read all rows and columns
.table().readTable(Tables.USERS_TABLE)

// Read specific columns only
.table().readTable(
    Tables.USERS_TABLE,
    TableField.of(UserRow::setUsername),
    TableField.of(UserRow::setEmail)
)

// Read row range (inclusive)
.table().readTable(Tables.USERS_TABLE, 0, 9)  // First 10 rows

// Read row range with specific columns
.table().readTable(
    Tables.USERS_TABLE,
    0, 9,
    TableField.of(UserRow::setUsername),
    TableField.of(UserRow::setStatus)
)
```

#### Read Specific Row
```java
// Read row by index (0-based)
.table().readRow(Tables.USERS_TABLE, 0)

// Read row by search criteria
.table().readRow(Tables.USERS_TABLE, List.of("john@example.com"))
```

### Table Validation

#### Table-Level Assertions
```java
// Validate table is not empty
.table().validate(
    Tables.USERS_TABLE,
    Assertion.builder()
        .target(TABLE_VALUES)
        .type(TABLE_NOT_EMPTY)
        .build()
)

// Validate row count
.table().validate(
    Tables.USERS_TABLE,
    Assertion.builder()
        .target(TABLE_VALUES)
        .type(TABLE_ROW_COUNT)
        .expected(10)
        .build()
)

// Validate all rows contain specific values
.table().validate(
    Tables.USERS_TABLE,
    Assertion.builder()
        .target(TABLE_VALUES)
        .type(EVERY_ROW_CONTAINS_VALUES)
        .expected(List.of("Active"))
        .build()
)

// Validate column uniqueness
.table().validate(
    Tables.USERS_TABLE,
    Assertion.builder()
        .target(TABLE_VALUES)
        .type(COLUMN_VALUES_ARE_UNIQUE)
        .key("email")  // Column name
        .build()
)
```

#### Row-Level Assertions
```java
// First, read specific row
.table().readRow(Tables.USERS_TABLE, 0)

// Then validate row data
.table().validate(
    Tables.USERS_TABLE,
    Assertion.builder()
        .target(ROW_VALUES)
        .type(ROW_CONTAINS_VALUES)
        .expected(List.of("john@example.com", "Active"))
        .build()
)
```

### Table Assertion Types

**Table-Level (target: TABLE_VALUES):**
* `TABLE_NOT_EMPTY` - Validates table contains data
* `TABLE_ROW_COUNT` - Validates exact row count
* `EVERY_ROW_CONTAINS_VALUES` - All rows contain specific values
* `TABLE_DOES_NOT_CONTAIN_ROW` - Row is not present
* `ALL_ROWS_ARE_UNIQUE` - No duplicate rows
* `COLUMN_VALUES_ARE_UNIQUE` - Column has unique values
* `NO_EMPTY_CELLS` - All cells have values
* `TABLE_DATA_MATCHES_EXPECTED` - Entire table matches expected dataset

**Element-Level (target: TABLE_ELEMENTS):**
* `ALL_CELLS_ENABLED` - All cells are enabled
* `ALL_CELLS_CLICKABLE` - All cells are clickable

**Row-Level (target: ROW_VALUES):**
* `ROW_NOT_EMPTY` - Row contains data
* `ROW_CONTAINS_VALUES` - Row contains specific values

### Retrieving Stored Table Data
```java
// Retrieve entire table
List<UserRow> users = retrieve(
    tableRowExtractor(Tables.USERS_TABLE),
    UserRow.class
);

// Retrieve specific row by search criteria
UserRow user = retrieve(
    tableRowExtractor(Tables.USERS_TABLE, List.of("john@example.com")),
    UserRow.class
);
```

### Table Model Definition
```java
public class UserRow {
    private String username;
    private String email;
    private String status;

    // Setters required for TableField mapping
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters for test assertions
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
}
```

---

## Authentication

### @AuthenticateViaUi
Automatic UI authentication before test execution.

**Basic Usage:**
```java
@Test
@AuthenticateViaUi(
    credentials = AdminCredentials.class,
    type = AppUiLogin.class
)
void authenticatedTest(Quest quest) {
    // Already logged in when test starts
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/dashboard")
        .button().click(ButtonFields.CREATE_ORDER)
        .complete();
}
```

**Credentials Class:**
```java
public class AdminCredentials implements Credentials {
    @Override
    public String username() {
        return Data.testData().username();  // From configuration
    }

    @Override
    public String password() {
        return Data.testData().password();
    }
}
```

**Login Implementation:**
```java
public class AppUiLogin implements UiLogin {
    @Override
    public void login(Quest quest, Credentials credentials) {
        quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME, credentials.username())
            .input().insert(InputFields.PASSWORD, credentials.password())
            .button().click(ButtonFields.LOGIN_BUTTON)
            .alert().validateValue(AlertFields.SUCCESS, "Login successful")
            .drop();  // Drop ring after authentication
    }
}
```

**Session Caching (Optional):**
```java
@AuthenticateViaUi(
    credentials = AdminCredentials.class,
    type = AppUiLogin.class,
    cacheCredentials = true  // Reuse session across tests
)
```

See [core-framework-instructions.md](core-framework-instructions.md) for general authentication patterns.

---

## UI Test Data Management

See [core-framework-instructions.md](core-framework-instructions.md) for @Craft fundamentals.

### UI-Specific @Craft Usage

**Form Data Models:**
```java
@Test
void createUserWithCraftData(
    Quest quest,
    @Craft(model = DataCreator.Data.USER) User user) {

    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/users/new")
        .input().insert(InputFields.USERNAME, user.getUsername())
        .input().insert(InputFields.EMAIL, user.getEmail())
        .input().insert(InputFields.PHONE, user.getPhone())
        .button().click(ButtonFields.SAVE_BUTTON)
        .alert().validateValue(AlertFields.SUCCESS, "User created")
        .complete();
}
```

**Automatic Form Population:**
```java
@Test
void createUserWithInsertionService(
    Quest quest,
    @Craft(model = DataCreator.Data.USER) User user) {

    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/users/new")
        // ✅ Insertion service populates all fields automatically
        .insertion().insertData(user)
        .button().click(ButtonFields.SAVE_BUTTON)
        .alert().validateValue(AlertFields.SUCCESS, "User created")
        .complete();
}
```

**Late Initialization:**
```java
@Test
void createOrderAfterPrecondition(
    Quest quest,
    Late<@Craft(model = DataCreator.Data.ORDER)> orderLate) {

    quest
        .use(RING_OF_UI)
        .button().click(ButtonFields.CREATE_ORDER)
        // ✅ Create order model after button click
        .insertion().insertData(orderLate.create())
        .button().click(ButtonFields.SUBMIT_ORDER)
        .complete();
}
```

---

## UI Configuration

### Environment-Specific URLs
```java
// ✅ CORRECT: Use configuration
.browser().navigate(getUiConfig().baseUrl())
.browser().navigate(getUiConfig().baseUrl() + "/login")

// ❌ WRONG: Hardcoded URL
.browser().navigate("http://localhost:8080")
```

### Configuration Properties
**test_data-dev.properties:**
```properties
ui.base.url=http://localhost:8080
ui.username=admin
ui.password=admin123
ui.timeout=10
```

**Accessing Configuration:**
```java
getUiConfig().baseUrl()       // UI base URL
Data.testData().username()    // Credentials from config
Data.testData().password()
```

---

## Quest API Surface for UI Testing

**Quest maintains high-level abstraction and does NOT expose internal details.**

### ✅ Available Methods
```java
quest
    .use(RING_OF_UI)
    .button().click(ButtonFields.SUBMIT)
    .input().insert(InputFields.USERNAME, "admin")
    .alert().validateValue(AlertFields.SUCCESS, "Done")
    .complete();
```

### ❌ NOT Available
```java
// ❌ These methods don't exist
quest.getDriver()          // Method doesn't exist
quest.getStorage()         // Method doesn't exist
quest.getConfiguration()   // Method doesn't exist

// ❌ WRONG: Trying to access driver
.validate(() -> {
    WebDriver driver = quest.getDriver();  // Compilation error
})

// ✅ CORRECT: Use framework services
.button().validateIsVisible(ButtonFields.ELEMENT)
```

**Rule:** Always use service ring methods. Never attempt to access Quest internals.

---

## UI Service Parent Methods

**When extending `UiServiceFluent`, use correct parent class method names.**

### Correct Method Names

| Component | ✅ Correct Parent Method | ❌ Wrong Method |
|-----------|-------------------------|-----------------|
| Alert | `getAlertField()` | `getAlert()` |
| Button | `getButtonField()` | `getButton()` |
| Input | `getInputField()` | `getInput()` |
| Select | `getSelectField()` | `getSelect()` |
| Radio | `getRadioField()` | `getRadio()` |
| Checkbox | `getCheckboxField()` | `getCheckbox()` |
| Link | `getLinkField()` | `getLink()` |
| List | `getListField()` | `getList()` |
| Table | `getTableField()` | `getTable()` |

### Example: Custom UI Service
```java
public class AppUiService extends UiServiceFluent<AppUiService> {

    public AppUiService(Quest quest) {
        super(quest);
    }

    // ✅ CORRECT: Call parent methods with "Field" suffix
    public AlertServiceFluent<AppUiService> alert() {
        return getAlertField();
    }

    public ButtonServiceFluent<AppUiService> button() {
        return getButtonField();
    }

    public InputServiceFluent<AppUiService> input() {
        return getInputField();
    }

    // ❌ WRONG: Missing "Field" suffix
    public AlertServiceFluent<AppUiService> alert() {
        return getAlert();  // Compilation error: method not found
    }
}
```

---

## Preconditions and Cleanup

See [core-framework-instructions.md](core-framework-instructions.md) for @Journey and @Ripper fundamentals.

### UI-Specific Journey Example
```java
@Test
@Journey(Preconditions.Data.CREATE_USER_VIA_UI)
@JourneyData(model = DataCreator.Data.USER)
void testWithUiPrecondition(Quest quest) {
    // User already created via UI before test starts
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/users")
        .table().validate(
            Tables.USERS_TABLE,
            Assertion.builder()
                .target(TABLE_VALUES)
                .type(TABLE_NOT_EMPTY)
                .build()
        )
        .complete();
}
```

### UI-Specific Ripper Example
```java
@Test
@Ripper(DataCleaner.Data.DELETE_USER_VIA_UI)
void testWithUiCleanup(Quest quest) {
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/users/new")
        .input().insert(InputFields.USERNAME, "testuser")
        .button().click(ButtonFields.SAVE_BUTTON)
        // @Ripper automatically deletes user after test
        .complete();
}
```

---

## Common UI Testing Patterns

### Form Submission Pattern
```java
@Test
void submitCompleteForm(Quest quest, @Craft(model = DataCreator.Data.USER) User user) {
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/register")
        .insertion().insertData(user)  // Auto-populate all fields
        .button().click(ButtonFields.SUBMIT)
        .alert().validateValue(AlertFields.SUCCESS, "Registration successful")
        .complete();
}
```

### Navigation and Validation Pattern
```java
@Test
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void navigateAndVerifyDashboard(Quest quest) {
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/dashboard")
        .button().validateIsVisible(ButtonFields.CREATE_ORDER)
        .button().validateIsVisible(ButtonFields.VIEW_REPORTS)
        .input().validateIsEnabled(InputFields.SEARCH)
        .complete();
}
```

### Multi-Step Workflow Pattern
```java
@Test
void completeOrderWorkflow(
    Quest quest,
    @Craft(model = DataCreator.Data.ORDER) Order order) {

    quest
        .use(RING_OF_UI)
        // Step 1: Navigate to orders
        .browser().navigate(getUiConfig().baseUrl() + "/orders")
        .button().click(ButtonFields.NEW_ORDER)

        // Step 2: Fill order form
        .insertion().insertData(order)
        .button().click(ButtonFields.NEXT)

        // Step 3: Review and confirm
        .button().validateIsEnabled(ButtonFields.CONFIRM)
        .button().click(ButtonFields.CONFIRM)

        // Step 4: Validate success
        .alert().validateValue(AlertFields.SUCCESS, "Order created")
        .complete();
}
```

### Soft Assertions for Multiple Validations
```java
@Test
void validateMultipleFieldsWithSoftAssertions(Quest quest) {
    quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl() + "/profile")
        // All validations execute even if one fails
        .input().validateValue(InputFields.USERNAME, "admin", true)
        .input().validateValue(InputFields.EMAIL, "admin@example.com", true)
        .button().validateIsEnabled(ButtonFields.SAVE, true)
        .button().validateIsVisible(ButtonFields.CANCEL, true)
        .complete();
}
```

---

## Summary: Key Takeaways

### Three-Layer Architecture (MANDATORY)
1. **Component Type Registry** (ui/types/*FieldTypes) - Technology identifiers
2. **Element Definition** (ui/elements/*Fields) - Locators and lifecycle
3. **Component Implementation** (ui/components/*Impl) - Interaction logic

**Missing any layer causes runtime failure.**

### Interface Contracts (MANDATORY)
* ComponentType → `getType()`
* UiElement → `enumImpl()`

**Mixing them causes compilation errors.**

### Smart API (MANDATORY)
* `findSmartElement()` not `findElement()`
* `getDomProperty()` not `getAttribute()`

### Validation (MANDATORY)
* UI components → Direct validation methods
* API/DB → `Assertion.builder()`
* Tables → `Assertion.builder()` with table-specific targets

### Quest Abstraction (MANDATORY)
* Never access `quest.getDriver()` or `quest.getStorage()`
* Always use service ring methods

### References
* Core framework concepts → [core-framework-instructions.md](core-framework-instructions.md)
* Mandatory standards → [rules.md](../../rules/rules.md)
* Best practices → [best-practices.md](../../rules/best-practices.md)
* Code examples → [ui-test-examples.md](../../ui-test-examples.md)
