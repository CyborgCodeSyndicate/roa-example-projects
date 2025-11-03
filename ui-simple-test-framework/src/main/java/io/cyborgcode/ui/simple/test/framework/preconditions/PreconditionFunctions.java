package io.cyborgcode.ui.simple.test.framework.preconditions;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_PURCHASE_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

public class PreconditionFunctions {

   private PreconditionFunctions() {
   }

   public static void userLoginSetup(SuperQuest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back();
   }

   public static void purchaseCurrencySetup(SuperQuest quest, PurchaseForeignCurrency purchaseForeignCurrency) {
      quest
            .use(RING_OF_PURCHASE_CURRENCY)
            .purchaseCurrency(purchaseForeignCurrency);
   }

}
