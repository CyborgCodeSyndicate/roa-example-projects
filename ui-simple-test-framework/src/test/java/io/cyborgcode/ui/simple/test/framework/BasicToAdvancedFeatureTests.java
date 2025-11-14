package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.simple.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.simple.test.framework.data.test_data.Data;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.ui.simple.test.framework.ui.elements.AlertFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ListFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.RadioFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_PURCHASE_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.PAY_BILLS;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.PURCHASE_FOREIGN_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Purchase.CURRENCY_PESO;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Purchase.SUCCESSFUL_PURCHASE_MESSAGE;
import static io.cyborgcode.ui.simple.test.framework.preconditions.Preconditions.Data.PURCHASE_CURRENCY_PRECONDITION;
import static io.cyborgcode.ui.simple.test.framework.preconditions.Preconditions.Data.USER_LOGIN_PRECONDITION;

/**
 * Demonstrates a progressive evolution of UI testing features, moving from basic flows
 * to advanced capabilities in the ROA UI framework.
 *
 * <p>Each test introduces a new feature (configuration-driven data, UI authentication,
 * data crafting/injection, insertion service, custom service rings, and reusable
 * preconditions via journeys). The goal is to show how scenarios scale in sophistication
 * without repeating setup or boilerplate.
 *
 * <p>Use this class as a learning path: start with the simplest test and continue
 * in order to see each feature in isolation and then in combination.
 *
 * <p>Examples target the Zero Bank demo app (via {@code getUiConfig().baseUrl()}),
 * but the patterns are app-agnostic and reusable across projects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DisplayName("Progressive UI Features: Basic to Advanced")
class BasicToAdvancedFeatureTests extends BaseQuest {

   @Test
   @Description("Baseline simple flow without advanced framework features")
   @Regression
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

   @Test
   @Description("Retrieves Login credentials from configuration properties")
   @Regression
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

   @Test
   @Description("AuthenticateViaUi performs login per test without session caching")
   @Regression
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

   @Test
   @Description("Craft injects a typed model instance for data-driven steps")
   @Regression
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

   @Test
   @Description("Insertion service maps model fields to UI controls in one operation")
   @Regression
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

   @Test
   @Description("Usage of custom service, and switching between different services")
   @Regression
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

   @Test
   @Description("Perform the entire scenario via a custom ring (service) methods only")
   @Regression
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

   @Test
   @Description("PreQuest with a single @Journey precondition to set required state")
   @Regression
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

   @Test
   @Description("PreQuest with multiple @Journey entries to compose preconditions, no JourneyData")
   @Regression
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

}
