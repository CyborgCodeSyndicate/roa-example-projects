package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.types.LinkFieldTypes;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.link.LinkComponentType;
import io.cyborgcode.roa.ui.selenium.LinkUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum LinkFields implements LinkUiElement {

   STOREFRONT_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK),
   DASHBOARD_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK),
   LOGOUT_LINK(By.cssSelector("a[href='/logout']"), LinkFieldTypes.VA_LINK);

   public static final class Data {

      public static final String STOREFRONT_LINK = "STOREFRONT_LINK";
      public static final String DASHBOARD_LINK = "DASHBOARD_LINK";
      public static final String LOGOUT_LINK = "LOGOUT_LINK";

      private Data() {
      }

   }

   private final By locator;
   private final LinkComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;


   LinkFields(By locator, LinkComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   LinkFields(By locator,
              LinkComponentType componentType,
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
