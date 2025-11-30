package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.InterceptRequests;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.data.extractor.DataExtractorFunctions;
import io.cyborgcode.ui.complex.test.framework.data.test_data.StaticData;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields;
import io.cyborgcode.ui.complex.test.framework.ui.interceptor.RequestsInterceptor;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuestSequential;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.storage.DataExtractorsTest.staticTestData;
import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.ui.complex.test.framework.api.AppEndpoints.ENDPOINT_BAKERY;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.*;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.ui.complex.test.framework.service.CustomService.getJsessionCookie;

/**
 * End-to-end examples showcasing advanced ROA features working together:
 * Craft and Insertion, Precondition Journeys, UI Authentication, Storage/StaticTestData,
 * Request Interceptors with custom extraction, Late data creation, and Ripper cleanup.
 * The H2 database is initialized before the suite via a DbHook.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DB
@API
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@DisplayName("Advanced Features usage examples")
class AdvancedFeaturesTest extends BaseQuestSequential {

   @Test
   @Smoke
   @Regression
   @Description("Craft and Insertion features: Obtain typed models via @Craft and populate mapped model fields to UI " +
           "controls in one operation using insertion service")
   void craftAndInsertionFeatures(Quest quest,
         @Craft(model = DataCreator.Data.SELLER) Seller seller,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .insertion().insertData(seller)
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .drop()
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .complete();
   }

   @Test
   @Regression
   @Description("Precondition feature: Uses @Journey precondition without data injection")
   @Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
   void preconditionFeatureUsingJourneyWithoutData(Quest quest,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
       quest
               .use(RING_OF_CUSTOM)
               .createOrder(order)
               .validateOrder(order)
               .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Precondition feature: Uses multiple ordered @Journey preconditions with data injection")
   @Journey(value = Preconditions.Data.LOGIN_PRECONDITION,
           journeyData = {@JourneyData(DataCreator.Data.SELLER)}, order = 1)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
           journeyData = {@JourneyData(DataCreator.Data.ORDER)}, order = 2)
   void preconditionFeatureUsingMuliJourneysWithData(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class))
            .complete();
   }

   @Test
   @Regression
   @Description("Authentication feature: Automatic login via @AuthenticateViaUi without cache " +
         "credentials for session re-usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void authenticationFeatureWithoutCacheCredentials(Quest quest,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Authentication feature: Automatic login via @AuthenticateViaUi with cache credentials " +
         "for session re-usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void authenticationFeatureWithCacheCredentials(Quest quest,
         @Craft(model = DataCreator.Data.ORDER) Order order) {
      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  ENDPOINT_BAKERY.withHeader("Cookie", getJsessionCookie()),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
            .drop()
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Storage feature: Retrieve storage data captured during test UI steps")
   void storageFeatureUsingDataFromFluentSteps(Quest quest,
         @Craft(model = DataCreator.Data.SELLER) Seller seller) {
      quest
            .use(RING_OF_CUSTOM)
            .login(seller)
            .drop()
            .use(RING_OF_UI)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .select().getAvailableOptions(SelectFields.LOCATION_DDL)
            .validate(() -> Assertions.assertEquals(
                  2,
                  DefaultStorage.retrieve(SelectFields.LOCATION_DDL, List.class).size()))
            .validate(() -> Assertions.assertIterableEquals(
                  List.of("Store", "Bakery"),
                  DefaultStorage.retrieve(SelectFields.LOCATION_DDL, List.class)))
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Storage feature: Retrieve storage data captured during test precondition as journeyData " +
           "pre-arguments")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.ORDER)})
   void storageFeatureUsingDataFromPreconditions(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class))
            .complete();
   }

   @Test
   @Regression
   @Description("Static Data with Storage feature: Retrieve storage data preloaded as static data before test " +
           "execution")
   @Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
   @StaticTestData(StaticData.class)
   void staticTestDataFeatureUsingDataFromStorage(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(staticTestData(StaticData.ORDER), Order.class))
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Interceptor feature: Use built-in response status validation")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void interceptorFeatureUsedForAuthenticateViaUiValidation(Quest quest) {
      quest
            .use(RING_OF_UI)
            .interceptor().validateResponseHaveStatus(
                  RequestsInterceptor.INTERCEPT_REQUEST_AUTH.getEndpointSubString(), 2, true)
            .complete();
   }

   @Test
   @Regression
   @Description("Interceptor feature: Extract intercepted responses from storage using custom data extractors for " +
         "test validation")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   void interceptorFeatureUsedForTestDataValidation(Quest quest,
         @Craft(model = DataCreator.Data.SELLER) Seller seller) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUsingInsertion(seller)
            .editOrder("Lionel Huber")
            .drop()
            .use(RING_OF_UI)
            .validate(() -> Assertions.assertEquals(List.of("$197.54"),
                  retrieve(DataExtractorFunctions
                              .responseBodyExtraction(RequestsInterceptor.INTERCEPT_REQUEST_AUTH.getEndpointSubString(),
                                    "$[0].changes[?(@.key=='totalPrice')].value", "for(;;);"),
                        List.class)))
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Interceptor feature: Extract intercepted responses data and use it for Late data creation when needed")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   void interceptorFeatureUsedForLateDataCreation(Quest quest,
         @Craft(model = DataCreator.Data.SELLER) Seller seller,
         @Craft(model = DataCreator.Data.ORDER) Order order,
         @Craft(model = DataCreator.Data.LATE_ORDER) Late<Order> lateOrder) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUsingInsertion(seller)
            .createOrder(order)
            .validateOrder(order)
            .createOrder(lateOrder.create())
            .validateOrder(lateOrder.create())
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @Description("Ripper feature: Deletes data created in the test")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.ORDER)})
   @Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
   void ripperFeature(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class))
            .complete();
   }

}
