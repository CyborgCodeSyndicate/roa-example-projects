package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.db.responses.DbResponsesJsonPaths;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminUi;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogging;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.db.annotations.DbHooks;
import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.storage.StorageKeysDb;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_DB;
import static io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner.Data.DELETE_CREATED_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER_PRODUCT;
import static io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions.Data.*;
import static io.cyborgcode.roa.db.validator.DbAssertionTarget.QUERY_RESULT;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS_ALL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;

@UI
@DB
@API
@DbHooks({
      @DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
})
public class BakeryDataBaseTests extends BaseQuest {

   @Test
   @Description("Database usage in Test")
   @PreQuest({
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 2)
   })
   public void createOrderDatabaseValidation(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .drop()
            .use(RING_OF_DB)
            .query(QUERY_ORDER.withParam("id", 1))
            .validate(retrieve(StorageKeysDb.DB, QUERY_ORDER, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PRODUCT_BY_ID.getJsonPath(1))
                        .type(CONTAINS_ALL).expected(List.of(order.getProduct())).soft(true)
                        .build(),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.LOCATION_BY_ID.getJsonPath(1))
                        .type(CONTAINS_ALL).expected(List.of(order.getLocation())).soft(true)
                        .build()
            )
            .complete();
   }


   @Test
   @Description("Database usage in PreQuest and Test")
   @PreQuest({
         @Journey(value = SELLER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1),
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 2),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 3)
   })
   public void createOrderPreQuestDatabase(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .drop()
            .use(RING_OF_DB)
            .query(QUERY_ORDER_PRODUCT.withParam("id", 1))
            .validate(retrieve(StorageKeysDb.DB, QUERY_ORDER_PRODUCT, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PRODUCT.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(order.getProduct()).soft(true)
                        .build()
            )
            .complete();
   }


   @Test
   @Description("Database cleanup with Ripper usage")
   @AuthenticateViaUi(credentials = AdminUi.class, type = AppUiLogging.class)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
   })
   @Ripper(targets = {DELETE_CREATED_ORDERS})
   public void createOrderPreArgumentsAndRipper(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }

}
