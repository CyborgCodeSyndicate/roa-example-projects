package io.cyborgcode.ui.simple.test.framework.preconditions;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_PURCHASE_CURRENCY;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

/**
 * Implementation functions for precondition setup operations.
 *
 * <p> This utility class provides the actual precondition logic referenced by {@link Preconditions}
 * enum constants. Each method performs setup operations required to establish test state
 * before the main test execution begins.

 * <p> These functions demonstrate multi-ring orchestration, combining UI, API, and database
 * operations to establish complex test preconditions. They integrate with ROA
 * {@code @Journey} and {@code @PreQuest} annotations to execute setup logic before tests.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class PreconditionFunctions {

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
