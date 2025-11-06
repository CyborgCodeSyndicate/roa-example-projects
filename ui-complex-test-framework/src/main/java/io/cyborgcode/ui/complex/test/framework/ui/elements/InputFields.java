package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.functions.ContextConsumer;
import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUi;
import io.cyborgcode.ui.complex.test.framework.ui.types.InputFieldTypes;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.input.InputComponentType;
import io.cyborgcode.roa.ui.selenium.InputUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum InputFields implements InputUiElement {

   USERNAME_FIELD(By.id("vaadinLoginUsername"), InputFieldTypes.VA_INPUT_TYPE),
   PASSWORD_FIELD(By.id("vaadinLoginPassword"), InputFieldTypes.VA_INPUT_TYPE),
   SEARCH_BAR_FIELD(By.cssSelector("search-bar#search"), InputFieldTypes.VA_INPUT_TYPE,
         SharedUi.WAIT_FOR_PRESENCE,
         SharedUi.WAIT_FOR_LOADING),
   CUSTOMER_FIELD(By.id("customerName"), InputFieldTypes.VA_INPUT_TYPE,
         SharedUi.WAIT_FOR_PRESENCE),
   DETAILS_FIELD(By.id("customerDetails"), InputFieldTypes.VA_INPUT_TYPE,
         SharedUi.WAIT_FOR_PRESENCE),
   NUMBER_FIELD(By.id("customerNumber"), InputFieldTypes.VA_INPUT_TYPE,
         SharedUi.WAIT_FOR_PRESENCE);

   public static final class Data {

      public static final String USERNAME_FIELD = "USERNAME_FIELD";
      public static final String PASSWORD_FIELD = "PASSWORD_FIELD";
      public static final String CUSTOMER_FIELD = "CUSTOMER_FIELD";
      public static final String DETAILS_FIELD = "DETAILS_FIELD";
      public static final String NUMBER_FIELD = "NUMBER_FIELD";

      private Data() {
      }

   }

   private final By locator;
   private final InputComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;

   InputFields(By locator, InputComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   InputFields(By locator,
               InputComponentType componentType,
               ContextConsumer before) {
      this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
      });
   }

   InputFields(By locator,
               InputComponentType componentType,
               Consumer<SmartWebDriver> before,
               Consumer<SmartWebDriver> after) {
      this.locator = locator;
      this.componentType = componentType;
      this.before = before;
      this.after = after;
   }

   InputFields(By locator,
               InputComponentType componentType,
               ContextConsumer before,
               ContextConsumer after) {
      this(locator, componentType, before.asConsumer(locator), after.asConsumer(locator));
   }

   @Override
   public By locator() {
      return locator;
   }

   @Override
   public <T extends ComponentType> T componentType() {
      if (componentType == null) {
         return InputUiElement.super.componentType();
      }
      return (T) componentType;
   }

   @Override
   public Enum<?> enumImpl() {
      return this;
   }

   @Override
   public Consumer<SmartWebDriver> before() {
      return before;
   }

   @Override
   public Consumer<SmartWebDriver> after() {
      return after;
   }

}
