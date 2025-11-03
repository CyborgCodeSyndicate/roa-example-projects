package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.PreQuest;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.simple.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.simple.test.framework.data.retriever.DataProperties;
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
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_PURCHASE_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.simple.test.framework.preconditions.Preconditions.Data.PURCHASE_CURRENCY_PRECONDITION;
import static io.cyborgcode.ui.simple.test.framework.preconditions.Preconditions.Data.USER_LOGIN_PRECONDITION;

@UI
class ZeroBankEvolvingTest extends BaseQuest {


   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   void radioButtonTestSimple(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate("http://zero.webappsecurity.com/")
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
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   void radioButtonTestReadFromConfigProperties(Quest quest) {
      final DataProperties dataProperties = ConfigCache.getOrCreate(DataProperties.class);

      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, dataProperties.username())
            .input().insert(InputFields.PASSWORD_FIELD, dataProperties.password())
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
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void radioButtonTestAuthenticateViaUINoCache(Quest quest) {
      quest
            .use(RING_OF_UI)
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
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void radioButtonTestCraftUsage(Quest quest,
         @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Purchase Foreign Currency")
            .select().selectOption(SelectFields.PC_CURRENCY_DDL, purchaseForeignCurrency.getCurrency())
            .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, purchaseForeignCurrency.getAmount())
            .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
            .button().click(ButtonFields.CALCULATE_COST_BUTTON)
            .button().click(ButtonFields.PURCHASE_BUTTON)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void radioButtonTestUseCustomServiceAndChangeService(Quest quest,
         @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
      quest
            .use(RING_OF_PURCHASE_CURRENCY)
            .purchaseCurrency(purchaseForeignCurrency)
            .drop()
            .use(RING_OF_UI)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void radioButtonTestUseOnlyCustomService(Quest quest,
         @Craft(model = DataCreator.Data.PURCHASE_CURRENCY) PurchaseForeignCurrency purchaseForeignCurrency) {
      quest
            .use(RING_OF_PURCHASE_CURRENCY)
            .purchaseCurrency(purchaseForeignCurrency)
            .validatePurchase()
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   @PreQuest({
         @Journey(value = PURCHASE_CURRENCY_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.PURCHASE_CURRENCY)})
   })
   void radioButtonTestUsingJourney(Quest quest) {
      quest
            .use(RING_OF_PURCHASE_CURRENCY)
            .validatePurchase()
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
   @Regression
   @PreQuest({
         @Journey(value = USER_LOGIN_PRECONDITION),
         @Journey(value = PURCHASE_CURRENCY_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.PURCHASE_CURRENCY)})
   })
   void radioButtonTestUsingMultipleJourneysAndWithoutJourneyData(Quest quest) {
      quest
            .use(RING_OF_PURCHASE_CURRENCY)
            .validatePurchase()
            .complete();
   }

}
