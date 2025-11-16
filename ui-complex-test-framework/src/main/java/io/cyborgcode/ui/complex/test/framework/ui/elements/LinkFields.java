package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.types.LinkFieldTypes;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.link.LinkComponentType;
import io.cyborgcode.roa.ui.selenium.LinkUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

/**
 * Registry of link UI elements for the test application.
 *
 * <p>Each enum constant defines a specific link or tab with its Selenium locator, component type
 * (see {@link LinkComponentType}), and optional before/after synchronization hooks.
 *
 * <p>Implements {@link LinkUiElement} to integrate with ROA fluent UI testing API for navigating
 * and triggering link-driven flows.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .link().click(LinkFields.STOREFRONT_LINK);
 * }</pre>
 *
 * <p>Before/after hooks can be used to wait for page transitions or loading indicators when
 * navigating between views.
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum LinkFields implements LinkUiElement {
   STOREFRONT_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK_TYPE),
   DASHBOARD_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK_TYPE),
   LOGOUT_LINK(By.cssSelector("a[href='/logout']"), LinkFieldTypes.VA_LINK_TYPE);

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
