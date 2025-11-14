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
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.PAY_BILLS;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.PAY_SAVED_PAYEE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.PURCHASE_FOREIGN_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_AMOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_APPLE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_APPLE_PLACEHOLDER;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_BANK_OF_AMERICA;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_BANK_OF_AMERICA_PLACEHOLDER;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_CHECKING_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_DATE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_DESCRIPTION;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_SPRINT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_SPRINT_PLACEHOLDER;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_WELLS_FARGO;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.PAYEE_WELLS_FARGO_PLACEHOLDER;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.PayBills.SUCCESSFUL_PAYMENT_MESSAGE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Purchase.CURRENCY_PESO;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Purchase.SUCCESSFUL_PURCHASE_MESSAGE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.CREDIT_CARD_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.LOAN_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.SUCCESSFUL_TRANSFER_MESSAGE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.TRANSFER_LOAN_TO_CREDIT_CARD;

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
class GettingStartedTests extends BaseQuest {

   @Test()
   @Description("Components Covered: Browser, Button, Input, Link, Select, Alert")
   @Regression
   void components_browserButtonInputLinkSelectAlert(Quest quest) {
      quest
            // use(): activates a UI ring (service bundle) for fluent interactions in this quest
            .use(RING_OF_UI)
            // browser(): provides high-level navigation utilities (navigate, back, refresh, etc.)
            .browser().navigate(getUiConfig().baseUrl())
            // button(): exposes fluent actions for clickable button elements
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            // input(): handles typing and clearing text inputs and textarea
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            // link(): encapsulates interactions with anchor/link elements
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            // select(): provides selection by visible text/value/index for dropdowns
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, LOAN_ACCOUNT)
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, CREDIT_CARD_ACCOUNT)
            .input().insert(InputFields.AMOUNT_FIELD, "100")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, TRANSFER_LOAN_TO_CREDIT_CARD)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            // alert(): validates UI alert/toast/banner messages
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, SUCCESSFUL_TRANSFER_MESSAGE)
            // drop(): releases the current ring/service context when you are done with it
            .drop()
            // complete(): finalizes the quest; can be called with or without a prior drop()
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
            // list(): targets grouped items (tabs/menus/collections) for selection by label
            .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
            .list().select(ListFields.PAY_BILLS_TABS, PURCHASE_FOREIGN_CURRENCY)
            .select().selectOption(SelectFields.PC_CURRENCY_DDL, CURRENCY_PESO)
            .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
            // radio(): selects a radio option by its field identifier
            .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
            .button().click(ButtonFields.CALCULATE_COST_BUTTON)
            .button().click(ButtonFields.PURCHASE_BUTTON)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, SUCCESSFUL_PURCHASE_MESSAGE)
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
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_WELLS_FARGO)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_WELLS_FARGO_PLACEHOLDER, true)
            .select().selectOption(SelectFields.SP_ACCOUNT_DDL, PAYEE_CHECKING_ACCOUNT)
            .input().insert(InputFields.SP_AMOUNT_FIELD, PAYEE_AMOUNT)
            .input().insert(InputFields.SP_DATE_FIELD, PAYEE_DATE)
            .input().insert(InputFields.SP_DESCRIPTION_FIELD, PAYEE_DESCRIPTION)
            .button().click(ButtonFields.PAY_BUTTON)
            .alert().validateValue(AlertFields.PAYMENT_MESSAGE, SUCCESSFUL_PAYMENT_MESSAGE, true)
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
            .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
            .list().select(ListFields.PAY_BILLS_TABS, PAY_SAVED_PAYEE)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_SPRINT)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            // validate(): when the soft-assert flag is omitted, assertions default to HARD mode
            .validate().validateTextInField(Tag.I, PAYEE_SPRINT_PLACEHOLDER)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_BANK_OF_AMERICA)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_BANK_OF_AMERICA_PLACEHOLDER, false)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_APPLE)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_APPLE_PLACEHOLDER)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_WELLS_FARGO)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_WELLS_FARGO_PLACEHOLDER, false)
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
            .list().select(ListFields.NAVIGATION_TABS, PAY_BILLS)
            .list().select(ListFields.PAY_BILLS_TABS, PAY_SAVED_PAYEE)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_SPRINT)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_SPRINT_PLACEHOLDER)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_BANK_OF_AMERICA)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_BANK_OF_AMERICA_PLACEHOLDER, false)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_APPLE)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_APPLE_PLACEHOLDER, true)
            .select().selectOption(SelectFields.SP_PAYEE_DDL, PAYEE_WELLS_FARGO)
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, PAYEE_WELLS_FARGO_PLACEHOLDER)
            .complete();
   }

}
