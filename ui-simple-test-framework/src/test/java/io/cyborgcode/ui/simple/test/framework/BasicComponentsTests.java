package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.simple.test.framework.ui.elements.AlertFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ListFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.RadioFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields;
import io.qameta.allure.Description;
import javax.swing.text.html.HTML.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

/**
 * Demonstrates core UI component interactions in the ROA UI framework.
 *
 * <p>This test class contains examples for the following components:
 * Button, Input, Link, Select, Alert, List, and Radio. It also demonstrates
 * the Browser service for navigation and the Validate service
 * for fluent assertions of text presence in UI fields.
 *
 * <p>Use these scenarios as reference implementations for composing fluent,
 * readable UI tests that focus on business intent while reusing common
 * navigation and validation primitives.
 *
 * <p>Examples target the Zero Bank demo app (via {@code getUiConfig().baseUrl()}),
 * but the patterns are app-agnostic and reusable across projects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DisplayName("Core Components: Fundamentals and Usage Examples")
class BasicComponentsTests extends BaseQuest {

   @Test()
   @Description("Components Covered: Browser, Button, Input, Link, Select, Alert")
   @Regression
   void components_browserButtonInputLinkSelectAlert(Quest quest) {
      quest
            // use() activates a UI ring (service bundle) for fluent interactions in this quest
            .use(RING_OF_UI)
            // browser() provides high-level navigation utilities (navigate, back, refresh, etc.)
            .browser().navigate(getUiConfig().baseUrl())
            // button() exposes fluent actions for clickable button elements
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            // input() handles typing and clearing text inputs and textarea
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            // link() encapsulates interactions with anchor/link elements
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            // select() provides selection by visible text/value/index for dropdowns
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Credit Card(Avail. balance = $ -265)")
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Loan(Avail. balance = $ 780)")
            .input().insert(InputFields.AMOUNT_FIELD, "100")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, "Transfer Amount")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            // alert() validates UI alert/toast/banner messages
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
            // drop() releases the current ring/service context when you are done with it
            .drop()
            // complete() finalizes the quest; can be called with or without a prior drop()
            .complete();
   }

   @Test
   @Description("Components Covered: Browser, Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   void components_listRadio(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            // list() targets grouped items (tabs/menus/collections) for selection by label
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Purchase Foreign Currency")
            .select().selectOption(SelectFields.PC_CURRENCY_DDL, "Mexico (peso)")
            .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
            // radio() selects a radio option by its field identifier
            .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
            .button().click(ButtonFields.CALCULATE_COST_BUTTON)
            .button().click(ButtonFields.PURCHASE_BUTTON)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
            .drop()
            .complete();
   }

   @Test
   @Description("Component Covered: Validate using Soft Assertions")
   @Regression
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
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            // validate() offers fluent assertions for text presence/visibility in UI fields (soft or hard)
            .validate().validateTextInField(Tag.I, "For 12119415161214 Sprint account", true)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 47844181491040 Bank of America account", true)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 48944145651315 Apple account", true)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account", true)
            .select().selectOption(SelectFields.SP_ACCOUNT_DDL, "Checking")
            .input().insert(InputFields.SP_AMOUNT_FIELD, "1000")
            .input().insert(InputFields.SP_DATE_FIELD, "2025-01-27")
            .input().insert(InputFields.SP_DESCRIPTION_FIELD, "Description Example")
            .button().click(ButtonFields.PAY_BUTTON)
            .alert().validateValue(AlertFields.PAYMENT_MESSAGE, "The payment was successfully submitted.", true)
            .complete();
   }

   @Test
   @Description("Component Covered: Validate using Hard Assertions")
   @Regression
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
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            // validate(): when the soft-assert flag is omitted, assertions default to HARD mode
            .validate().validateTextInField(Tag.I, "For 12119415161214 Sprint account")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 47844181491040 Bank of America account", false)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 48944145651315 Apple account")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account", false)
            .complete();
   }

   @Test
   @Description("Component Covered: Validate using mixed assertions")
   @Regression
   void components_validateUsingMixedAssertions(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 12119415161214 Sprint account")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 47844181491040 Bank of America account", false)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 48944145651315 Apple account", true)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account")
            .complete();
   }

}
