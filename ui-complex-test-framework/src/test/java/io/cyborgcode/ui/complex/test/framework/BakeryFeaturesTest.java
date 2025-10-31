package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.InterceptRequests;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.data.extractions.CustomDataExtractor;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Order;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Seller;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.db.annotations.DbHooks;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuestSequential;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.ui.complex.test.framework.preconditions.BakeryInterceptRequests;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminUi;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.BakeryUiLogging;
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
import static io.cyborgcode.ui.complex.test.framework.base.Rings.*;
import static io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner.Data.DELETE_CREATED_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator.Data.*;
import static io.cyborgcode.ui.complex.test.framework.preconditions.BakeryInterceptRequests.Data.INTERCEPT_REQUEST_AUTH;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.ui.complex.test.framework.preconditions.BakeryQuestPreconditions.Data.LOGIN_PRECONDITION;
import static io.cyborgcode.ui.complex.test.framework.preconditions.BakeryQuestPreconditions.Data.ORDER_PRECONDITION;
import static io.cyborgcode.ui.complex.test.framework.rest.Endpoints.ENDPOINT_BAKERY;
import static io.cyborgcode.ui.complex.test.framework.service.CustomService.getJsessionCookie;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.ButtonFields.*;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.SelectFields.LOCATION_DDL;

@UI
@DB
@API
@DbHooks({
      @DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
})
class BakeryFeaturesTest extends BaseQuestSequential {


   @Test
   @Description("Insertion data usage")
   void createOrderInsertion(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .insertion().insertData(seller)
            .button().click(SIGN_IN_BUTTON)
            .button().click(NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(REVIEW_ORDER_BUTTON)
            .button().click(PLACE_ORDER_BUTTON)
            .drop()
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Storage usage")
   void createOrderStorage(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUser(seller)
            .drop()
            .use(RING_OF_UI)
            .button().click(NEW_ORDER_BUTTON)
            .select().getAvailableOptions(LOCATION_DDL)
            .validate(() -> Assertions.assertEquals(
                  2,
                  DefaultStorage.retrieve(LOCATION_DDL, List.class).size()))
            .validate(() -> Assertions.assertIterableEquals(
                  List.of("Store", "Bakery"),
                  DefaultStorage.retrieve(LOCATION_DDL, List.class)))
            .complete();
   }


   @Test
   @Description("PreQuest usage")
   @PreQuest({
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(VALID_SELLER)}, order = 1),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)}, order = 2)
   })
   void createOrderPreQuest(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUi(credentials = AdminUi.class, type = BakeryUiLogging.class)
   void createOrderAuth(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUi(credentials = AdminUi.class, type = BakeryUiLogging.class)
   void createOrderAuth2(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
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
   @AuthenticateViaUi(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)})
   })
   void createOrderAuthPreQuestPreArguments(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }


   @Test
   @Disabled
   @Description("Interceptor raw usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   public void createOrderInterceptor(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUser2(seller)
            .editOrder("Lionel Huber")
            .drop()
            .use(RING_OF_UI)
            .validate(() -> Assertions.assertEquals(List.of("$197.54"),
                  retrieve(CustomDataExtractor
                              .responseBodyExtraction(BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH.getEndpointSubString(),
                                    "$[0].changes[?(@.key=='totalPrice')].value", "for(;;);"),
                        List.class)))
            .complete();
   }


   @Test()
   @Disabled
   @Description("Late data created with interceptor and ripper data cleanup usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   @Ripper(targets = {DELETE_CREATED_ORDERS})
   public void createOrderInterceptorLateDataAndRipper(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller,
         @Craft(model = VALID_ORDER) Order order,
         @Craft(model = VALID_LATE_ORDER) Late<Order> lateOrder) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUser2(seller)
            .createOrder(order)
            .validateOrder(order)
            .createOrder(lateOrder.create())
            .validateOrder(lateOrder.create())
            .complete();
   }


   @Test
   @Disabled
   @Description("Interceptor with Storage and Late data re-usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   @AuthenticateViaUi(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)})
   })
   public void createOrderInterceptorStorage(Quest quest,
         @Craft(model = VALID_LATE_ORDER) Late<Order> lateOrder) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .createOrder(lateOrder.create())
            .validateOrder(lateOrder.create())
            .drop()
            .use(RING_OF_UI)
            .interceptor().validateResponseHaveStatus(
                  BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH.getEndpointSubString(), 2, true)
            .complete();
   }

}
