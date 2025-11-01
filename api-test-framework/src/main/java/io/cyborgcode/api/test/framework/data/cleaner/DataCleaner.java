package io.cyborgcode.api.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import java.util.function.Consumer;

public enum DataCleaner implements DataRipper<DataCleaner> {

   DELETE_ADMIN_USER(DataCleanerFunctions::deleteAdminUser);

   public static final class Data {

      private Data() {
      }

      public static final String DELETE_ADMIN_USER = "DELETE_ADMIN_USER";

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
