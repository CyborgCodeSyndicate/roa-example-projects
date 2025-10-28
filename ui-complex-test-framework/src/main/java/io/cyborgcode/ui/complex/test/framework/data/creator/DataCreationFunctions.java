package io.cyborgcode.ui.complex.test.framework.data.creator;

import io.cyborgcode.ui.complex.test.framework.data.extractions.CustomDataExtractor;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Order;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Seller;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import org.openqa.selenium.NotFoundException;

import java.util.List;

public class DataCreationFunctions {


   public static Seller createValidSeller() {
      return Seller.builder()
            .email("baker@vaadin.com")
            .password("Fw@7pop9")
            .build();
   }


   public static Order createValidOrder() {
      return Order.builder()
            .id(1)
            .customerName("John Terry")
            .customerDetails("Address")
            .phoneNumber("+1-555-7777")
            .location("Bakery")
            .product("Strawberry Bun")
            .build();
   }


   public static Order createValidLateOrder() {
      SuperQuest superQuest = QuestHolder.get();
      List<String> productList = superQuest.getStorage().get(CustomDataExtractor
                  .responseBodyExtraction("?v-r=uidl",
                        "$..orderCard[?(@.fullName=='John Terry')].items[*].product.name", "for(;;);"),
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
