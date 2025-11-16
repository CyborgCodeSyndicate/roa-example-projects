package io.cyborgcode.ui.complex.test.framework.data.creator;

import io.cyborgcode.ui.complex.test.framework.data.extractor.DataExtractorFunctions;
import io.cyborgcode.ui.complex.test.framework.data.test_data.Data;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import org.openqa.selenium.NotFoundException;

import java.util.List;

/**
 * Factory methods backing {@link DataCreator} entries.
 *
 * <p>Provides centralized, reusable builders for test data objects used across examples. Some
 * factories are context-aware: they can read from the active {@link SuperQuest} storage or apply
 * the late-bound creation pattern demonstrating ROA ability to extract runtime data from UI
 * responses and reuse it in subsequent test steps.
 *
 * <p>This keeps test classes focused on behavior while delegating all data construction and lookup
 * logic to a single, maintainable location.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class DataCreatorFunctions {

   private static final String RESPONSE_BODY_EXTRACTOR = "?v-r=uidl";
   private static final String JSON_PATH_PRODUCT_NAME = "$..orderCard[?(@.fullName=='John Terry')].items[*].product.name";
   private static final String JSON_PREFIX = "for(;;);";

   private DataCreatorFunctions() {
   }

   public static Seller createValidSeller() {
      return Seller.builder()
            .username(Data.testData().sellerEmail())
            .password(Data.testData().sellerPassword())
            .build();
   }

   public static Order createValidOrder() {
      return Order.builder()
            .id(1)
            .customerName(Data.testData().customerName())
            .customerDetails(Data.testData().customerDetails())
            .phoneNumber(Data.testData().phoneNumber())
            .location(Data.testData().location())
            .product(Data.testData().product())
            .build();
   }

   public static Order createValidLateOrder() {
      SuperQuest superQuest = QuestHolder.get();
      List<String> productList = superQuest.getStorage().get(DataExtractorFunctions
                  .responseBodyExtraction(RESPONSE_BODY_EXTRACTOR,
                          JSON_PATH_PRODUCT_NAME, JSON_PREFIX),
            List.class);
      if (productList.isEmpty()) {
         throw new NotFoundException("There is no product element");
      }

      return Order.builder()
            .id(2)
            .customerName("Petar Terry")
            .customerDetails("Address")
            .phoneNumber("+1-222-7778")
            .location("Store")
            .product(productList.get(0))
            .build();
   }

}
