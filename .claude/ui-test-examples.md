# **ui-test-examples.md**

## **Overview**
This document provides curated examples of UI testing using the ROA framework. Each example demonstrates a unique pattern or feature. Examples progress from basic to advanced usage.

## Prerequisites
**Before using these examples, read:**
1. [.claude/instructions/core-framework-instructions.md](../instructions/core-framework-instructions.md) - Core framework concepts
2. [.claude/instructions/ui-framework-instructions.md](../instructions/ui-framework-instructions.md) - UI architecture details
3. [.claude/rules/rules.md](../rules/rules.md) - Mandatory standards
4. [.claude/rules/best-practices.md](../rules/best-practices.md) - Recommended practices

## **Reference Test Class Structure**
Every UI test file should follow this template. Import only what you need for your specific test.

```java
package io.cyborgcode.ui.simple.test.framework;

// Core Framework Imports (Always needed)
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import org.junit.jupiter.api.Test;

// Test Annotations (As needed)
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Smoke;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;

// Configuration (For environment-specific URLs)
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;

// Rings (For service activation)
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

// UI Elements (Import specific field classes as needed)
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.AlertFields;
// ... other element fields as needed

// Constants (Import specific constant groups as needed)
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.*;

// ===== OPTIONAL: Import only if using these features =====

// Authentication (@AuthenticateViaUi)
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AppUiLogin;

// Data Crafting (@Craft)
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.ui.simple.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;

// Journeys (@Journey, @JourneyData)
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import static io.cyborgcode.ui.simple.test.framework.preconditions.Preconditions.Data.*;

// Table Operations
import io.cyborgcode.roa.ui.components.table.base.TableField;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.simple.test.framework.ui.elements.Tables;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.*;
import static io.cyborgcode.roa.ui.storage.DataExtractorsUi.tableRowExtractor;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.*;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.*;

// Late/Lazy initialization
import io.cyborgcode.roa.framework.parameters.Late;

// Multi-module testing (Database, API)
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Ripper;

@UI
@DisplayName("Example UI Test")
class ExampleUiTest extends BaseQuest {
    // Tests go here...
}
```

## ⚠️ Common Test Mistakes

### ❌ Mistake 1: Trying to Access WebDriver from Quest
**WRONG:**
```java
@Test
void login_validates_userIsLoggedIn_WRONG(Quest quest) {
    quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.LOGIN_BUTTON)
            .validate(() -> {
                // ❌ Quest doesn't expose getDriver()
                quest.getDriver().findElement(By.cssSelector(".settings-icon"));
            })
            .complete();
}
```

**CORRECT:**
```java
@Test
void login_validates_successMessage_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl() + "/login")
            .input().insert(InputFields.USERNAME, "admin")
            .input().insert(InputFields.PASSWORD, "password")
            .button().click(ButtonFields.LOGIN_BUTTON)
            // ✅ Validate success alert appears
            .alert().validateValue(AlertFields.LOGIN_SUCCESS, "Welcome back!")
            .complete();
}
```

**Examples of component-specific validation:**
```java
// Checkbox validation
@Test
void checkboxState_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .checkbox().check(CheckboxFields.TERMS_AGREEMENT)
            .checkbox().validateIsEnabled(CheckboxFields.TERMS_AGREEMENT)
            .checkbox().validateIsSelected(CheckboxFields.TERMS_AGREEMENT)
            .complete();
}

// Select/Dropdown validation
@Test
void dropdownSelection_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .select().selectByValue(SelectFields.COUNTRY, "USA")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUCCESS, "Country set to USA")
            .complete();
}
```

**Why Wrong:** Quest maintains abstraction and doesn't expose WebDriver. Use framework services instead.

### ❌ Mistake 2: Using Assertion.builder() for Alert Validation
**WRONG:**
```java
@Test
void alertValidation_WRONG(Quest quest) {
    quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            // ❌ UiAlertAssertionTarget and UiAlertAssertionTypes don't exist
            .alert().validate(
                    AlertFields.ERROR_ALERT,
                    Assertion.builder()
                            .target(ALERT_TEXT)
                            .type(IS)
                            .expected("Error message")
                            .build())
            .complete();
}
```

**CORRECT:**
```java
@Test
void alertValidation_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            // ✅ Alert has specific validation methods
            .alert().validateValue(AlertFields.ERROR_ALERT, "Error message")
            .complete();
}
```

**Why Wrong:** UI components use specific validation methods, NOT Assertion.builder(). Only use Assertion.builder() for API/DB/Table validation.

### ❌ Mistake 3: Missing Validations in Tests
**WRONG:**
```java
@Test
void userLogin_WRONG(Quest quest) {
    quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME, "admin")
            .input().insert(InputFields.PASSWORD, "password")
            .button().click(ButtonFields.LOGIN_BUTTON)
            // ❌ No validation - test doesn't verify anything
            .complete();
}
```

**CORRECT:**
```java
@Test
void userLogin_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME, "admin")
            .input().insert(InputFields.PASSWORD, "password")
            .button().click(ButtonFields.LOGIN_BUTTON)
            // ✅ Validate successful login
            .alert().validateValue(AlertFields.SUCCESS_MESSAGE, "Login successful")
            .complete();
}
```

**Why Wrong:** Every test MUST have at least one validation. Otherwise, you're just executing actions without verifying outcomes.

## **Test Examples - Progressive Learning Path**

### **1. Baseline Flow - Manual Steps**
**Pattern:** Basic UI interactions with hardcoded data (not recommended for production).

```java
@Test
@Regression
@Description("Baseline simple flow without advanced framework features")
void baseline_simpleFlow(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate("http://zero.webappsecurity.com/")
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, "username")
         .input().insert(InputFields.PASSWORD_FIELD, "password")
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```

**Key Concepts:** Quest chaining, component methods, .complete() requirement, basic validation.

### **2. Configuration-Driven Data**
**Pattern:** Use configuration files for environment-specific data (URLs, credentials).

```java
@Test
@Regression
@Description("Retrieves credentials from configuration properties")
void config_drivenCredentials(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())  // ✅ Config-driven URL
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())  // ✅ Config-driven
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())  // ✅ Config-driven
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```

**Key Concepts:** getUiConfig().baseUrl(), Data.testData(), environment independence.

### **3. Automatic Authentication**
**Pattern:** Use @AuthenticateViaUi to handle login automatically.

```java
@Test
@Regression
@Description("AuthenticateViaUi performs automatic login")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void authenticateViaUi_automaticLogin(Quest quest) {
   quest
         .use(RING_OF_UI)
         // ✅ Already logged in via @AuthenticateViaUi
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```

**Key Concepts:** @AuthenticateViaUi annotation, credentials class, login type class. Add cacheCredentials = true to reuse session across tests.

### **4. Data Injection with @Craft**
**Pattern:** Use @Craft to inject typed model instances for test data.

```java
@Test
@Regression
@Description("Craft injects typed model instances")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void craft_injectsModelData(
      Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseData) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         // ✅ Using crafted model values
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, purchaseData.getCurrency())
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, purchaseData.getAmount())
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```

**Key Concepts:** @Craft annotation, DataCreator.Data constants, model getter methods, data reusability.

### **5. Insertion Service - Automatic Form Population**
**Pattern:** Use .insertion().insertData() to populate forms automatically from models.

```java
@Test
@Regression
@Description("Insertion service maps model fields to UI controls")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void insertionService_populatesForm(Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseData) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         // ✅ Insertion service populates all fields automatically
         .insertion().insertData(purchaseData)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```

**Key Concepts:** .insertion().insertData(), field mapping via annotations, reduced boilerplate.

### **6. Custom Service Rings**
**Pattern:** Use custom rings to encapsulate domain-specific workflows.

```java
@Test
@Regression
@Description("Custom service ring encapsulates workflow")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void customRing_encapsulatesWorkflow(Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseData) {
   quest
         // ✅ Use custom ring with domain-specific methods
         .use(RING_OF_PURCHASE_CURRENCY)
         .purchaseCurrency(purchaseData)
         .validatePurchase()
         .complete();
}
```

**Key Concepts:** Custom rings, domain-specific methods, workflow encapsulation, .drop() when switching rings.

### **7. Journey Preconditions**
**Pattern:** Use @Journey to execute reusable preconditions before tests.

```java
@Test
@Regression
@Description("Journey executes reusable preconditions")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
@Journey(value = PURCHASE_CURRENCY_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.PURCHASE_CURRENCY)})
void journey_precondition(Quest quest) {
   quest
         .use(RING_OF_PURCHASE_CURRENCY)
         // ✅ Precondition already executed via @Journey
         .validatePurchase()
         .complete();
}
```

**Multiple Journeys:**
```java
@Journey(value = USER_LOGIN_PRECONDITION)
@Journey(value = CREATE_ORDER_PRECONDITION, 
         journeyData = {@JourneyData(DataCreator.Data.ORDER)})
@Test
void multipleJourneys_composedPreconditions(Quest quest) {
   // Both preconditions executed in order
}
```

**Key Concepts:** @Journey annotation, @JourneyData for models, journey composition, precondition reusability.

### **8. Text Field Validation**
**Pattern:** Validate text presence in UI fields with soft/hard assertions.

```java
@Test
@Smoke
@Description("Validate text in UI fields - soft assertions")
void validateTextInFields_softAssertions(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .link().click(LinkFields.ACCOUNT_LINK)
         // ✅ Soft assertions (true) - continue on failure
         .validate().validateTextInField(Tag.DIV, "Account Balance", true)
         .validate().validateTextInField(Tag.SPAN, "Available Credit", true)
         .complete();
}
```

**Key Concepts:** .validate().validateTextInField(), Tag enum, soft assertions (continues on failure), hard assertions (stops on failure).

### **9. Table Operations - Read and Validate**
**Pattern:** Read table data and validate using table assertion types.

```java
@Test
@Smoke
@Description("Read table and validate using assertion types")
void table_readAndValidate(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
         .button().click(ButtonFields.FIND_TRANSACTIONS_BUTTON)
         // ✅ Read entire table
         .table().readTable(Tables.TRANSACTIONS)
         // ✅ Validate using table assertion types
         .table().validate(
               Tables.TRANSACTIONS,
               Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(5).build(),
               Assertion.builder().target(TABLE_VALUES).type(ALL_ROWS_ARE_UNIQUE).expected(true).build())
         // ✅ Read specific row
         .table().readRow(Tables.TRANSACTIONS, 1)
         .table().validate(
               Tables.TRANSACTIONS,
               Assertion.builder().target(ROW_VALUES).type(ROW_NOT_EMPTY).expected(true).build())
         .complete();
}
```

**Read Specific Columns:**
```java
// Read only specific columns for better performance
.table().readTable(Tables.TRANSACTIONS, 
    TableField.of(Transaction::setDate),
    TableField.of(Transaction::setAmount))
```

**Read Row Range:**
```java
// Read rows 3-5 (inclusive)
.table().readTable(Tables.TRANSACTIONS, 3, 5)
```

**Key Concepts:** .table().readTable(), table assertion types, TABLE_VALUES vs ROW_VALUES targets, tableRowExtractor for data retrieval.

### **10. Multi-Module Testing (UI + API + DB)**
**Pattern:** Combine UI, API, and DB operations in a single test.

```java
@UI
@API
@DB
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@DisplayName("Multi-Module Test")
class MultiModuleTest extends BaseQuest {

   @Test
   @Smoke
   @Description("Validate data consistency across UI, API, and DB")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void multiModule_dataConsistency(Quest quest,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
      quest
            // ✅ Create order via custom UI ring
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .drop()
            // ✅ Validate order via API
            .use(RING_OF_API)
            .requestAndValidate(
                  OrderEndpoints.GET_ORDER.withParam(order.getOrderId()),
                  Assertion.builder().target(STATUS).type(IS).expected(200).build())
            .drop()
            // ✅ Validate order in database
            .use(RING_OF_DB)
            .requestAndValidate(
                  AppQueries.QUERY_ORDER.withParam(order.getOrderId()),
                  Assertion.builder().target(RESULT_SET_SIZE).type(IS).expected(1).build())
            .complete();
   }
}
```

**Key Concepts:** Multiple module annotations (@UI, @API, @DB), .drop() when switching rings, @DbHook for database setup, consistent data validation across layers.

### **11. Data Cleanup with @Ripper**
**Pattern:** Automatic cleanup of test data after execution.

```java
@Test
@Smoke
@Description("Automatic data cleanup using @Ripper")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
@Ripper(DataCleaner.Data.ORDER_CLEANER)
void testWithCleanup(Quest quest,
      @Craft(model = DataCreator.Data.ORDER) Order order) {
   quest
         .use(RING_OF_CUSTOM)
         .createOrder(order)
         .validateOrder(order)
         // ✅ @Ripper automatically cleans up after test (success or failure)
         .complete();
}
```

**Key Concepts:** @Ripper annotation, DataCleaner registry, cleanup after success/failure, test isolation.

### **12. Late Initialization**
**Pattern:** Use Late<@Craft> when model creation depends on runtime data.

```java
@Test
@Regression
@Description("Late initialization for runtime-dependent data")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void lateInitialization_runtimeData(Quest quest,
      Late<@Craft(model = DataCreator.Data.ORDER)> orderLate) {
   quest
         .use(RING_OF_CUSTOM)
         .performPreconditionAction()
         // ✅ Create order model after precondition, using runtime data
         .createOrder(orderLate.create())
         .validateOrder(orderLate.create())
         .complete();
}
```

**Key Concepts:** Late<@Craft>, .create() method, lazy initialization, runtime dependencies.

## Available UI Components

| Component | Entry Method | Common Operations |
|-----------|--------------|-------------------|
| Browser | `.browser()` | navigate, back, forward, refresh, switchTab |
| Button | `.button()` | click, doubleClick, validateIsEnabled, validateIsVisible |
| Input | `.input()` | insert, clear, getValue, validateValue |
| Link | `.link()` | click, getHref, validateIsEnabled |
| Select | `.select()` | selectOption, getSelectedOption, validateSelectedOption |
| Radio | `.radio()` | select, validateIsSelected |
| Checkbox | `.checkbox()` | check, uncheck, validateIsChecked, validateIsSelected |
| Alert | `.alert()` | validateValue, accept, dismiss, getText |
| List | `.list()` | select, validateIsSelected |
| Table | `.table()` | readTable, readRow, validate, clickElementInCell |
| Insertion | `.insertion()` | insertData (populates forms from models) |
| Validate | `.validate()` | validateTextInField, validate (custom lambda) |

## Available Table Assertion Types

| Assertion Type | Description | Target |
|----------------|-------------|--------|
| `TABLE_NOT_EMPTY` | Validates table contains data | `TABLE_VALUES` |
| `TABLE_ROW_COUNT` | Validates exact row count | `TABLE_VALUES` |
| `EVERY_ROW_CONTAINS_VALUES` | Validates all rows contain values | `TABLE_VALUES` |
| `TABLE_DOES_NOT_CONTAIN_ROW` | Validates row is not present | `TABLE_VALUES` |
| `ALL_ROWS_ARE_UNIQUE` | Validates no duplicate rows | `TABLE_VALUES` |
| `COLUMN_VALUES_ARE_UNIQUE` | Validates column uniqueness | `TABLE_VALUES` |
| `ALL_CELLS_ENABLED` | Validates all cells enabled | `TABLE_ELEMENTS` |
| `ALL_CELLS_CLICKABLE` | Validates all cells clickable | `TABLE_ELEMENTS` |
| `ROW_NOT_EMPTY` | Validates row contains data | `ROW_VALUES` |
| `ROW_CONTAINS_VALUES` | Validates row contains values | `ROW_VALUES` |

## Quick Reference Checklist

**Basic Test Setup:**
- [ ] Add `@UI` annotation to test class
- [ ] Extend `BaseQuest`
- [ ] Quest parameter as first argument
- [ ] Use `.use(RING_OF_UI)` to activate UI ring
- [ ] End with `.complete()`

**Configuration:**
- [ ] Use `getUiConfig().baseUrl()` for URLs
- [ ] Use `Data.testData()` for credentials
- [ ] Define element enums with locators and types

**Advanced Features:**
- [ ] Use `@AuthenticateViaUi` for automatic login
- [ ] Use `@Craft` for test data models
- [ ] Use `@Journey` for preconditions
- [ ] Use `@Ripper` for cleanup
- [ ] Use `.insertion().insertData()` for forms
- [ ] Use custom rings for domain workflows

**Validation:**
- [ ] Every test must have at least one validation
- [ ] Use component-specific methods (not Assertion.builder())
- [ ] Use soft assertions for multiple related checks
- [ ] Use `.validate(() -> {})` for custom logic

**Multi-Module:**
- [ ] Add module annotations (`@UI`, `@API`, `@DB`)
- [ ] Use `.drop()` before switching rings
- [ ] Validate consistency across layers
