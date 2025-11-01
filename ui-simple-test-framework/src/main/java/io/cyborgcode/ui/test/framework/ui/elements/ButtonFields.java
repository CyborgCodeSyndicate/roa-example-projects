package io.cyborgcode.ui.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.button.ButtonComponentType;
import io.cyborgcode.roa.ui.selenium.ButtonUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.ui.test.framework.ui.functions.ContextConsumer;
import io.cyborgcode.ui.test.framework.ui.functions.SharedUi;
import io.cyborgcode.ui.test.framework.ui.types.ButtonFieldTypes;
import java.util.function.Consumer;
import org.openqa.selenium.By;

public enum ButtonFields implements ButtonUiElement {

   SIGN_IN_BUTTON(By.id("signin_button"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE, SharedUi.WAIT_FOR_PRESENCE),
   SIGN_IN_FORM_BUTTON(By.cssSelector("input[value='Sign in']"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SUBMIT_BUTTON(By.id("btn_submit"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   CALCULATE_COST_BUTTON(By.id("pc_calculate_costs"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   PURCHASE_BUTTON(By.id("purchase_cash"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   MORE_SERVICES_BUTTON(By.id("online-banking"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   FIND_SUBMIT_BUTTON(By.cssSelector("button[type='submit']"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
   PAY_BUTTON(By.id("pay_saved_payees"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE);

   private final By locator;
   private final ButtonComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;


   ButtonFields(final By locator) {
      this(locator, null, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   ButtonFields(final By locator, final ButtonComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before) {
      this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
      });
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                Consumer<SmartWebDriver> before,
                Consumer<SmartWebDriver> after) {
      this.locator = locator;
      this.componentType = componentType;
      this.before = before;
      this.after = after;
   }


   @Override
   public By locator() {
      return locator;
   }


   @Override
   public <T extends ComponentType> T componentType() {
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
