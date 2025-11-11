package io.cyborgcode.ui.simple.test.framework.preconditions;

import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;
import java.util.function.BiConsumer;

import static io.cyborgcode.ui.simple.test.framework.preconditions.PreconditionFunctions.purchaseCurrencySetup;
import static io.cyborgcode.ui.simple.test.framework.preconditions.PreconditionFunctions.userLoginSetup;

public enum Preconditions implements PreQuestJourney<Preconditions> {

   PURCHASE_CURRENCY_PRECONDITION((quest, objects) -> purchaseCurrencySetup(quest, (PurchaseForeignCurrency) objects[0])),
   USER_LOGIN_PRECONDITION((quest, objects) -> userLoginSetup(quest));

   public static final class Data {

      public static final String PURCHASE_CURRENCY_PRECONDITION = "PURCHASE_CURRENCY_PRECONDITION";
      public static final String USER_LOGIN_PRECONDITION = "USER_LOGIN_PRECONDITION";

      private Data() {
      }

   }

   private final BiConsumer<SuperQuest, Object[]> function;

   Preconditions(final BiConsumer<SuperQuest, Object[]> function) {
      this.function = function;
   }

   @Override
   public BiConsumer<SuperQuest, Object[]> journey() {
      return function;
   }

   @Override
   public Preconditions enumImpl() {
      return this;
   }

}
