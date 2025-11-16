package io.cyborgcode.ui.complex.test.framework.preconditions;

import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreatorFunctions;
import io.cyborgcode.ui.complex.test.framework.db.extractors.DbResponsesJsonPaths;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.storage.StorageKeysDb;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.apache.http.HttpStatus;

import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_DB;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_SELLER_EMAIL;
import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_SELLER_PASSWORD;
import static io.cyborgcode.ui.complex.test.framework.api.AppEndpoints.ENDPOINT_BAKERY;
import static io.cyborgcode.ui.complex.test.framework.service.CustomService.getJsessionCookie;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.db.validator.DbAssertionTarget.QUERY_RESULT;
import static io.cyborgcode.roa.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;

/**
 * Implementation functions for precondition setup operations.
 *
 * <p>This utility class provides the actual precondition logic referenced by {@link Preconditions}
 * enum constants. Each method performs setup operations required to establish test state before the
 * main test execution begins.
 *
 * <p>These functions demonstrate multi-ring orchestration, combining UI, API, and database
 * operations to establish complex test preconditions. They integrate with ROA {@code @Journey}
 * and {@code @PreQuest} annotations to execute setup logic before tests.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class PreconditionFunctions {

   private PreconditionFunctions() {
   }

   public static void loginUser(SuperQuest quest, Seller seller) {
      loginUserAndValidate(quest, seller);
   }

   public static void loginDefaultUser(SuperQuest quest) {
      loginUserAndValidate(quest, DataCreatorFunctions.createValidSeller());
   }

   public static void validateSellerExistInDatabase(SuperQuest quest, Seller seller) {
      quest
            .use(RING_OF_DB)
            .query(QUERY_SELLER_EMAIL.withParam("id", 1))
            .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_SELLER_EMAIL, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.EMAIL.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(seller.getUsername()).soft(true)
                        .build())
            .query(QUERY_SELLER_PASSWORD.withParam("id", 1))
            .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_SELLER_PASSWORD, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PASSWORD.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(seller.getPassword()).soft(true)
                        .build()
            );
   }

   public static void validOrderSetup(SuperQuest quest, Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order);
   }

   public static void validOrderSetup(SuperQuest quest, Late<Order> order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order.create());
   }

   private static void loginUserAndValidate(SuperQuest quest, Seller seller) {
      quest
            .use(RING_OF_CUSTOM)
            .login(seller)
            .drop()
            .use(RING_OF_API)
            .requestAndValidate(
                  ENDPOINT_BAKERY.withHeader("Cookie", getJsessionCookie()),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build());
   }

}
