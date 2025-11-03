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
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

@UI
class ZeroBankComponentsTest extends BaseQuest {


   @Test()
   @Description("COMPONENTS: Button, Input, Link, Select, Alert")
   @Regression
   void alertTest(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Credit Card(Avail. balance = $ -265)")
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Loan(Avail. balance = $ 780)")
            .input().insert(InputFields.AMOUNT_FIELD, "100")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, "Transfer Amount")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   void radioButtonTest(Quest quest) {
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
            .list().select(ListFields.PAY_BILLS_TABS, "Purchase Foreign Currency")
            .select().selectOption(SelectFields.PC_CURRENCY_DDL, "Mexico (peso)")
            .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
            .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
            .button().click(ButtonFields.CALCULATE_COST_BUTTON)
            .button().click(ButtonFields.PURCHASE_BUTTON)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Alert")
   @Regression
   void paragraphTextValueTestSoftAssertions(Quest quest) {
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
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
   @Regression
   void paragraphTextValueTestHardAssertions(Quest quest) {
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
            .validate().validateTextInField(Tag.I, "For 48944145651315 Apple account")
            .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
            .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
            .validate().validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account", false)
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
   @Regression
   void paragraphTextValueTestMixedAssertions(Quest quest) {
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
