package io.cyborgcode.ui.complex.test.framework.service;

import io.cyborgcode.ui.complex.test.framework.model.bakery.Order;
import io.cyborgcode.ui.complex.test.framework.model.bakery.Seller;
import io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.LinkFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.SelectFields;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Objects;

import static io.cyborgcode.ui.complex.test.framework.base.Ring.RING_OF_UI;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.ButtonFields.SIGN_IN_BUTTON;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.bakery.InputFields.*;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Ring("Custom")
public class CustomService extends FluentService {


   public CustomService loginUser(String username, String password) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(USERNAME_FIELD, username)
            .input().insert(PASSWORD_FIELD, password)
            .button().click(SIGN_IN_BUTTON)
            //.button().validateIsVisible(NEW_ORDER_BUTTON);
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }


   public CustomService loginUser(Seller seller) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(USERNAME_FIELD, seller.getEmail())
            .input().insert(PASSWORD_FIELD, seller.getPassword())
            .button().click(SIGN_IN_BUTTON)
            //.button().validateIsVisible(NEW_ORDER_BUTTON);
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }


   public CustomService loginUser2(Seller seller) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .insertion().insertData(seller)
            .button().click(SIGN_IN_BUTTON)
            //.button().validateIsVisible(NEW_ORDER_BUTTON);
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }


   public CustomService logoutUser() {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .link().click(LinkFields.LOGOUT_LINK)
            .validate(() -> quest.artifact(RING_OF_UI, SmartWebDriver.class).getWait()
                  .until(ExpectedConditions.presenceOfElementLocated(By.tagName("vaadin-login-overlay"))));
      return this;
   }

   public CustomService createOrder() {
      quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
            .input().validateValue(InputFields.CUSTOMER_FIELD, "John Terry")
            .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
            .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, "Strawberry Bun")
            .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
            .input().insert(InputFields.NUMBER_FIELD, "+1-555-7777")
            .input().insert(InputFields.DETAILS_FIELD, "Address")
            .select().selectOption(SelectFields.LOCATION_DDL, "Bakery")
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON);
      return this;
   }

   /*public CustomService createOrder(Order order) {
      quest
            .use(EARTH)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .input().insert(InputFields.CUSTOMER_FIELD, order.getCustomerName())
            .input().validateValue(InputFields.CUSTOMER_FIELD, order.getCustomerName())
            .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
            .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, order.getProduct())
            .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
            .input().insert(InputFields.NUMBER_FIELD, order.getPhoneNumber())
            .input().insert(InputFields.DETAILS_FIELD, order.getCustomerDetails())
            .select().selectOption(SelectFields.LOCATION_DDL, order.getLocation())
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON);
      return this;
   }*/

   //Insertion
   public CustomService createOrder(Order order) {
      quest
            .use(RING_OF_UI)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            /*.validate(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            })*/
            .button().click(ButtonFields.PLACE_ORDER_BUTTON);
      return this;
   }

   public CustomService validateOrder(String customer) {
      quest
            .use(RING_OF_UI)
            .validate(() -> {
               List<SmartWebElement> elements = quest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(customer)));
            });
      return this;
   }

   public CustomService validateOrder(Order order) {
      quest
            .use(RING_OF_UI)
            .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
            .validate(() -> {
               List<SmartWebElement> elements = quest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
            })
            .button().click(ButtonFields.CLEAR_SEARCH);
      return this;
   }

   public CustomService editOrder(String customer) {
      quest
            .use(RING_OF_UI)
            .input().insert(SEARCH_BAR_FIELD, customer)
            .validate(() -> {
               /*try {
                  Thread.sleep(1000);
               } catch (InterruptedException e) {
                  throw new RuntimeException(e);
               }*/
               List<SmartWebElement> elements = quest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(By.cssSelector("h3[class='name']"));
               elements.stream().filter(e -> e.getText().equalsIgnoreCase(customer)).findFirst().get().click();
            })
            .button().click(ButtonFields.CANCEL_ORDER_BUTTON);
      return this;
   }

   public static String getJsessionCookie() {
      return Objects.requireNonNull(
            QuestHolder.get().artifact(RING_OF_UI, SmartWebDriver.class)
                  .getOriginal()
                  .manage()
                  .getCookieNamed("JSESSIONID"),
            "JSESSIONID cookie not found!"
      ).toString();
   }
}
