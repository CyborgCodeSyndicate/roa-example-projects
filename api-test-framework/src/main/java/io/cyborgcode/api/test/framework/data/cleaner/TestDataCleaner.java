package io.cyborgcode.api.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper<TestDataCleaner> {

   DELETE_ADMIN_USER(DataCleanUpFunctions::deleteAdminUser);

   public static final class Data {

      public static final String DELETE_ADMIN_USER = "DELETE_ADMIN_USER";

      private Data() {
      }

   }

   private final Consumer<SuperQuest> cleanUpFunction;

   TestDataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
      this.cleanUpFunction = cleanUpFunction;
   }

   @Override
   public Consumer<SuperQuest> eliminate() {
      return cleanUpFunction;
   }

   @Override
   public TestDataCleaner enumImpl() {
      return this;
   }

}
