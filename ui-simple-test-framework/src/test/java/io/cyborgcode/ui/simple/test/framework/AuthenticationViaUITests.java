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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.BROKERAGE_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.CHECKING_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.CREDIT_CARD_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.LOAN_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.SAVINGS_ACCOUNT;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.SUCCESSFUL_TRANSFER_MESSAGE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.TRANSFER_BROKERAGE_TO_CHECKING;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.TRANSFER_LOAN_TO_CREDIT_CARD;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.TransferFunds.TRANSFER_SAVING_TO_CHECKING;

/**
 * Represents an authenticated UI test suite that integrates with the {@link AuthenticateViaUi}.
 *
 * <p>This class demonstrates a single cached login via {@code cacheCredentials = true}.
 * The authenticated session is reused as a precondition across tests in this class to reduce
 * boilerplate and speed up execution. The first annotated test performs the login and
 * subsequent annotated tests reuse the cached session.
 *
 * <p>The {@link AuthenticateViaUi} annotation must be applied at the test method level.
 * If an isolated session is needed per test, set {@code cacheCredentials = false}.
 *
 * <p>Examples target the Zero Bank demo app (via {@code getUiConfig().baseUrl()}),
 * but the patterns are app-agnostic and reusable across projects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DisplayName("UI Authentication with Cached Session")
class AuthenticationViaUITests extends BaseQuest {

   @Test
   @Regression
   @Description("Login is handled once via @AuthenticateViaUi and reused")// Uses @AuthenticateViaUi with cacheCredentials=true so login runs once and is reused across tests
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void testScenario_1(Quest quest) {
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
   void testScenario_2(Quest quest) {
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

   @Test
   @Regression
   @Description("Login session is reused from cached login")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void testScenario_3(Quest quest) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, BROKERAGE_ACCOUNT)
            .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, CHECKING_ACCOUNT)
            .input().insert(InputFields.AMOUNT_FIELD, "100")
            .input().insert(InputFields.TF_DESCRIPTION_FIELD, TRANSFER_BROKERAGE_TO_CHECKING)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .button().click(ButtonFields.SUBMIT_BUTTON)
            .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, SUCCESSFUL_TRANSFER_MESSAGE)
            .complete();
   }

}
