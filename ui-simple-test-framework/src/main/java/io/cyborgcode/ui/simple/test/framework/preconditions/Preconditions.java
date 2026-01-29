package io.cyborgcode.ui.simple.test.framework.preconditions;

import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;
import java.util.function.BiConsumer;

import static io.cyborgcode.ui.simple.test.framework.preconditions.PreconditionFunctions.purchaseCurrencySetup;
import static io.cyborgcode.ui.simple.test.framework.preconditions.PreconditionFunctions.userLoginSetup;

/**
 * Registry of reusable pre-test journeys for the UI testing suite.
 *
 * <p> Each enum constant represents a named precondition that can be referenced from
 * {@link Journey} annotations to set up required state before test execution.
 * The underlying {@link BiConsumer} receives the active {@link SuperQuest} and any
 * journey data, and is responsible for preparing the required state (e.g., logging in users,
 * creating orders, validating sellers) before the actual test method executes.
 *
 * <p> Centralizing preconditions here keeps setup logic:
 * <ul>
 *   <li>discoverable and type-safe,</li>
 *   <li>reusable across multiple tests,</li>
 *   <li>aligned with the ROA {@link PreQuestJourney} contract,</li>
 *   <li>decoupled from test implementation details.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
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
