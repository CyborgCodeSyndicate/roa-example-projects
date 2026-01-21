# **ui-test-examples.md**

## **Overview**
This document provides comprehensive examples of UI testing using the ROA (Ring of Automation) test framework. 
The examples demonstrate progressive feature adoption from basic UI flows to advanced capabilities including 
authentication, data crafting, insertion services, custom rings, table operations, and journey-based preconditions.

## **Reference Test Class Structure**
``
Every UI test file should follow this template to ensure the AI has context for imports and annotations.
Import only what you need for your specific test.
``
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
import io.cyborgcode.roa.ui.validator.TableAssertionTypes;
import io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.simple.test.framework.ui.elements.Tables;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.*;
import static io.cyborgcode.roa.ui.storage.DataExtractorsUi.tableRowExtractor;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.*;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;

// Configuration-driven test data
import io.cyborgcode.ui.simple.test.framework.data.test_data.Data;

// Late/Lazy initialization
import io.cyborgcode.roa.framework.parameters.Late;

// Advanced features (Database, API, Hooks)
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.annotation.StaticTestData;
import io.cyborgcode.roa.ui.annotations.InterceptRequests;

@UI
@DisplayName("Example UI Test")
class ExampleUiTest extends BaseQuest {
    // Tests go here...
}
```

## Common Test Mistakes

## ⚠️ Common UI Test Mistakes

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

@Test
void login_validates_dashboardAccess_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl() + "/login")
            .input().insert(InputFields.USERNAME, "admin")
            .input().insert(InputFields.PASSWORD, "password")
            .button().click(ButtonFields.LOGIN_BUTTON)
            // ✅ Validate by navigating to PROTECTED dashboard (requires auth)
            .browser().navigate(getUiConfig().baseUrl() + "/dashboard")
            // ✅ Further validate dashboard element is visible
            .alert().validateIsVisible(DashboardElements.USER_MENU)
            .complete();
}
```
`Here are examples how to use the ui components in test (not via getDriver()):`
#### Checkbox Component
**WRONG:**
```java
@Test
void checkboxState_accessingDriver_WRONG(Quest quest) {
    quest
            .use(RING_OF_UI)
            .checkbox().check(CheckboxFields.TERMS_AGREEMENT)
            .validate(() -> {
                // ❌ Quest doesn't expose getDriver()
                WebElement checkbox = quest.getDriver().findElement(By.id("terms"));
                assertTrue(checkbox.isSelected());
            })
            .complete();
}
```
**CORRECT:**

```java
@Test
void checkboxState_usingFramework_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .checkbox().check(CheckboxFields.TERMS_AGREEMENT)
            // ✅ Validate via enabled state of dependent button
            .checkbox().validateIsEnabled(ButtonFields.SUBMIT_BUTTON)
            .checkbox().validateIsSelected(ButtonFields.SUBMIT_BUTTON)
            .complete();
}
```

#### Select/Dropdown Component
**WRONG:**
```java
@Test
void dropdownSelection_accessingDriver_WRONG(Quest quest) {
    quest
            .use(RING_OF_UI)
            .select().selectByValue(SelectFields.COUNTRY, "USA")
            .validate(() -> {
                // ❌ Quest doesn't expose getDriver()
                WebElement select = quest.getDriver().findElement(By.id("country"));
                Select dropdown = new Select(select);
                assertEquals("USA", dropdown.getFirstSelectedOption().getText());
            })
            .complete();
}
```
**CORRECT:**

```java
@Test
void dropdownSelection_usingFramework_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .select().selectByValue(SelectFields.COUNTRY, "USA")
            // ✅ Validate via subsequent action or alert
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUCCESS, "Country set to USA")
            .complete();
}
```
**Why Wrong:** Quest maintains abstraction and doesn't expose the WebDriver. Use framework services instead.

### ❌ Mistake 2: Using Assertion.builder() for alert component validation
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

@Test
void elementVisibility_usingFramework_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.SHOW_MODAL)
            // ✅ Use framework visibility validation
            .button().validateIsVisible(ModalElements.MODAL_DIALOG)
            .button().validateIsEnabled(ModalElements.MODAL_DIALOG)
            .complete();
}
```
**Why Wrong:** UI components use specific validation methods, NOT Assertion.builder(). 

### ❌ Mistake 3: Missing Validations in Tests
**WRONG:**
```java
@Test
@DisplayName("User login")
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
@DisplayName("User login with validation")
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

@Test
@DisplayName("User login displays user profile icon")
void userLogin_displaysUserProfileIcon_CORRECT(Quest quest) {
    quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl() + "/login")
            .input().insert(InputFields.USERNAME, "admin")
            .input().insert(InputFields.PASSWORD, "password")
            .button().click(ButtonFields.LOGIN_BUTTON)
            // ✅ Validate user profile icon is visible after login
            .button().validateIsVisible(HeaderElements.USER_PROFILE_ICON)
            .button().validateIsEnabled(HeaderElements.USER_PROFILE_ICON)
            .complete();
}
```
**Why Wrong:** Every test MUST have at least one validation. Otherwise, you're just executing actions without verifying outcomes.


## **Test Examples**
``Each numbered example section contains:``
**Example title:** One line per scenario (baseline flow, config-driven data, @AuthenticateViaUi, @Craft, insertion service,
custom ring usage, journeys, table read/validate variants).
**Short description:** 1–2 sentences describing the feature progression or UI concept.
**Code snippet:** Full Java test method showing the intended UI DSL usage.
**Key Patterns:** Bullet list explaining navigation and basic UI interactions. How config data, auth,
Craft models, insertion, rings, and journeys fit in. How table read/validate methods and assertion targets are used.

### **1. Baseline Simple Flow - No Advanced Features**
**Description:** Demonstrates a basic UI automation flow without using any advanced framework features. 
This serves as a starting point showing manual navigation, input, and validation steps.

```java
@Test
@Regression
@Description("Baseline simple flow without advanced framework features")
void baseline_simpleFlow_noAdvancedFeatures(Quest quest) {
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
         .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use RING_OF_UI for standard UI operations
Chain UI component interactions fluently (browser, button, input, link, etc.)
Complete the quest with .complete()
All credentials and data are hardcoded (not recommended for production)
``

### **2. Configuration-Driven Test Data**
**Description:** Retrieves login credentials and application URLs from configuration properties instead of hardcoding 
values in the test.

```java
@Test
@Regression
@Description("Retrieves Login credentials from configuration properties")
void config_properties_retrievedLoginCredentials(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         // Data.testData(): Reads test data (e.g., login credentials) from config properties
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use getUiConfig().baseUrl() for environment-specific URLs
Use Data.testData().username() and Data.testData().password() for config-driven credentials
Centralize configuration to support multiple environments (dev, test, prod)
``

### **3. Automatic UI Authentication - Per Test**
**Description:** Uses @AuthenticateViaUi annotation to perform automatic login before each test execution.
By default, the session is not cached (each test gets a fresh login).

```java
@Test
@Regression
@Description("AuthenticateViaUi performs login per test without session caching")
// @AuthenticateViaUi: auto-login per test as precondition (no cached session)
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void authenticateViaUi_perTestNoCache(Quest quest) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use @AuthenticateViaUi annotation for automatic authentication
Specify credentials class containing login data
Specify type class implementing the login flow
Authentication executes before the test method starts
Test logic begins post-authentication
To cache and reuse session across tests, add cacheCredentials = true
``

### **3a. Automatic UI Authentication - Cached Session**
**Description:** Uses @AuthenticateViaUi with cacheCredentials=true to perform login once and reuse
the session across multiple tests for better performance.

```java
@Test
@Regression
@Description("Login is handled once via @AuthenticateViaUi and reused")
// Uses @AuthenticateViaUi with cacheCredentials=true so login runs once and is reused across tests
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
void testWithCachedSession_1(Quest quest) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, LOAN_ACCOUNT)
         .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, CREDIT_CARD_ACCOUNT)
         .input().insert(InputFields.AMOUNT_FIELD, "300")
         .input().insert(InputFields.TF_DESCRIPTION_FIELD, TRANSFER_LOAN_TO_CREDIT_CARD)
         .button().click(ButtonFields.SUBMIT_BUTTON)
         .button().click(ButtonFields.SUBMIT_BUTTON)
         .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, SUCCESSFUL_TRANSFER_MESSAGE)
         .complete();
}

@Test
@Regression
@Description("Login session is reused from cached login")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
void testWithCachedSession_2(Quest quest) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, SAVINGS_ACCOUNT)
         .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, CHECKING_ACCOUNT)
         .input().insert(InputFields.AMOUNT_FIELD, "2000")
         .input().insert(InputFields.TF_DESCRIPTION_FIELD, TRANSFER_SAVING_TO_CHECKING)
         .button().click(ButtonFields.SUBMIT_BUTTON)
         .button().click(ButtonFields.SUBMIT_BUTTON)
         .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, SUCCESSFUL_TRANSFER_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use cacheCredentials = true to cache and reuse authenticated session
First test with cacheCredentials=true performs login and caches session
Subsequent tests with same credentials reuse the cached session
Significantly improves test execution speed for test suites
All tests must use the same credentials class for session reuse
``

### **4. Data Injection via @Craft Models**
**Description:** Demonstrates using @Craft annotation to inject typed model instances that provide test 
data for UI operations.

```java
@Test
@Regression
@Description("Craft injects a typed model instance for data-driven steps")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void craft_injectsModelDataIntoSteps(
      Quest quest,
      // @Craft: provides a typed model instance resolved by the data creator
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency)
{
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
         // purchaseForeignCurrency: Using crafted model values directly in UI steps
         .select().selectOption(SelectFields.PC_CURRENCY_DDL, purchaseForeignCurrency.getCurrency())
         .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, purchaseForeignCurrency.getAmount())
         .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use @Craft(model = DataCreator.Data.MODEL_NAME) to inject test data models.
Models are materialized eagerly before test execution
Access model fields via getter methods in UI steps
Promotes data reusability across tests
``

### **5. Insertion Service - Form Population from Model**
**Description:** Shows how the insertion service can automatically map model fields to UI controls, 
reducing boilerplate code for form population.

```java
@Test
@Regression
@Description("Insertion service maps model fields to UI controls in one operation")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void insertionService_populatesFormFromModel(Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
   quest
         .use(RING_OF_UI)
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
         // insertion(): maps model fields to UI inputs in a single call
         .insertion().insertData(purchaseForeignCurrency)
         .button().click(ButtonFields.CALCULATE_COST_BUTTON)
         .button().click(ButtonFields.PURCHASE_BUTTON)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use .insertion().insertData(model) to populate forms automatically
Model fields must be properly mapped to UI elements via annotations or configuration
Significantly reduces code verbosity for complex forms
Maintains type safety and validation
``
### **6. Custom Service Ring - Switching Between Services**
**Description:** Demonstrates using custom service rings to encapsulate domain-specific operations and switching between different rings.

```java
@Test
@Regression
@Description("Usage of custom service, and switching between different services")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void customServiceExample_switchBetweenServices(Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
   quest
         // Use a custom ring (service) exposing domain-specific fluent methods
         .use(RING_OF_PURCHASE_CURRENCY)
         .purchaseCurrency(purchaseForeignCurrency)
         // drop(): release current ring (service) before switching
         .drop()
         // Switch back to the default UI ring (service)
         .use(RING_OF_UI)
         .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
         .complete();
}
```
### Key Patterns:
``
Use .use(RING_NAME) to activate a custom service ring
Custom rings encapsulate complex multi-step workflows
Use .drop() to release the current ring before switching
Enables cleaner test code by hiding implementation details
``

### **7. Custom Service Ring - Complete Encapsulation**
**Description:** Shows a complete test scenario using only custom ring methods, with all UI interactions encapsulated within the custom service.

```java
@Test
@Regression
@Description("Perform the entire scenario via a custom ring (service) methods only")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void customServiceExample_usingOnlyCustomMethods(Quest quest,
      @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
   quest
         // Entire flow encapsulated by the custom ring (service), no generic UI calls
         .use(RING_OF_PURCHASE_CURRENCY)
         .purchaseCurrency(purchaseForeignCurrency)
         .validatePurchase()
         .complete();
}
```
### Key Patterns:
``
Encapsulate entire workflows in custom ring methods
Promotes high-level, business-readable test code
Reduces duplication across tests
Simplifies maintenance when UI changes occur
``

### **8. Single Journey Precondition**
**Description:** Uses @Journey annotation to execute a reusable precondition that sets up required application state before the test.

```java
@Test
@Regression
@Description("PreQuest with a single @Journey precondition to set required state")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
// @Journey: reusable precondition executed before the test
@Journey(value = PURCHASE_CURRENCY_PRECONDITION,
      journeyData = {@JourneyData(DataCreator.Data.PURCHASE_CURRENCY)})
void journey_singlePrecondition(Quest quest) {
   quest
         .use(RING_OF_PURCHASE_CURRENCY)
         .validatePurchase()
         .complete();
}
```
### Key Patterns:
``
Use @Journey to define reusable preconditions
Specify value pointing to the precondition implementation
Use @JourneyData to pass Craft models to the journey.
Journey executes before the test method
Test focuses only on validation, not setup
``

### **9. Multiple Journey Preconditions - Composition**
**Description:** Demonstrates composing multiple journey preconditions to create complex test setups with ordered execution.

```java
@Test
@Regression
@Description("PreQuest with multiple @Journey entries to compose preconditions, no JourneyData")
// Combine multiple journeys to compose the required preconditions
@Journey(value = USER_LOGIN_PRECONDITION)
@Journey(value = PURCHASE_CURRENCY_PRECONDITION,
      journeyData = {@JourneyData(DataCreator.Data.PURCHASE_CURRENCY)})
void multipleJourneys_combinedPreconditions_noJourneyData(Quest quest) {
   quest
         .use(RING_OF_PURCHASE_CURRENCY)
         .validatePurchase()
         .complete();
}
```
### Key Patterns:
``
Stack multiple @Journey annotations for sequential execution
Journeys execute in the order they are declared
Some journeys may require @JourneyData, others may not
Enables complex precondition composition without code duplication
``

### **9a. Text Validation in UI Fields**
**Description:** Demonstrates using the validate() component for verifying text presence in UI fields.
Supports both soft and hard assertions.

```java
@Test
@Smoke
@Regression
@Description("Component Covered: Validate using Soft Assertions")
void components_validateUsingSoftAssertions(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, "username")
         .input().insert(InputFields.PASSWORD_FIELD, "password")
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PAY_SAVED_PAYEE)
         .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_SPRINT)
         .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
         // validate(): offers fluent assertions for text presence/visibility in UI fields (soft or hard)
         .validate().validateTextInField(Tag.I, PAYEE_SPRINT_PLACEHOLDER, true)
         .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_BANK_OF_AMERICA)
         .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
         .validate().validateTextInField(Tag.I, PAYEE_BANK_OF_AMERICA_PLACEHOLDER, true)
         .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_APPLE)
         .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
         .validate().validateTextInField(Tag.I, PAYEE_APPLE_PLACEHOLDER, true)
         .complete();
}

@Test
@Smoke
@Regression
@Description("Component Covered: Validate using Hard Assertions")
void components_validateUsingHardAssertions(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, "username")
         .input().insert(InputFields.PASSWORD_FIELD, "password")
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .link().click(LinkFields.TRANSFER_FUNDS_LINK)
         .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
         .list().select(ListFields.PAY_BILLS_TABS, PAY_SAVED_PAYEE)
         .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_SPRINT)
         .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
         // validate(): when the soft-assert flag is omitted or false, assertions default to HARD mode
         .validate().validateTextInField(Tag.I, PAYEE_SPRINT_PLACEHOLDER)
         .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_BANK_OF_AMERICA)
         .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
         .validate().validateTextInField(Tag.I, PAYEE_BANK_OF_AMERICA_PLACEHOLDER, false)
         .complete();
}
```
### Key Patterns:
``
Use .validate().validateTextInField(tag, expectedText, soft) for text validation
Tag parameter specifies the HTML tag to search within (Tag.I, Tag.DIV, Tag.SPAN, etc.)
Third parameter (boolean) controls soft vs hard assertion mode
Soft assertions (true) continue test execution even if validation fails
Hard assertions (false or omitted) stop test execution immediately on failure
Useful for validating dynamic content, placeholders, and UI messages
``

### **10. Read Entire Table with Comprehensive Assertions**
**Description:** Demonstrates reading an entire table and validating it using various table assertion types including row counts, uniqueness, empty cells, and data matching.

```java
@Test
@Smoke
@Regression
@Description("Read entire table and validate using table assertion types")
void readEntireTable_validateWithAssertionTypes(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
         .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, FIND_TRANSACTIONS)
         .input().insert(InputFields.AA_DESCRIPTION_FIELD, TRANSACTION_DESCRIPTION_ONLINE)
         .input().insert(InputFields.AA_FROM_DATE_FIELD, TRANSACTION_FROM_DATE)
         .input().insert(InputFields.AA_TO_DATE_FIELD, TRANSACTION_TO_DATE)
         .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, TRANSACTION_AMOUNT_100)
         .input().insert(InputFields.AA_TO_AMOUNT_FIELD, TRANSACTION_AMOUNT_1000)
         .select().selectOption(SelectFields.AA_TYPE_DDL, TRANSACTION_TYPE_DEPOSIT)
         .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
         // table(): entry point for table component interactions (read/validate/click)
         // readTable(table): reads the entire table into the framework's storage for later assertions
         .table().readTable(Tables.FILTERED_TRANSACTIONS)
         // table().validate(): fluent assertions targeting table values/elements using TableAssertionTypes
         .table().validate(
               Tables.FILTERED_TRANSACTIONS,
               Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(2).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(EVERY_ROW_CONTAINS_VALUES).expected(List.of(ONLINE_TRANSFER_REFERENCE)).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(TABLE_DOES_NOT_CONTAIN_ROW).expected(ROW_VALUES_NOT_CONTAINED).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(ALL_ROWS_ARE_UNIQUE).expected(true).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(NO_EMPTY_CELLS).expected(false).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(COLUMN_VALUES_ARE_UNIQUE).expected(1).soft(true).build(),
               Assertion.builder().target(TABLE_VALUES).type(TABLE_DATA_MATCHES_EXPECTED).expected(ONLINE_TRANSFERS_EXPECTED_TABLE).soft(true).build(),
               Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_ENABLED).expected(true).soft(true).build(),
               Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_CLICKABLE).expected(true).soft(true).build())
         // readRow(): narrows context to a single row by index for row-level assertions
         .table().readRow(Tables.FILTERED_TRANSACTIONS, 1)
         .table().validate(
               Tables.FILTERED_TRANSACTIONS,
               Assertion.builder().target(ROW_VALUES).type(ROW_NOT_EMPTY).expected(true).soft(true).build(),
               Assertion.builder().target(ROW_VALUES).type(ROW_CONTAINS_VALUES).expected(List.of(TRANSFER_DATE_1, ONLINE_TRANSFER_REFERENCE)).soft(true).build())
         .complete();
}
```
### Key Patterns:
``
Use .table().readTable(Tables.TABLE_NAME) to read entire tables
Table data is stored in framework storage for assertions
Use TABLE_VALUES target for value-based assertions
Use TABLE_ELEMENTS target for element state assertions (enabled, clickable)
Use .readRow(table, index) to focus on specific rows
Combine table-level and row-level assertions for comprehensive validation
``

### **11. Read Table with Specific Columns**
**Description:** Shows how to read only specific columns from a table using TableField mappings and validate cell values.

```java
@Test
@Smoke
@Regression
@Description("Read table with specific columns and validate target cell value")
void readTableWithSpecifiedColumns_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
         .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, FIND_TRANSACTIONS)
         .list().validateIsSelected(ListFields.ACCOUNT_ACTIVITY_TABS, false, FIND_TRANSACTIONS)
         .input().insert(InputFields.AA_FROM_DATE_FIELD, TRANSACTION_FROM_DATE)
         .input().insert(InputFields.AA_TO_DATE_FIELD, TRANSACTION_TO_DATE)
         .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, TRANSACTION_AMOUNT_1)
         .input().insert(InputFields.AA_TO_AMOUNT_FIELD, TRANSACTION_AMOUNT_1000)
         .select().selectOption(SelectFields.AA_TYPE_DDL, TRANSACTION_TYPE_ANY)
         .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
         // readTable(table, columns...): reads specific columns from the table into the framework's storage
         .table().readTable(Tables.FILTERED_TRANSACTIONS, TableField.of(FilteredTransactionEntry::setDescription),
               TableField.of(FilteredTransactionEntry::setWithdrawal))
         // validate(): uses a lambda for arbitrary assertions when a built-in assertion type isn't suitable
         .validate(() -> Assertions.assertEquals(
               "50",
               retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, TRANSACTION_DESCRIPTION_OFFICE_SUPPLY),
                     FilteredTransactionEntry.class).getWithdrawal().getText(),
               "Wrong deposit value")
         )
         .complete();
}
```
### Key Patterns:
``
Use TableField.of(ModelClass::setter) to specify columns to read
Only specified columns are extracted and mapped
Improves performance when working with large tables
Use retrieve(tableRowExtractor(...)) to access specific rows from storage
Use .validate(() -> {}) for custom assertion logic
``

### **12. Read Table with Row Range**
**Description:** Demonstrates reading a subset of table rows using start and end indices.

```java
@Test
@Smoke
@Regression
@Description("Read table with start/end row range and validate target cell value")
void readTableWithRowRange_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // readTable(table, start, end): reads a subset of rows (inclusive indices)
         .table().readTable(Tables.OUTFLOW, 3, 5)
         .validate(() -> Assertions.assertEquals(
               "$375.55",
               retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                     OutFlow.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .readTable(table, startIndex, endIndex) for row range reading
Indices are inclusive (both start and end rows are included)
Useful for paginated tables or performance optimization
Reduces memory footprint when only a subset is needed
``

### **13. Read Table with Row Range and Specific Columns**
**Description:** Combines row range reading with specific column selection for maximum control over data extraction.

```java
@Test
@Smoke
@Regression
@Description("Read specific columns within row range and validate target cell value")
void readTableSpecificColumnsWithRowRange_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // readTable(table, start, end, columns...): subset of rows with specific mapped columns
         .table().readTable(Tables.OUTFLOW, 3, 5, TableField.of(OutFlow::setCategory),
               TableField.of(OutFlow::setAmount))
         .validate(() -> Assertions.assertEquals(
               "$375.55",
               retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                     OutFlow.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Combine row range and column selection for precise data extraction
Syntax: .readTable(table, start, end, TableField.of(...)...)
Maximizes performance for large tables
Reduces memory usage and processing time
``

### **14. Read Table Row by Search Criteria**
**Description:** Shows how to read a specific table row by providing search criteria values.

```java
@Test
@Smoke
@Regression
@Description("Read a table row by search criteria and validate target cell value")
void readTableRowBySearchCriteria_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // readRow(table, criteria): reads a row matching the provided search values
         .table().readRow(Tables.OUTFLOW, List.of(RETAIL))
         .validate(() -> Assertions.assertEquals(
               "$375.55",
               retrieve(tableRowExtractor(Tables.OUTFLOW),
                     OutFlow.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .readRow(table, List.of(searchValue...)) to find rows by criteria
Provide one or more values that must match in the row
Useful when you know specific identifying values
Returns the first matching row
``

### **15. Read Table Row with Specific Columns**
**Description:** Demonstrates reading a specific row by index with only selected columns.

```java
@Test
@Smoke
@Regression
@Description("Read a specific table row with specific columns and validate target cell value")
void readTableRowWithSpecifiedColumns_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // readRow(table, row, columns...): reads a specific row with only the specified columns
         .table().readRow(Tables.OUTFLOW, 1, TableField.of(OutFlow::setCategory),
               TableField.of(OutFlow::setAmount))
         .validate(() -> Assertions.assertEquals(
               "$160.00",
               retrieve(tableRowExtractor(Tables.OUTFLOW, TRANSPORTATION),
                     OutFlow.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .readRow(table, rowIndex, TableField.of(...)) for row-specific column reading
Combines row selection by index with column filtering
Optimizes performance when only certain fields are needed
``

### **16. Read Table Row by Criteria with Specific Columns**
**Description:** Combines search criteria with column selection for targeted data extraction.

```java
@Test
@Smoke
@Regression
@Description("Read a table row by search criteria with specified columns and validate target cell value")
void readTableRowByCriteriaWithSpecifiedColumns_validateCell(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // readRow(table, criteria, columns...): reads by search criteria and map the specified columns
         .table().readRow(Tables.OUTFLOW, List.of(RETAIL), TableField.of(OutFlow::setCategory),
               TableField.of(OutFlow::setAmount))
         .validate(() -> Assertions.assertEquals(
               "$375.55",
               retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                     OutFlow.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .readRow(table, criteria, columns...) to combine search with column selection
Most precise way to extract specific data from tables
Improves both performance and code clarity
``

### **17. Click Element Inside Table Cell - By Row Index**
**Description:** Shows how to click on elements (links, buttons) inside table cells using row index and TableField mapping.

```java
@Test
@Smoke
@Regression
@Description("Click a link inside a cell found by row using cell insertion interface")
void clickLinkInCertainCell_usingCellInsertion(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.ACCOUNT_SUMMARY_LINK)
         // clickElementInCell(table, row, column): clicks an element inside a cell resolved by TableField mapping
         .table().clickElementInCell(Tables.CREDIT_ACCOUNTS, 1, TableField.of(CreditAccounts::setAccount))
         .table().readTable(Tables.ALL_TRANSACTIONS)
         .validate(() -> Assertions.assertEquals(
               "99.6",
               retrieve(tableRowExtractor(Tables.ALL_TRANSACTIONS, TRANSACTION_DESCRIPTION_TELECOM),
                     AllTransactionEntry.class).getWithdrawal().getText(),
               "Wrong Balance")
         )
         .complete();
}
```
### Key Patterns:
``
Use .clickElementInCell(table, rowIndex, TableField.of(...)) to click elements in cells
Useful for clicking links, buttons, or other interactive elements within tables
Row specified by zero-based index
Column specified by TableField mapping to model setter
``

### **18. Click Element Inside Table Cell - By Search Criteria**
**Description:** Demonstrates clicking elements in cells by first locating the row using search criteria.

```java
@Test
@Smoke
@Regression
@Description("Click a button inside a cell found by search criteria using custom insertion interface")
void clickButtonInCellFoundByCriteria_usingCustomInsertion(Quest quest) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // clickElementInCell(table, criteria, column): locates the row by criteria and click inside the mapped column
         .table().clickElementInCell(Tables.OUTFLOW, List.of(CHECKS_WRITTEN), TableField.of(OutFlow::setDetails))
         .table().readTable(Tables.DETAILED_REPORT)
         .validate(() -> Assertions.assertEquals(
               "$105.00",
               retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                     DetailedReport.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .clickElementInCell(table, List.of(criteria), TableField.of(...)) for criteria-based clicking
Locates row by matching criteria values
More flexible than row index when row position may vary
Useful for dynamic tables where row order changes
``

### **19. Click Element Inside Table Cell - Using Data Object**
**Description:** Shows how to use a crafted data model to specify which element to click in a table cell.

```java
@Test
@Smoke
@Regression
@Description("Click a button inside a cell found by row using data object")
void clickButtonInCertainCell_usingDataObject(
      Quest quest,
      @Craft(model = DataCreator.Data.OUTFLOW_DATA) OutFlow outFlowDetails) {
   quest
         .use(RING_OF_UI)
         .browser().navigate(getUiConfig().baseUrl())
         .button().click(ButtonFields.SIGN_IN_BUTTON)
         .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
         .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
         .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
         .browser().back()
         .button().click(ButtonFields.MORE_SERVICES_BUTTON)
         .link().click(LinkFields.MY_MONEY_MAP_LINK)
         // clickElementInCell(table, row, model): locates the row, clicks an element inside a cell
         // resolved by the data model
         .table().clickElementInCell(Tables.OUTFLOW, 4, outFlowDetails)
         .table().readTable(Tables.DETAILED_REPORT)
         .validate(() -> Assertions.assertEquals(
               "$105.00",
               retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                     DetailedReport.class).getAmount().getText(),
               "Wrong Amount")
         )
         .complete();
}
```
### Key Patterns:
``
Use .clickElementInCell(table, row, dataModel) with crafted models
Data model contains mapping logic for cell interactions
Useful for complex cell structures with multiple clickable elements
Promotes reusability of interaction patterns
``

### Core Framework Patterns - DO's
**Request Execution & Navigation**
✅ DO use RING_OF_UI for standard UI operations
✅ DO chain UI component interactions fluently
✅ DO always call .complete() at the end of the quest chain
✅ DO use getUiConfig().baseUrl() for environment-specific URLs
✅ DO use .use(RING_NAME) to activate service rings

**Authentication**
✅ DO use @AuthenticateViaUi for automatic UI authentication
✅ DO define credentials in dedicated credential classes
✅ DO implement authentication logic in separate login classes
✅ DO use Data.testData() for configuration-driven credentials

**Test Data Management**
✅ DO use @Craft annotations for test data model injection
✅ DO define models in DataCreator.Data enum
✅ DO use descriptive model names
✅ DO use Late<@Craft> for lazy initialization when needed
✅ DO store test constants in dedicated constant classes

**Form Population**
✅ DO use .insertion().insertData(model) for automatic form population
✅ DO map model fields to UI elements properly
✅ DO validate insertion mappings in model classes

**Table Operations**
✅ DO use .table().readTable(table) to read entire tables
✅ DO use .table().readRow(table, index) for specific rows
✅ DO use TableField.of(ModelClass::setter) for column selection
✅ DO use row ranges for performance optimization
✅ DO use search criteria when you know identifying values
✅ DO validate tables using TABLE_VALUES and TABLE_ELEMENTS targets

**Custom Service Rings**
✅ DO create custom rings for domain-specific workflows
✅ DO encapsulate complex multi-step operations in ring methods
✅ DO use .drop() to release rings before switching
✅ DO name custom ring methods clearly and business-focused

**Journey Preconditions**
✅ DO use @Journey for reusable preconditions
✅ DO pass data to journeys via @JourneyData
✅ DO stack multiple @Journey annotations for composition
✅ DO use order parameter when execution sequence matters
✅ DO keep journey logic independent and reusable
✅ DO access journey data from PRE_ARGUMENTS storage

**Advanced Features**
✅ DO use @DB annotation when tests need database operations
✅ DO use @DbHook for database initialization/cleanup
✅ DO use @Ripper for automatic test data cleanup
✅ DO use @StaticTestData to preload reference data
✅ DO use @InterceptRequests to validate network traffic
✅ DO use Late<> for expensive or conditional model creation
✅ DO combine @UI, @API, and @DB for full-stack testing
✅ DO use retrieve() to access stored data from previous steps
✅ DO use DefaultStorage.retrieve() for explicit storage access

**Assertions & Validation**
✅ DO use soft assertions with .soft(true) for multiple related checks
✅ DO use .validate(() -> {}) for custom assertion logic
✅ DO use retrieve(tableRowExtractor(...)) to access table data from storage
✅ DO provide descriptive error messages in assertions

**Test Organization**
✅ DO use @Description for test documentation
✅ DO use @DisplayName at class level for test organization
✅ DO apply appropriate tags (@Smoke, @Regression)
✅ DO extend BaseQuest for all UI test classes
✅ DO mark test classes with @UI annotation

### Core Framework Patterns - DON'Ts
**Request Execution & Navigation**
❌ DON'T use Selenium WebDriver directly; always use the quest DSL
❌ DON'T forget to call .complete() at the end
❌ DON'T hardcode URLs; use configuration
❌ DON'T activate multiple rings without dropping

**Authentication**
❌ DON'T hardcode credentials in test methods
❌ DON'T share authentication state across unrelated tests
❌ DON'T perform manual login when @AuthenticateViaUi can handle it
❌ DON'T skip authentication for secured pages

**Test Data Management**
❌ DON'T create test data inline in test methods
❌ DON'T use Late<> for models that don't need lazy initialization
❌ DON'T create duplicate Craft models
❌ DON'T hardcode magic strings or numbers
❌ DON'T modify Craft models after injection

**Form Population**
❌ DON'T manually insert each field when .insertion() can handle it
❌ DON'T create models without proper field mappings
❌ DON'T use insertion service for single-field forms

**Table Operations**
❌ DON'T read entire tables when you only need specific rows
❌ DON'T read all columns when only specific ones are needed
❌ DON'T use generic WebElement access; use table component methods
❌ DON'T create table models without proper field mappings
❌ DON'T ignore table assertion types; use them instead of custom logic

**Custom Service Rings**
❌ DON'T create custom rings for single-use operations
❌ DON'T create circular dependencies between ring methods
❌ DON'T mix generic UI calls with custom ring methods
❌ DON'T bypass the ring system with direct Selenium calls

**Journey Preconditions**
❌ DON'T perform manual setup when Journey can handle it
❌ DON'T create data dependencies between unrelated tests
❌ DON'T duplicate journey logic across tests
❌ DON'T make journeys test-specific; keep them reusable
❌ DON'T forget to specify order when journey sequence matters

**Advanced Features**
❌ DON'T use @DB without proper database configuration
❌ DON'T run database operations without @DbHook initialization
❌ DON'T leave test data without cleanup (@Ripper or manual cleanup)
❌ DON'T call .get() on Late<> models multiple times unnecessarily
❌ DON'T use StaticTestData for test-specific dynamic data
❌ DON'T forget to .drop() rings before switching to another ring
❌ DON'T mix storage keys; use descriptive unique keys
❌ DON'T access storage data that hasn't been stored yet

**Assertions & Validation**
❌ DON'T use generic exception messages
❌ DON'T mix JUnit assertions with framework assertions unnecessarily
❌ DON'T skip validation steps
❌ DON'T create overly complex validation logic

**Test Organization**
❌ DON'T create overly long test methods (keep under 40 lines)
❌ DON'T test multiple unrelated scenarios in one test
❌ DON'T skip test documentation
❌ DON'T use unclear test method names
❌ DON'T mix UI and API testing logic in the same test class

### Available UI Components
| Component | Entry Method | Common Operations |
|-----------|--------------|-------------------|
| Browser | `.browser()` | navigate, back, forward, refresh, switchTab |
| Button | `.button()` | click, doubleClick, rightClick, validateIsEnabled, validateIsVisible |
| Input | `.input()` | insert, clear, getValue, validateIsEnabled |
| Link | `.link()` | click, getHref, validateIsEnabled |
| Select | `.select()` | selectOption, getSelectedOption, getAvailableOptions |
| Radio | `.radio()` | select, validateIsSelected |
| Checkbox | `.checkbox()` | check, uncheck, validateIsChecked, validateIsEnabled, validateIsSelected |
| Alert | `.alert()` | validateValue (with optional soft assertion), accept, dismiss, getText |
| List | `.list()` | select, validateIsSelected |
| Table | `.table()` | readTable, readRow, validate, clickElementInCell |
| Insertion | `.insertion()` | insertData (populates forms from models) |
| Validate | `.validate()` | validateTextInField (soft/hard), validate (custom lambda) |

### Available Table Assertion Types
| Assertion Type | Description | Target |
|----------------|-------------|--------|
| `TABLE_NOT_EMPTY` | Validates table contains data | `TABLE_VALUES` |
| `TABLE_ROW_COUNT` | Validates exact row count | `TABLE_VALUES` |
| `EVERY_ROW_CONTAINS_VALUES` | Validates all rows contain specific values | `TABLE_VALUES` |
| `TABLE_DOES_NOT_CONTAIN_ROW` | Validates row is not present | `TABLE_VALUES` |
| `ALL_ROWS_ARE_UNIQUE` | Validates no duplicate rows | `TABLE_VALUES` |
| `NO_EMPTY_CELLS` | Validates all cells have values | `TABLE_VALUES` |
| `COLUMN_VALUES_ARE_UNIQUE` | Validates column has unique values | `TABLE_VALUES` |
| `TABLE_DATA_MATCHES_EXPECTED` | Validates entire table data | `TABLE_VALUES` |
| `ALL_CELLS_ENABLED` | Validates all cells are enabled | `TABLE_ELEMENTS` |
| `ALL_CELLS_CLICKABLE` | Validates all cells are clickable | `TABLE_ELEMENTS` |
| `ROW_NOT_EMPTY` | Validates row contains data | `ROW_VALUES` |
| `ROW_CONTAINS_VALUES` | Validates row contains specific values | `ROW_VALUES` |

## **Advanced Features**

### **20. Database Integration - @DB and @DbHook**
**Description:** Demonstrates integrating database operations with UI tests using @DB annotation and database hooks.

```java
@UI
@DB
@API
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@DisplayName("Database Integration Examples")
class DatabaseIntegrationTest extends BaseQuest {

   @Test
   @Smoke
   @Regression
   @Description("Validate database state during UI test execution")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void validateDatabaseDuringTest(Quest quest,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .drop()
            .use(RING_OF_DB)
            .requestAndValidate(
                  AppQueries.QUERY_ORDER.withParam(order.getOrderId()),
                  Assertion.builder().target(RESULT_SET_SIZE).type(IS).expected(1).build())
            .complete();
   }
}
```
### Key Patterns:
``
Use @DB annotation to enable database operations
Use @DbHook to run setup/teardown database scripts
when = BEFORE runs before test class, when = AFTER runs after
Combine @UI, @DB, and @API for full stack testing
Use RING_OF_DB to execute database queries and validations
``

### **21. Data Cleanup - @Ripper**
**Description:** Uses @Ripper annotation to automatically clean up test data after test execution.

```java
@Test
@Smoke
@Regression
@Description("Cleanup test data after execution using Ripper")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
@Ripper(DataCleaner.Data.ORDER_CLEANER)
void testWithAutomaticCleanup(Quest quest,
      @Craft(model = DataCreator.Data.ORDER) Order order) {
   quest
         .use(RING_OF_CUSTOM)
         .createOrder(order)
         .validateOrder(order)
         // @Ripper automatically cleans up order data after test completion
         .complete();
}
```
### Key Patterns:
``
Use @Ripper to specify cleanup logic for test data
Cleaner executes after test completes (success or failure)
Prevents data pollution between tests
Useful for maintaining test independence
Cleaner receives test context and can access crafted models
``

### **22. Static Test Data - @StaticTestData**
**Description:** Demonstrates preloading static test data before test execution for better performance.

```java
@Test
@Smoke
@Regression
@Description("Static Data with Storage feature: Retrieve storage data preloaded as static data before test execution")
@Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
@StaticTestData(StaticData.class)
void staticTestDataFeatureUsingDataFromStorage(Quest quest) {
   quest
         .use(RING_OF_CUSTOM)
         .validateOrder(retrieve(staticTestData(StaticData.ORDER), Order.class))
         .complete();
}
```
### Key Patterns:
``
Use @StaticTestData to preload data before test execution
Data is loaded once and cached for the test
Access via retrieve(staticTestData(key), Class)
Improves performance by avoiding repeated data creation
Useful for reference data that doesn't change between tests
``

### **23. Request Interception - @InterceptRequests**
**Description:** Shows how to intercept and validate network requests made during UI interactions.

```java
@Test
@Smoke
@Regression
@Description("Request Interceptors feature: Attach a network request interceptor to capture and extract data")
@InterceptRequests(RequestsInterceptor.class)
@Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
void requestInterceptorsFeatureWithCustomExtraction(
      Quest quest,
      @Craft(model = DataCreator.Data.ORDER) Order order) {
   quest
         .use(RING_OF_CUSTOM)
         .createOrder(order)
         .drop()
         .use(RING_OF_API)
         // Validate intercepted request data
         .requestAndValidate(
               retrieve(INTERCEPTED_DATA, DataExtractorFunctions.ORDER_API_ENDPOINT, Endpoint.class)
                     .withHeader("Cookie", getJsessionCookie()),
               Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
         .complete();
}
```
### Key Patterns:
``
Use @InterceptRequests to attach network request interceptor
Interceptor captures browser network traffic during test execution
Extract request data using custom extractor functions
Useful for validating API calls triggered by UI interactions
Combines UI and API validation in single test
``

### **24. Late/Lazy Initialization - Late<>**
**Description:** Demonstrates lazy initialization of crafted models using Late<> wrapper.

```java
@Test
@Smoke
@Regression
@Description("Late initialization of crafted model data")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void lateInitializationFeature(
      Quest quest,
      @Craft(model = DataCreator.Data.ORDER) Late<Order> lazyOrder) {
   quest
         .use(RING_OF_CUSTOM)
         // Model is created only when get() is called
         .createOrder(lazyOrder.get())
         .validateOrder(lazyOrder.get())
         .complete();
}
```
### Key Patterns:
``
Use Late<ModelClass> to defer model creation
Model is created only when .get() is first called
Useful when model creation is expensive or conditional
Maintains lazy evaluation semantics
Access actual model via .get() method
``

### **25. Storage and Data Extraction**
**Description:** Shows how to use framework storage to capture and retrieve data during test execution.

```java
@Test
@Smoke
@Regression
@Description("Storage feature: Retrieve storage data captured during test UI steps")
void storageFeatureUsingDataFromFluentSteps(Quest quest,
      @Craft(model = DataCreator.Data.SELLER) Seller seller) {
   quest
         .use(RING_OF_CUSTOM)
         .login(seller)
         .drop()
         .use(RING_OF_UI)
         .button().click(ButtonFields.NEW_ORDER_BUTTON)
         // getAvailableOptions stores dropdown options in framework storage
         .select().getAvailableOptions(SelectFields.LOCATION_DDL)
         // Retrieve stored data using DefaultStorage
         .validate(() -> Assertions.assertEquals(
               2,
               DefaultStorage.retrieve(SelectFields.LOCATION_DDL, List.class).size()))
         .validate(() -> Assertions.assertIterableEquals(
               List.of("Store", "Bakery"),
               DefaultStorage.retrieve(SelectFields.LOCATION_DDL, List.class)))
         .complete();
}
```
### Key Patterns:
``
Framework automatically stores certain data during test execution
Access stored data via retrieve(key, Class) or DefaultStorage.retrieve()
Useful for validating captured data or passing data between test steps
Table data is automatically stored when using .readTable() or .readRow()
Journey precondition data is stored in PRE_ARGUMENTS storage key
``

### **26. Multi-Ring Integration - UI + API + DB**
**Description:** Demonstrates switching between multiple service rings in a single test.

```java
@Test
@Smoke
@Regression
@Description("Integration of UI, API, and Database rings in single test")
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
void multiRingIntegration(Quest quest,
      @Craft(model = DataCreator.Data.ORDER) Order order) {
   quest
         // UI operations
         .use(RING_OF_CUSTOM)
         .createOrder(order)
         .drop()
         // API validation
         .use(RING_OF_API)
         .requestAndValidate(
               ENDPOINT_BAKERY.withHeader("Cookie", getJsessionCookie()),
               Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
         .drop()
         // Database validation
         .use(RING_OF_DB)
         .requestAndValidate(
               AppQueries.QUERY_ORDER.withParam(order.getOrderId()),
               Assertion.builder().target(RESULT_SET_SIZE).type(IS).expected(1).build())
         .complete();
}
```
### Key Patterns:
``
Use .use(RING_NAME) to activate a service ring
Use .drop() to release current ring before switching
Can switch between UI, API, DB, and custom rings in same test
Enables comprehensive end-to-end validation
Validates consistency across UI, API, and database layers
``

### Table Reading Patterns
**Read Entire Table**

```
.table().readTable(Tables.TABLE_NAME)
```

**Read Specific Columns**
```
.table().readTable(Tables.TABLE_NAME, 
    TableField.of(Model::setField1),
    TableField.of(Model::setField2))
```

**Read Row Range**
```
.table().readTable(Tables.TABLE_NAME, startIndex, endIndex)
```

**Read Row Range with Columns**
```
.table().readTable(Tables.TABLE_NAME, startIndex, endIndex,
    TableField.of(Model::setField1),
    TableField.of(Model::setField2))
```

**Read Specific Row by Index**
```
.table().readRow(Tables.TABLE_NAME, rowIndex)
```

**Read Row by Search Criteria**
```
.table().readRow(Tables.TABLE_NAME, List.of(searchValue1, searchValue2))
```

**Retrieve Table Data from Storage**
```
retrieve(tableRowExtractor(Tables.TABLE_NAME, searchCriteria), ModelClass.class)
```

## **Annotation Reference**

### Test-Level Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Test` | Marks method as a test | `@Test` |
| `@DisplayName` | Provides readable test class name | `@DisplayName("User Login Tests")` |
| `@Description` | Documents test purpose for Allure | `@Description("Validates successful login flow")` |
| `@Smoke` | Tags test as smoke test | `@Smoke` |
| `@Regression` | Tags test as regression test | `@Regression` |

### Class-Level Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@UI` | Enables UI testing capabilities | `@UI` |
| `@API` | Enables API testing capabilities | `@API` |
| `@DB` | Enables database operations | `@DB` |
| `@DbHook` | Runs database scripts before/after tests | `@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)` |

### Data Management Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Craft` | Injects crafted test data model | `@Craft(model = DataCreator.Data.ORDER) Order order` |
| `@StaticTestData` | Preloads static test data | `@StaticTestData(StaticData.class)` |

### Authentication Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@AuthenticateViaUi` | Automatic UI login before test | `@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)` |
| `@AuthenticateViaUi` (cached) | Login once, reuse session | `@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)` |

### Precondition Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Journey` | Executes reusable precondition | `@Journey(value = Preconditions.Data.LOGIN)` |
| `@Journey` (with data) | Precondition with injected data | `@Journey(value = Preconditions.Data.ORDER, journeyData = {@JourneyData(DataCreator.Data.ORDER)})` |
| `@Journey` (ordered) | Multiple journeys with execution order | `@Journey(value = Preconditions.Data.LOGIN, order = 1)` |

### Cleanup Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Ripper` | Automatic test data cleanup | `@Ripper(DataCleaner.Data.ORDER_CLEANER)` |

### Network Annotations
| Annotation | Purpose | Example |
|------------|---------|---------|
| `@InterceptRequests` | Intercepts browser network requests | `@InterceptRequests(RequestsInterceptor.class)` |

### Parameter Wrappers
| Wrapper | Purpose | Example |
|---------|---------|---------|
| `Late<>` | Lazy/deferred model initialization | `@Craft(model = DataCreator.Data.ORDER) Late<Order> order` |

## **Storage Keys Reference**

### Common Storage Keys
| Storage Key | Purpose | Access Method |
|-------------|---------|---------------|
| `PRE_ARGUMENTS` | Journey precondition data | `retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class)` |
| `INTERCEPTED_DATA` | Intercepted network request data | `retrieve(INTERCEPTED_DATA, extractor, Class.class)` |
| `staticTestData(key)` | Static preloaded test data | `retrieve(staticTestData(StaticData.ORDER), Order.class)` |
| `tableRowExtractor(table, criteria)` | Table row data | `retrieve(tableRowExtractor(Tables.TABLE_NAME, criteria), ModelClass.class)` |
| `SelectFields.FIELD_NAME` | Dropdown options from getAvailableOptions | `DefaultStorage.retrieve(SelectFields.LOCATION_DDL, List.class)` |

## **Ring (Service) Reference**

### Available Rings
| Ring | Purpose | Entry Point |
|------|---------|-------------|
| `RING_OF_UI` | Standard UI operations | `.use(RING_OF_UI)` |
| `RING_OF_API` | API operations | `.use(RING_OF_API)` |
| `RING_OF_DB` | Database operations | `.use(RING_OF_DB)` |
| `RING_OF_CUSTOM` | Custom domain-specific operations | `.use(RING_OF_CUSTOM)` |

### Ring Management
| Operation | Purpose | Example |
|-----------|---------|---------|
| `.use(RING)` | Activate a ring | `.use(RING_OF_UI)` |
| `.drop()` | Release current ring | `.drop()` |
| `.complete()` | Finish quest (with or without drop) | `.complete()` |

## **Quick Start Checklist**

### Basic UI Test
- [ ] Extend `BaseQuest`
- [ ] Add `@UI` annotation to class
- [ ] Add `@Test` annotation to method
- [ ] Import `RING_OF_UI` and `getUiConfig()`
- [ ] Import UI element fields (ButtonFields, InputFields, etc.)
- [ ] Import constants for test data
- [ ] Use `.use(RING_OF_UI)` to start
- [ ] Chain UI operations
- [ ] Add validation (alert, validate, or assertions)
- [ ] Call `.complete()`

### Test with Authentication
- [ ] All basic UI test items above
- [ ] Create credentials class (implements UiCredentials)
- [ ] Create login class (implements UiAuthentication)
- [ ] Add `@AuthenticateViaUi` to test method
- [ ] Add `cacheCredentials = true` if reusing session
- [ ] Remove manual login steps from test

### Test with Data Crafting
- [ ] All basic UI test items above
- [ ] Define model in DataCreator.Data enum
- [ ] Implement creator method in DataCreator
- [ ] Add `@Craft` parameter to test method
- [ ] Use model fields in test steps

### Test with Insertion Service
- [ ] All test with data crafting items above
- [ ] Annotate model fields with `@UiField`
- [ ] Map fields to UI elements
- [ ] Use `.insertion().insertData(model)` instead of manual inserts

### Test with Journeys
- [ ] Define journey in Preconditions.Data enum
- [ ] Implement journey logic in Preconditions class
- [ ] Add `@Journey` annotation to test
- [ ] Add `@JourneyData` if journey needs data
- [ ] Access journey data via `PRE_ARGUMENTS` if needed

### Table Test
- [ ] All basic UI test items above
- [ ] Define table in Tables class
- [ ] Create table model class with setters
- [ ] Import `TableField`, `tableRowExtractor`
- [ ] Import table assertion types and targets
- [ ] Use `.table().readTable()` or `.table().readRow()`
- [ ] Use `.table().validate()` or custom assertions
- [ ] Access data via `retrieve(tableRowExtractor(...))`

### Full-Stack Test
- [ ] Add `@UI`, `@API`, `@DB` annotations
- [ ] Add `@DbHook` if database setup needed
- [ ] Import all required rings
- [ ] Use `.use(RING)` to switch between layers
- [ ] Use `.drop()` before switching rings
- [ ] Validate consistency across layers