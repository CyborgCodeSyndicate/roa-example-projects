package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.simple.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.ui.simple.test.framework.ui.elements.AlertFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

@UI
class ZeroBankAuthTest extends BaseQuest {


   @Test()
   @Description("COMPONENTS: Button, Input, Link, Select, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void testScenario1(Quest quest) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Loan(Avail. balance = $ 780)")
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Credit Card(Avail. balance = $ -265)")
            .input().insert(InputFields.AMOUNT_FIELD, "300")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, "Transfer Amount from Loan to Credit Card")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
            .complete();
   }

   @Test()
   @Description("COMPONENTS: Button, Input, Link, Select, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void testScenario2(Quest quest) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Savings(Avail. balance = $ 1000)")
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Checking(Avail. balance = $ -500.2)")
            .input().insert(InputFields.AMOUNT_FIELD, "2000")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, "Transfer Amount from Savings to Checking")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
            .complete();
   }

   @Test()
   @Description("COMPONENTS: Button, Input, Link, Select, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void testScenario3(Quest quest) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Brokerage(Avail. balance = $ 197)")
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Checking(Avail. balance = $ -500.2)")
            .input().insert(InputFields.AMOUNT_FIELD, "100")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, "Transfer Amount from Brokerage to Checking")
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
            .complete();
   }

}
