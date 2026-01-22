# **ui-framework-instructions.md**

## Prerequisites
Before implementing UI components or tests, read [core-framework-instructions.md](core-framework-instructions.md) for:
- Quest object fundamentals
- Service ring management
- Test completion requirements (.complete())
- Test data management (@Craft annotation)
- Preconditions (@Journey) and cleanup (@Ripper)
- Validation requirements

This file contains **UI-specific architecture and patterns only**.

## Interface Contracts

### ComponentType vs UiElement
Two different interfaces require two different method names. **Mixing them causes compilation errors.**

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
**Rule:** Types use getType(), Elements use enumImpl().

## UI Testing with ROA Framework
The ROA framework provides a comprehensive UI testing solution with component abstraction, declarative element
definitions, lifecycle hooks, and table operations.

### Test Class Structure
**Class Annotations**
* Use `@UI` annotation on each UI test class
* Use `@DisplayName("...")` for descriptive test organization

**Class Inheritance**
* See [core-framework-instructions.md](core-framework-instructions.md) for BaseQuest vs BaseQuestSequential

### Test Method Structure
See [core-framework-instructions.md](core-framework-instructions.md) for Quest object basics, service ring management, and test completion requirements.

**UI-Specific Requirements:**
* Use `@UI` annotation on test classes
* Use `.use(RING_OF_UI)` to activate UI service ring
* Use `.drop()` when switching to other rings if your project has multiple modules

### UI Component System
**Component Architecture**
* UI components follow a three-layer architecture: Component Interface, Component Implementation, and Element Definition
* Component interfaces define the contract for interactions (e.g., `Button`, `Input`, `Select`, `Table`)
* Component implementations provide technology-specific logic (e.g., `ButtonVaImpl` for Vaadin, `ButtonBootstrapImpl` for Bootstrap)
* Element definitions are enums that declare specific UI elements with locators, types, and lifecycle hooks

**Component Interfaces**
* Define standard interaction contracts (click, insert, select, validate, etc.)
* Technology-agnostic; implementations handle specific UI frameworks
* Available components: Button, Input, Select, Radio, Checkbox, Link, Alert, List, Table, Insertion

**Component Implementations**
* Provide technology-specific logic for UI frameworks (Vaadin, Bootstrap, Material Design)
* Extend BaseComponent and implement the component interface
* Use `@ImplementationOfType` annotation to link implementations to component types
* Framework automatically selects correct implementation based on element type

**Component Usage in Tests**
* Access components via quest ring methods (e.g., `.button()`, `.input()`, `.select()`)
* Pass element enum constants to component methods (e.g., `.button().click(ButtonFields.SIGN_IN_BUTTON)`)
* Framework resolves the correct implementation and executes lifecycle hooks automatically
* No direct instantiation of components or implementations in tests

### Element Definition
**Element Structure**
* Define UI elements in dedicated enum classes (e.g., `ButtonFields`, `InputFields`, `SelectFields`)
* Each element enum must implement the corresponding UI element interface (e.g., `ButtonUiElement`, `InputUiElement`)
* Elements must specify at minimum: locator (By) and component type
* Nested Data class provides string constants for annotation-based references
* Optional before/after lifecycle hooks can be added when synchronization is needed

**Element Declaration Examples**
```java
// Minimal declaration - only locator and component type
SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE)

// With before hook for synchronization
NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE,
    SharedUi.WAIT_TO_BE_CLICKABLE)

// With both before and after hooks
DELETE_BUTTON(By.id("delete-btn"), ButtonFieldTypes.VA_BUTTON_TYPE,
    SharedUi.WAIT_TO_BE_CLICKABLE, ButtonFields::waitForRemoval)
```

**Required Properties**
* Locator (By): Selenium locator strategy (CSS, ID, tag name, XPath)
* Component Type: Technology identifier linking to component implementation via `@ImplementationOfType` (e.g., `VA_BUTTON_TYPE`, `BOOTSTRAP_INPUT_TYPE`)

**Optional Properties**
* Before Hook: Pre-interaction logic (wait for presence, clickability, overlay dismissal) - only add when needed
* After Hook: Post-interaction logic (wait for removal, page transition, loading) - only add when needed

**Multiple Constructors**
* Element enums support multiple constructors to accommodate different configuration needs
* Use the simplest constructor (locator + type) when no synchronization is needed
* Add hooks only when dealing with dynamic UI behavior or asynchronous updates
* Constructor overloading allows flexibility without forcing unnecessary complexity

**Component Implementation Linkage**
* Component implementations use `@ImplementationOfType(ComponentTypes.Data.TYPE_NAME)` annotation
* Framework automatically resolves which implementation to use based on the component type declared in element definition
* Element's component type must match an existing implementation's `@ImplementationOfType` value
* This pattern enables technology-agnostic test code with technology-specific execution

**Element Organization**
* Group related elements in domain-specific enum classes
* Use descriptive enum constant names (e.g., `SIGN_IN_BUTTON`, `USERNAME_FIELD`, `CURRENCY_SELECT`)
* Maintain one enum class per component type per domain or page
* Keep element definitions close to their usage context

### UI Locator Strategies
**Locator Best Practices**
* Prefer stable locators: IDs, CSS selectors, or tag names with attributes
* Avoid XPath unless necessary for complex element relationships
* Use `By.cssSelector()` for most web elements
* Use `By.tagName()` for technology-specific tags (e.g., vaadin-button)
* Use `By.id()` for elements with unique IDs
* Avoid brittle locators dependent on DOM structure position

**Locator Types**
* `By.id()`: Most stable; use when elements have unique IDs
* `By.cssSelector()`: Flexible and performant for most scenarios
* `By.tagName()`: Good for technology-specific components
* `By.xpath()`: Use sparingly; less performant and more brittle
* `By.className()`: Use for shared styling classes
* `By.name()`: Use for form elements with name attributes

**Dynamic Locators**
* Use parameterized locators for dynamic elements
* Build locators at runtime when element properties change
* Use contains(), starts-with() in CSS/XPath for partial matches

### Component Types
**Component Type Registries**
* Define component type registries in enums (e.g., `ButtonFieldTypes`, `InputFieldTypes`)
* Each type constant represents a specific UI technology (Material Design, Bootstrap, Vaadin, etc.)
* Component type identifiers enable automatic implementation selection at runtime
* Use ComponentType.Data constants in @ImplementationOfType annotations
* **CRITICAL:** ComponentType interface requires `getType()` method (NOT `enumImpl()`)

**Component Type Structure:**
```java
public enum ButtonFieldTypes implements ButtonComponentType {
    BOOTSTRAP_BUTTON_TYPE,
    VA_BUTTON_TYPE;

    public static final class Data {
        public static final String BOOTSTRAP_BUTTON_TYPE = "BOOTSTRAP_BUTTON_TYPE";
        public static final String VA_BUTTON_TYPE = "VA_BUTTON_TYPE";
        private Data() {}
    }

    @Override
    public Enum<?> getType() {  // ✅ MUST use getType()
        return this;
    }
}
```

**Available Component Types**
* Angular Material Design: MD_BUTTON_TYPE, MD_INPUT_TYPE, etc.
* Bootstrap: BOOTSTRAP_BUTTON_TYPE, BOOTSTRAP_INPUT_TYPE, etc.
* Vaadin: VA_BUTTON_TYPE, VA_INPUT_TYPE, etc.
* **Other types of technologies** can be added by creating a new enum and adding it to the registry. (ex. react components, etc...)

### Component Implementations
**Three-Layer Architecture Requirement**
* Every UI component MUST have three layers or runtime component resolution fails:
    1. **Component Type Registry** (ui/types/*FieldTypes) - Technology identifier
    2. **Element Definition** (ui/elements/*Fields) - Locator registry
    3. **Component Implementation** (ui/components/<type>/*Impl) - Actual interaction logic (<type> can be button, input, etc...)

**Component Implementation Structure:**
```java
@ImplementationOfType(ButtonFieldTypes.Data.BOOTSTRAP_BUTTON_TYPE)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

    public ButtonBootstrapImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    public void click(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);  // ✅ Use findSmartElement
        button.click();
    }

    @Override
    public boolean isEnabled(By locator) {
        SmartWebElement button = driver.findSmartElement(locator);
        return button.isEnabled();
    }
}
```

**Smart API Requirements:**
* Component implementations MUST use findSmartElement() not findElement()
* Component implementations MUST use getDomProperty() not getAttribute()
* Using standard Selenium APIs causes type mismatch compilation errors

**Smart API Reference:**

| Standard Selenium API    | Smart API                       | Why                                             |
|--------------------------|----------------------------------|------------------------------------------------|
| driver.findElement()     | driver.findSmartElement()        | Returns SmartWebElement with framework features|
| element.findElement()    | element.findSmartElement()       | Nested element search                          |
| element.getAttribute()   | element.getDomProperty()         | Modern DOM property access                     |

### Lifecycle Hooks
**Before Hooks**
* Execute pre-interaction logic (wait for presence, clickability, overlay dismissal)
* Use ContextConsumer with .asConsumer(locator) to create locator-aware wait strategies
* Predefined strategies available in `SharedUi` (e.g., `WAIT_FOR_LOADING`, `WAIT_TO_BE_CLICKABLE`)

**After Hooks**
* Execute post-interaction logic (wait for element removal, page transition, loading)
* Synchronize with asynchronous UI updates
* Ensure stable state before next interaction

**Custom Wait Strategies**
* Define shared wait strategies in SharedUi enum for reusability across elements
* Use SharedUiFunctions for common wait patterns (overlays, presence, visibility)
* Avoid duplicating wait logic in test methods; encapsulate in element definitions

### Table Operations
**Reading Tables**
* Use `.table().readTable(Tables.TABLE_NAME)` to read entire tables into framework storage
* Use `.table().readTable(Tables.TABLE_NAME, TableField.of(Model::setter)...)` to read specific columns only
* Use `.table().readTable(Tables.TABLE_NAME, startIndex, endIndex)` to read a subset of rows by range (inclusive)
* Use `.table().readTable(Tables.TABLE_NAME, startIndex, endIndex, TableField.of(Model::setter)...)` to combine row range with specific columns
* Table data is automatically stored in framework storage for later assertions

**Reading Specific Rows**
* Use `.table().readRow(Tables.TABLE_NAME, rowIndex)` to read a specific row by index
* Use `.table().readRow(Tables.TABLE_NAME, List.of(searchValue...))` to find and read a row by search criteria
* Row reading narrows context for subsequent row-level validations

**Table Validation**
* Use `.table().validate(Tables.TABLE_NAME, Assertion.builder()...)` to validate table data
* Use `TABLE_VALUES` target for value-based assertions (row count, uniqueness, content matching)
* Use `TABLE_ELEMENTS` target for element state assertions (enabled, clickable)
* Use `ROW_VALUES` target for row-specific assertions after .readRow()
* Retrieve stored table data via `retrieve(tableRowExtractor(Tables.TABLE_NAME, searchCriteria), ModelClass.class)`

**Table Field Mapping**
* Define table row models with proper field mappings (setters for each column)
* Use `TableField.of(Model::setterMethod)` to specify which columns to extract
* Table models must have setters that match the column structure
* Define table references in dedicated Tables enum

**Table Assertion Types**
* `TABLE_NOT_EMPTY`: Validates table contains data
* `TABLE_ROW_COUNT`: Validates exact row count
* `EVERY_ROW_CONTAINS_VALUES`: Validates all rows contain specific values
* `TABLE_DOES_NOT_CONTAIN_ROW`: Validates row is not present
* `ALL_ROWS_ARE_UNIQUE`: Validates no duplicate rows
* `NO_EMPTY_CELLS`: Validates all cells have values
* `COLUMN_VALUES_ARE_UNIQUE`: Validates column has unique values
* `TABLE_DATA_MATCHES_EXPECTED`: Validates entire table data against expected dataset
* `ALL_CELLS_ENABLED`: Validates all cells are enabled
* `ALL_CELLS_CLICKABLE`: Validates all cells are clickable
* `ROW_NOT_EMPTY`: Validates row contains data
* `ROW_CONTAINS_VALUES`: Validates row contains specific values

### Authentication
**@AuthenticateViaUi**
* Use `@AuthenticateViaUi(credentials = CredentialsClass.class, type = LoginClass.class)` for automatic UI login
* No session caching by default; authentication runs per test
* Define login flows in dedicated login classes (e.g., `AppUiLogin`)
* Authentication executes before test method starts

### UI Component Validation
**Component-Specific Validation Methods**
* UI components use dedicated validation methods, NOT the generic `Assertion.builder()` pattern
* `Assertion.builder()` is only for API/DB/Table validation

**Example: Available Alert Validation Methods:**
```java
// Hard assertion (fails immediately)
.alert().validateValue(AlertFields.ERROR_MESSAGE, "Expected text")
.alert().validateIsVisible(AlertFields.ERROR_MESSAGE)
.alert().validateIsHidden(AlertFields.SUCCESS_MESSAGE)

// Soft assertion (continues on failure)
.alert().validateValue(AlertFields.ERROR_MESSAGE, "Expected text", true)
.alert().validateIsVisible(AlertFields.ERROR_MESSAGE, true)
.alert().validateIsHidden(AlertFields.SUCCESS_MESSAGE, true)
```

```java
@Test
void errorAlertIsDisplayedWithCorrectMessage(Quest quest) {
    quest
        .use(RING_OF_UI)
        .button().click(ButtonFields.SUBMIT_BUTTON)
        .alert().validateValue(AlertFields.ERROR_ALERT, "Invalid input")
        .complete();
}
```

**IMPORTANT - Do NOT Use:**
* ❌ Assertion.builder() for alert validation
* ❌ import io.cyborgcode.roa.ui.validator.UiAlertAssertionTarget - doesn't exist
* ❌ import io.cyborgcode.roa.ui.validator.UiAlertAssertionTypes - doesn't exist

### Test Data Management
See [core-framework-instructions.md](core-framework-instructions.md) for @Craft annotation details.

**UI-Specific @Craft Usage:**
* Use `@Craft(model = DataCreator.Data.MODEL_NAME)` to inject UI form data models
* Access model fields via getter methods in UI interaction steps
* Use `.insertion().insertData(model)` for automatic form population with crafted models
* Use `Late<@Craft>` for lazy instantiation when the form data depends on data produced earlier in the quest

### Preconditions and Cleanup
See [core-framework-instructions.md](core-framework-instructions.md) for @Journey and @Ripper fundamentals.

**UI-Specific Usage:**
* Use `@Journey` to perform UI or mixed (UI/API/DB) precondition flows before the main test execution
* Define UI-related preconditions in the `Preconditions` enum
* Use `@Ripper` to specify cleanup operations related to UI-created data (users, orders, form submissions, uploaded files)
* Ensures isolation of UI test data and prevents pollution of the application state across test runs

**Insertion Service**
* Use `.insertion().insertData(model)` to populate forms automatically
* Model fields must be properly mapped to UI elements via annotations or configuration
* Significantly reduces code verbosity for complex forms

**Configuration**
* Use `getUiConfig().baseUrl()` for environment-specific UI URLs
* Store UI credentials in test_data-{env}.properties

### Quest API Surface
**Quest maintains high-level abstraction and does NOT expose internal details.**

**Available Methods:**
```java
quest
    .use(RING_OF_UI)
    .button().click(ButtonFields.SUBMIT_BUTTON)
    .button().validateIsVisible(ButtonFields.SUBMIT_BUTTON)
    .complete();
```

**NOT Available:**
* ❌ quest.getDriver() - Method doesn't exist
* ❌ quest.getStorage() - Method doesn't exist

For validation, use framework services instead:
```java
// ❌ WRONG
.validate(() -> {
    WebElement userBtn = quest.getDriver().findElement(By.className("user-btn"));
})

// ✅ CORRECT
.button().validateIsVisible(ButtonElements.USER_BTN)
.button().validateIsEnabled(ButtonElements.USER_BTN)
```

### UI Service Parent Methods
**When extending `UiServiceFluent`, use correct parent class method names.**

| Component | ✅ Correct Parent Method | ❌ Wrong Method |
|-----------|-------------------------|----------------|
| Alert     | `getAlertField()`       | `getAlert()`   |
| Button    | `getButtonField()`      | `getButton()`  |
| Input     | `getInputField()`       | `getInput()`   |
| Select    | `getSelectField()`      | `getSelect()`  |
| Radio     | `getRadioField()`       | `getRadio()`   |
| Checkbox  | `getCheckboxField()`    | `getCheckbox()`|
| Link      | `getLinkField()`        | `getLink()`    |
| List      | `getListField()`        | `getList()`    |

**Example:**
```java
public class AppUiService extends UiServiceFluent<AppUiService> {

    public AlertServiceFluent<AppUiService> alert() {
        return getAlertField();  // ✅ Correct
    }

    public ButtonServiceFluent<AppUiService> button() {
        return getButtonField();  // ✅ Correct
    }
}
```
