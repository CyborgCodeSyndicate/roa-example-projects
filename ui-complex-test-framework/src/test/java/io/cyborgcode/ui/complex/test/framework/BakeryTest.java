package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.ui.components.table.base.TableField;
import io.cyborgcode.roa.ui.components.table.filters.FilterStrategy;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.Tables;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.ui.complex.test.framework.ui.model.tables.TableEntry;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.roa.ui.storage.DataExtractorsUi.tableRowExtractor;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.*;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.TABLE_VALUES;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UI
@DB
@API
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
class BakeryTest extends BaseQuest {


    @Test
    @Regression
    @Description("Order creation flow using craft and insertion features")
    void createOrderUsingCraftAndInsertionFeatures(Quest quest,
                                                   @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
                                                   @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
        quest
                .use(RING_OF_UI)
                .browser().navigate(getUiConfig().baseUrl())
                .insertion().insertData(seller)
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Lionel Huber")
                .table().readTable(Tables.ORDERS)
                .table().validate(
                        Tables.ORDERS,
                        Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build(),
                        Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(25).soft(true).build())
                /*.validate(() -> Assertions.assertEquals(
                        "Lionel Huber",
                        retrieve(tableRowExtractor(Tables.CAMPAIGNS, "Today"),
                                TableEntry.class).getRow().getText(),
                        "Wrong row value"))*/
                .complete();
    }

    /*@Test
    @Description("PreQuest, Craft and Service usage")
    @Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
    @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
            journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 2)
    void createOrderPreQuest(Quest quest) {
        quest
                .use(RING_OF_CUSTOM)
                .validateOrder()
                .complete();
    }*/


    /*@Test
    @Description("Authenticate, PreQuest, Service and Ripper usage")
    @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
    @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
            journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
    @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
            journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER2)})
    void createOrderPreArgumentsAndRipper(Quest quest) {
        quest
                .use(RING_OF_CUSTOM)
                .validateOrder()
                .complete();
    }*/


    /*@Test
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
    }*/

    /*@Test
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
    }*/

    /*@Test()
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
    }*/

   /* @Test
    @Description("Authentication usage")
    @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
    void createOrderAuth(Quest quest,
                         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
        quest
                .use(RING_OF_CUSTOM)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }

    @Test
    @Description("Authenticate, PreQuest and PreArguments usage")
    @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
    @PreQuest({
            @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
                    journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
    })
    void createOrderAuthPreQuestPreArguments(Quest quest) {
        quest
                .use(RING_OF_CUSTOM)
                .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
                .complete();
    }*/

    /*@Test
    @Description("Authentication usage")
    void createOrderAuth2(Quest quest,
                          @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
                          @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
        quest
                .use(RING_OF_UI)
                .browser().navigate(getUiConfig().baseUrl())
                .drop()
                .use(RING_OF_API)
                .request(ENDPOINT_BAKERY_LOGIN, seller)
                //.request(ENDPOINT_BAKERY_LOGIN.withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded"), seller)
                .drop()
                .use(RING_OF_CUSTOM)
                .setJsessionCookie(
                        retrieve(StorageKeysApi.API, ENDPOINT_BAKERY_LOGIN, Response.class)
                        .getCookies().get("JSESSIONID"))
                .drop()
                .use(RING_OF_UI)
                .browser().navigate(getUiConfig().baseUrl() + "/dashboard")
                .drop()
                .use(RING_OF_CUSTOM)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }*/

/*    @Test
    @Description("Service and Craft usage")
    @StaticTestData(StaticData.class)
    void createOrderUsingCustomServiceStaticDataAndCraft(Quest quest,
           @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {

        String username = QuestHolder.get().getStorage().get(staticTestData(StaticData.USERNAME), String.class);
        String password = QuestHolder.get().getStorage().get(staticTestData(StaticData.PASSWORD), String.class);

        quest
                .use(RING_OF_CUSTOM)
                .login(username, password)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }

   @Test
   @Description("Service and Craft usage")
   void createOrderUsingCustomServiceAndCraft(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .login(seller)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }*/

}
