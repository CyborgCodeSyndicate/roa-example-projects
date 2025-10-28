package io.cyborgcode.ui.complex.test.framework.preconditions;

import io.cyborgcode.ui.complex.test.framework.model.bakery.Order;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Seller;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;

import java.util.function.BiConsumer;

import static io.cyborgcode.ui.complex.test.framework.preconditions.BakeryQuestPreconditionFunctions.*;

public enum BakeryQuestPreconditions implements PreQuestJourney<BakeryQuestPreconditions> {

   SELLER_PRECONDITION((quest, objects) -> validSellerSetup(quest, (Seller) objects[0])),
   SELLER_PRECONDITION_LATE((quest, objects) -> validSellerSetup(quest, (Late<Seller>) objects[0])),
   ORDER_PRECONDITION((quest, objects) -> validOrderSetup(quest, (Order) objects[0])),
   ORDER_PRECONDITION_LATE((quest, objects) -> validOrderSetup(quest, (Late<Order>) objects[0])),
   LOGIN_PRECONDITION((quest, objects) -> loginUser(quest, (Seller) objects[0]));


   public static final class Data {

      public static final String SELLER_PRECONDITION = "SELLER_PRECONDITION";
      public static final String SELLER_PRECONDITION_LATE = "SELLER_PRECONDITION_LATE";
      public static final String ORDER_PRECONDITION = "ORDER_PRECONDITION";
      public static final String ORDER_PRECONDITION_LATE = "ORDER_PRECONDITION_LATE";
      public static final String LOGIN_PRECONDITION = "LOGIN_PRECONDITION";


      private Data() {
      }

   }


   private final BiConsumer<SuperQuest, Object[]> function;


   BakeryQuestPreconditions(final BiConsumer<SuperQuest, Object[]> function) {
      this.function = function;
   }


   @Override
   public BiConsumer<SuperQuest, Object[]> journey() {
      return function;
   }


   @Override
   public BakeryQuestPreconditions enumImpl() {
      return this;
   }

}
