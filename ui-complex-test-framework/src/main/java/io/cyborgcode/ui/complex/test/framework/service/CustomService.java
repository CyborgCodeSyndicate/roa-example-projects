package io.cyborgcode.ui.complex.test.framework.service;

import io.cyborgcode.ui.complex.test.framework.ui.model.Order;
import io.cyborgcode.ui.complex.test.framework.ui.model.Seller;
import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Objects;

import static io.cyborgcode.roa.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static io.cyborgcode.ui.complex.test.framework.base.Rings.RING_OF_UI;
import static io.cyborgcode.ui.complex.test.framework.data.creator.DataCreator.ORDER;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields.NEW_ORDER_BUTTON;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields.SIGN_IN_BUTTON;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.*;
import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * High-level, domain-specific service for UI application test operations.
 *
 * <p>This custom fluent service extends {@link FluentService} and is registered as the "Custom"
 * ring via {@code @Ring}. It provides business-logic-focused test operations that abstract complex
 * UI interaction sequences into reusable, readable methods. Tests access this service via {@code
 * quest.use(RING_OF_CUSTOM)}.
 *
 * <p>This service internally delegates to the UI ring ({@code RING_OF_UI}) for low-level Selenium
 * operations, maintaining the separation between test orchestration and element interaction logic.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Ring("Custom")
public class CustomService extends FluentService {

   private static final By HEADER_LOCATOR = By.cssSelector("h3[class='name']");
   private static final By LOGOUT_LOCATOR = By.tagName("vaadin-login-overlay");

   public CustomService login(String username, String password) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(USERNAME_FIELD, username)
            .input().insert(PASSWORD_FIELD, password)
            .button().click(SIGN_IN_BUTTON)
            .button().validateIsVisible(NEW_ORDER_BUTTON)
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }

   public CustomService login(Seller seller) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .input().insert(USERNAME_FIELD, seller.getUsername())
            .input().insert(PASSWORD_FIELD, seller.getPassword())
            .button().click(SIGN_IN_BUTTON)
            .button().validateIsVisible(NEW_ORDER_BUTTON)
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }

   public CustomService loginUsingInsertion(Seller seller) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .insertion().insertData(seller)
            .button().click(SIGN_IN_BUTTON)
            .button().validateIsVisible(NEW_ORDER_BUTTON)
            .input().validateIsEnabled(SEARCH_BAR_FIELD);
      return this;
   }

   public CustomService logout() {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .link().click(LinkFields.LOGOUT_LINK)
            .validate(() -> quest.artifact(RING_OF_UI, SmartWebDriver.class).getWait()
                  .until(ExpectedConditions.presenceOfElementLocated(LOGOUT_LOCATOR)));
      return this;
   }

   public CustomService createOrder() {
      quest
            .use(RING_OF_UI)
            .button().click(NEW_ORDER_BUTTON)
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

   public CustomService createOrder(Order order) {
      quest
            .use(RING_OF_UI)
            .button().click(NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON);
      return this;
   }

   public CustomService validateOrder(String customer) {
      quest
            .use(RING_OF_UI)
            .validate(() -> findOrderForCustomer(customer));
      return this;
   }

   public CustomService validateOrder(Order order) {
      quest
            .use(RING_OF_UI)
            .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
            .validate(() -> findOrderForCustomer(order.getCustomerName()))
            .button().click(ButtonFields.CLEAR_SEARCH);
      return this;
   }

   public CustomService validateOrder() {
      Order order = QuestHolder.get().getStorage().sub(PRE_ARGUMENTS).getByClass(ORDER, Order.class);
      quest
            .use(RING_OF_UI)
            .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
            .validate(() -> findOrderForCustomer(order.getCustomerName()))
            .button().click(ButtonFields.CLEAR_SEARCH);
      return this;
   }

   public CustomService editOrder(String customer) {
      quest
            .use(RING_OF_UI)
            .input().insert(SEARCH_BAR_FIELD, customer)
            .validate(() -> {
               List<SmartWebElement> elements = quest.artifact(RING_OF_UI, SmartWebDriver.class)
                     .findSmartElements(HEADER_LOCATOR);
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

   public CustomService setJsessionCookie(String cookie) {
      QuestHolder.get().artifact(RING_OF_UI, SmartWebDriver.class)
            .getOriginal()
            .manage()
            .addCookie(new Cookie("JSESSIONID", cookie));
      return this;
   }

   private void findOrderForCustomer(String customer) {
      List<SmartWebElement> elements = quest.artifact(RING_OF_UI, SmartWebDriver.class)
            .findSmartElements(HEADER_LOCATOR);
      assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(customer)));
   }

}
