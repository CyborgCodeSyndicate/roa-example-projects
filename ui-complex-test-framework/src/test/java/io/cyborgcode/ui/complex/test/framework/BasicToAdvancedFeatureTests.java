package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.complex.test.framework.data.test_data.Data;
import io.cyborgcode.ui.complex.test.framework.data.test_data.StaticData;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions;
import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.Tables;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.TABLE_NOT_EMPTY;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.TABLE_VALUES;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.LOCATION_DDL;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.PRODUCTS_DDL;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.roa.framework.storage.DataExtractorsTest.staticTestData;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;

/**
 * Demonstrates a progressive evolution of UI testing features, moving from basic flows
 * to advanced capabilities in the ROA UI framework.
 *
 * <p>Each test introduces a new feature (configuration-driven data, UI authentication,
 * data crafting/injection, insertion service, custom service rings, and reusable
 * preconditions via journeys). The goal is to show how scenarios scale in sophistication
 * without repeating setup or boilerplate.
 *
 * <p>Use this class as a learning path: start with the simplest test and continue
 * in order to see each feature in isolation and then in combination.
 *
 * <p>Examples target the Bakery demo app (via {@code getUiConfig().baseUrl()}),
 * but the patterns are app-agnostic and reusable across projects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@API
@DB
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@DisplayName("Progressive UI Features: Basic to Advanced")
class BasicToAdvancedFeatureTests extends BaseQuest {

   @Test
   @Regression
   @Description("Baseline order creation flow without advanced framework features and static raw data usage")
   void createOrderUsingRawData(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate("https://bakery-flow.demo.vaadin.com/")
            .input().insert(InputFields.USERNAME_FIELD, "admin@vaadin.com")
            .input().insert(InputFields.PASSWORD_FIELD, "admin")
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
            .input().insert(InputFields.DETAILS_FIELD, "Address")
            .input().insert(InputFields.NUMBER_FIELD, "+1-555-7777")
            .select().selectOption(LOCATION_DDL, "Store")
            .select().selectOptions(PRODUCTS_DDL, Strategy.FIRST)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "John Terry")
            .table().readTable(Tables.ORDERS)
            .table().validate(
                  Tables.ORDERS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using externalized test data properties")
   void createOrderUsingTestDataProperties(Quest quest) {
      // Explain usage of test data properties
      Seller seller = Seller.builder()
            .username(Data.testData().sellerEmail())
            .password(Data.testData().sellerPassword())
            .build();

      Order order = Order.builder()
            .customerName(Data.testData().customerName())
            .customerDetails(Data.testData().customerDetails())
            .phoneNumber(Data.testData().phoneNumber())
            .location(Data.testData().location())
            .product(Data.testData().product())
            .build();

      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME_FIELD, seller.getUsername())
            .input().insert(InputFields.PASSWORD_FIELD, seller.getPassword())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, order.getCustomerName())
            .input().insert(InputFields.DETAILS_FIELD, order.getCustomerDetails())
            .input().insert(InputFields.NUMBER_FIELD, order.getPhoneNumber())
            .select().selectOption(LOCATION_DDL, order.getLocation())
            .select().selectOptions(PRODUCTS_DDL, order.getProduct())
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
            .table().readTable(Tables.ORDERS)
            .table().validate(
                  Tables.ORDERS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using static test data feature")
   @StaticTestData(StaticData.class)
   void createOrderUsingStaticTestDataFeature(Quest quest) {
      // Explain usage of static data trough storage
      String username = retrieve(staticTestData(StaticData.USERNAME), String.class);
      String password = retrieve(staticTestData(StaticData.PASSWORD), String.class);
      Order order = retrieve(staticTestData(StaticData.ORDER), Order.class);

      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME_FIELD, username)
            .input().insert(InputFields.PASSWORD_FIELD, password)
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, order.getCustomerName())
            .input().insert(InputFields.DETAILS_FIELD, order.getCustomerDetails())
            .input().insert(InputFields.NUMBER_FIELD, order.getPhoneNumber())
            .select().selectOption(LOCATION_DDL, order.getLocation())
            .select().selectOptions(PRODUCTS_DDL, Strategy.FIRST)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
            .table().readTable(Tables.ORDERS)
            .table().validate(
                  Tables.ORDERS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using craft feature to create domain objects")
   void createOrderUsingCraftFeature(Quest quest,
         // Craft: provides a typed model instance resolved by the data creator
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            // Using crafted model values directly in UI steps
            .input().insert(InputFields.USERNAME_FIELD, seller.getUsername())
            .input().insert(InputFields.PASSWORD_FIELD, seller.getPassword())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, order.getCustomerName())
            .input().insert(InputFields.DETAILS_FIELD, order.getCustomerDetails())
            .input().insert(InputFields.NUMBER_FIELD, order.getPhoneNumber())
            .select().selectOption(LOCATION_DDL, order.getLocation())
            .select().selectOptions(PRODUCTS_DDL, order.getProduct())
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
            .table().readTable(Tables.ORDERS)
            .table().validate(
                  Tables.ORDERS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using craft and insertion features")
   void createOrderUsingCraftAndInsertionFeatures(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            // insertion(): maps model fields to UI inputs in a single call
            .insertion().insertData(seller)
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
            .table().readTable(Tables.ORDERS)
            .table().validate(
                  Tables.ORDERS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using craft and custom service features")
   void createOrderUsingCraftAndCustomServiceFeatures(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            // Use a custom ring (service) exposing domain-specific fluent methods
            .use(RING_OF_CUSTOM)
            .login(seller)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using authentication, craft and custom service features")
   // @AuthenticateViaUi: auto-login per test as precondition (no cached session)
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   void createOrderUsingAuthenticationCraftAndCustomServiceFeatures(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }

   @Test
   @Regression
   @Description("Order creation flow using precondition, craft and custom service features")
   // PreQuest/Journey: reusable precondition executed before the test
   @Journey(value = Preconditions.Data.LOGIN_PRECONDITION,
          journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1)
   @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
          journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 2)
   void createOrderUsingPreconditionCraftAndCustomServiceFeatures(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder()
            .complete();
   }

}
