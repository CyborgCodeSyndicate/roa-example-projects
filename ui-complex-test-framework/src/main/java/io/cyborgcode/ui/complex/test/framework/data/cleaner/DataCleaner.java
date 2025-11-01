package io.cyborgcode.ui.complex.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;

import java.util.function.Consumer;

public enum DataCleaner implements DataRipper<DataCleaner> {

   DELETE_CREATED_ORDERS(DataCleanerFunctions::cleanAllOrders);

   public static final class Data {

      public static final String DELETE_CREATED_ORDERS = "DELETE_CREATED_ORDERS";

      private Data() {
      }

   }

   private final Consumer<SuperQuest> cleanUpFunction;

   DataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
      this.cleanUpFunction = cleanUpFunction;
   }

   @Override
   public Consumer<SuperQuest> eliminate() {
      return cleanUpFunction;
   }

   @Override
   public DataCleaner enumImpl() {
      return this;
   }

}
