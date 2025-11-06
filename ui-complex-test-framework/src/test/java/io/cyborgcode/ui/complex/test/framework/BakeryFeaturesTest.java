package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.InterceptRequests;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.data.extractor.DataExtractorFunctions;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.ui.complex.test.framework.api.AppEndpoints.ENDPOINT_BAKERY;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.*;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.ui.complex.test.framework.service.CustomService.getJsessionCookie;

@UI
@DB
@API
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
class BakeryFeaturesTest extends BaseQuestSequential {


   @Test
   @Description("Insertion data usage")
   void createOrderInsertion(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
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
   @Description("Storage usage")
   void createOrderStorage(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller) {
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
   @Description("PreQuest usage")
   @Journey(value = Preconditions.Data.LOGIN_PRECONDITION,
           journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
           journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 2)
   void createOrderPreQuest(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void createOrderAuth(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void createOrderAuth2(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
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
   @Description("Authenticate, PreQuest and PreArguments usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
   void createOrderAuthPreQuestPreArguments(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }


   @Test
   @Disabled("Temporary")
   @Description("Interceptor raw usage")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   void createOrderInterceptor(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller) {
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


   @Test()
   @Disabled("Temporary")
   @Description("Late data created with interceptor and ripper data cleanup usage")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   @Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
   void createOrderInterceptorLateDataAndRipper(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order,
         @Craft(model = DataCreator.Data.VALID_LATE_ORDER) Late<Order> lateOrder) {
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
   @Disabled("Temporary")
   @Description("Interceptor with Storage and Late data re-usage")
   @InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
   void createOrderInterceptorStorage(Quest quest,
         @Craft(model = DataCreator.Data.VALID_LATE_ORDER) Late<Order> lateOrder) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .createOrder(lateOrder.create())
            .validateOrder(lateOrder.create())
            .drop()
            .use(RING_OF_UI)
            .interceptor().validateResponseHaveStatus(
                  RequestsInterceptor.INTERCEPT_REQUEST_AUTH.getEndpointSubString(), 2, true)
            .complete();
   }

}
