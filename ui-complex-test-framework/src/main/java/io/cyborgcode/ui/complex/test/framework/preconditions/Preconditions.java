package io.cyborgcode.ui.complex.test.framework.preconditions;

import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;

import java.util.function.BiConsumer;

import static io.cyborgcode.ui.complex.test.framework.preconditions.PreconditionFunctions.*;

/**
 * Registry of reusable pre-test journeys for the Bakery UI test suite.
 * <p>
 * Each enum constant represents a named precondition that can be referenced from
 * {@link Journey @Journey} annotations to set up required state before test execution.
 * The underlying {@link BiConsumer} receives the active {@link SuperQuest} and any
 * journey data, and is responsible for preparing the required state (e.g., logging in users,
 * creating orders, validating sellers) before the actual test method executes.
 * <p>
 * Centralizing preconditions here keeps setup logic:
 * <ul>
 *   <li>discoverable and type-safe,</li>
 *   <li>reusable across multiple tests,</li>
 *   <li>aligned with the ROA {@link PreQuestJourney} contract,</li>
 *   <li>decoupled from test implementation details.</li>
 * </ul>
 */
public enum Preconditions implements PreQuestJourney<Preconditions> {

   SELLER_EXIST_IN_DB_PRECONDITION((quest, objects) -> validateSellerExistInDatabase(quest, (Seller) objects[0])),
   ORDER_PRECONDITION((quest, objects) -> validOrderSetup(quest, (Order) objects[0])),
   ORDER_PRECONDITION_LATE((quest, objects) -> validOrderSetup(quest, (Late<Order>) objects[0])),
   LOGIN_PRECONDITION((quest, objects) -> loginUser(quest, (Seller) objects[0])),
   LOGIN_DEFAULT_PRECONDITION((quest, objects) -> loginDefaultUser(quest));

   public static final class Data {

      public static final String SELLER_EXIST_IN_DB_PRECONDITION = "SELLER_EXIST_IN_DB_PRECONDITION";
      public static final String ORDER_PRECONDITION = "ORDER_PRECONDITION";
      public static final String ORDER_PRECONDITION_LATE = "ORDER_PRECONDITION_LATE";
      public static final String LOGIN_PRECONDITION = "LOGIN_PRECONDITION";
      public static final String LOGIN_DEFAULT_PRECONDITION = "LOGIN_DEFAULT_PRECONDITION";

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
