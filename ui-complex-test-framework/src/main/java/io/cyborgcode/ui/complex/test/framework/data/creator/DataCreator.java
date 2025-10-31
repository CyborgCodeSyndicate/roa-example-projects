package io.cyborgcode.ui.complex.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

public enum DataCreator implements DataForge<DataCreator> {

   VALID_SELLER(DataCreationFunctions::createValidSeller),
   VALID_ORDER(DataCreationFunctions::createValidOrder),
   VALID_LATE_ORDER(DataCreationFunctions::createValidLateOrder);

   public static final class Data {

      public static final String VALID_SELLER = "VALID_SELLER";
      public static final String VALID_ORDER = "VALID_ORDER";
      public static final String VALID_LATE_ORDER = "VALID_LATE_ORDER";

      private Data() {
      }

   }

   private final Late<Object> createDataFunction;

   DataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }

   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }

   @Override
   public DataCreator enumImpl() {
      return this;
   }

}
