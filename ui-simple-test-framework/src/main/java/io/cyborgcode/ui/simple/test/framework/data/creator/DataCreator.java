package io.cyborgcode.ui.simple.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

public enum DataCreator implements DataForge<DataCreator> {

   PURCHASE_CURRENCY(DataCreatorFunctions::createPurchaseForeignCurrency);

   public static final class Data {

      public static final String PURCHASE_CURRENCY = "PURCHASE_CURRENCY";

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
