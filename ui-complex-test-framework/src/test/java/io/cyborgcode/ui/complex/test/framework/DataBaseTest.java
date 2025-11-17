package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.db.extractors.DbResponsesJsonPaths;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.storage.StorageKeysDb;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_DB;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_ORDER_PRODUCT;
import static io.cyborgcode.roa.db.validator.DbAssertionTarget.QUERY_RESULT;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS_ALL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;

/**
 * Database-centric tests demonstrating:
 * - DbHook to initialize H2 before tests
 * - DB validations inside test flows
 * - DB validations in preconditions (Journeys) before executing UI steps
 * - Cleanup of created data using the Ripper feature after test execution
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DB
@API
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@DisplayName("Database usage examples")
class DataBaseTest extends BaseQuest {

   @Test
   @Regression
   @Description("Demonstrates database validation within test execution") //Explain better
   @Journey(value = Preconditions.Data.LOGIN_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)})
   void createOrderDatabaseValidation(Quest quest,
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
   @Description("Demonstrates database validation in Journey preconditions before test execution") //Explain better
   @Journey(value = Preconditions.Data.SELLER_EXIST_IN_DB_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1)
   @Journey(value = Preconditions.Data.LOGIN_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 2)
   void createOrderPreQuestDatabase(Quest quest,
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
   @Description("Demonstrates database cleanup using @Ripper feature") //Explain better
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
   @Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
   void createOrderPreArgumentsAndRipper(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }

}
