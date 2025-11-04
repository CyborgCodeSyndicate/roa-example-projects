package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.ui.complex.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.complex.test.framework.data.test_data.DataProperties;
import io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFlows;
import io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions;
import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.db.annotations.DB;
import io.cyborgcode.roa.db.annotations.DbHook;
import io.cyborgcode.roa.db.annotations.DbHooks;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.ui.complex.test.framework.preconditions.Preconditions.Data.LOGIN_PRECONDITION;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.LOCATION_DDL;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.PRODUCTS_DDL;
import static io.cyborgcode.roa.framework.hooks.HookExecution.BEFORE;
import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UI
@DB
@API
@DbHooks({
      @DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
})
class BakeryEvolvingTest extends BaseQuest {


   @Test
   @Regression
   @Description("Raw usage")
   void createOrderRaw(Quest quest) {
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
            .validate(() -> {
               SuperQuest superQuest = QuestHolder.get();
               List<SmartWebElement> elements = superQuest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase("John Terry")));
            })
            .complete();
   }


   @Test
   @Regression
   @Description("Raw with Data usage")
   void createOrderRawData(Quest quest) {
      final DataProperties dataProperties = ConfigCache.getOrCreate(DataProperties.class);

      Seller seller = Seller.builder()
            .email(dataProperties.sellerEmail())
            .password(dataProperties.sellerPassword())
            .build();

      Order order = Order.builder()
            .customerName(dataProperties.customerName())
            .customerDetails(dataProperties.customerDetails())
            .phoneNumber(dataProperties.phoneNumber())
            .location(dataProperties.location())
            .product(dataProperties.product())
            .build();

      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME_FIELD, seller.getEmail())
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
            .validate(() -> {
               SuperQuest superQuest = QuestHolder.get();
               List<SmartWebElement> elements = superQuest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
            })
            .complete();
   }


   @Test
   @Description("Craft usage")
   void createOrderCraft(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(InputFields.USERNAME_FIELD, seller.getEmail())
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
            .validate(() -> {
               SuperQuest superQuest = QuestHolder.get();
               List<SmartWebElement> elements = superQuest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
            })
            .complete();
   }


   @Test
   @Description("Insertion and Craft usage")
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
            .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
            .validate(() -> {
               SuperQuest superQuest = QuestHolder.get();
               List<SmartWebElement> elements = superQuest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
            })
            .complete();
   }


   @Test
   @Description("Service and Craft usage")
   void createOrderService(Quest quest,
         @Craft(model = DataCreator.Data.VALID_SELLER) Seller seller,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .loginUser(seller)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication, Craft and Service usage")
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
   @Description("PreQuest, Craft and Service usage")
   @PreQuest({
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)}, order = 1),
         @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)}, order = 2)
   })
   void createOrderPreQuest(Quest quest,
         @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authenticate, PreQuest, Service and Ripper usage")
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
   @PreQuest({
         @Journey(value = Preconditions.Data.ORDER_PRECONDITION,
               journeyData = {@JourneyData(DataCreator.Data.VALID_ORDER)})
   })
   @Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
   void createOrderPreArgumentsAndRipper(Quest quest) {
      quest
            .use(RING_OF_CUSTOM)
            .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class))
            .complete();
   }

}
