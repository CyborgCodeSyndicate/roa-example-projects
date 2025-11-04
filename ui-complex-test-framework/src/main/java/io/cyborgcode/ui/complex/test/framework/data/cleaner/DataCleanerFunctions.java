package io.cyborgcode.ui.complex.test.framework.data.cleaner;

import io.cyborgcode.ui.complex.test.framework.db.extractors.DbResponsesJsonPaths;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.storage.StorageKeysDb;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;

import java.util.List;

import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_DB;
import static io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator.VALID_ORDER;
import static io.cyborgcode.roa.db.validator.DbAssertionTarget.NUMBER_ROWS;
import static io.cyborgcode.roa.db.validator.DbAssertionTarget.QUERY_RESULT;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER_ALL;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER_DELETE;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER_PRODUCT;

public final class DataCleanerFunctions {

    private DataCleanerFunctions() {
    }

   public static void cleanAllOrders(SuperQuest quest) {
      var storage = quest.getStorage().sub(PRE_ARGUMENTS);
      List<Order> allOrders = storage.getAllByClass(VALID_ORDER, Order.class);

      allOrders.forEach(order ->
            quest
                  .use(RING_OF_DB)
                  .query(QUERY_ORDER_ALL)
                  .query(QUERY_ORDER_DELETE.withParam("id", order.getId()))
                  .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER_DELETE, QueryResponse.class),
                        Assertion.builder()
                              .target(QUERY_RESULT).key(DbResponsesJsonPaths.DELETED.getJsonPath(0))
                              .type(IS).expected(1).soft(true)
                              .build()
                  )
                  .query(QUERY_ORDER_PRODUCT.withParam("id", order.getId()))
                  .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER_PRODUCT, QueryResponse.class),
                        Assertion.builder()
                              .target(NUMBER_ROWS).key("numRows")
                              .type(IS).expected(0).soft(true)
                              .build()
                  ));
   }

}
