package io.cyborgcode.ui.simple.test.framework.data.creator;

import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;

public final class DataCreatorFunctions {

   private DataCreatorFunctions() {
   }

   public static PurchaseForeignCurrency createPurchaseForeignCurrency() {
      return PurchaseForeignCurrency.builder()
            .currency("Mexico (peso)")
            .amount("100")
            .usDollar("U.S. dollar (USD)")
            .build();
   }

}
