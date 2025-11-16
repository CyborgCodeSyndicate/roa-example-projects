package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.types.CheckboxFieldTypes;
import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUi;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.checkbox.CheckboxComponentType;
import io.cyborgcode.roa.ui.selenium.CheckboxUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

/**
 * Registry of checkbox UI elements for the test application.
 *
 * <p>Each enum constant defines a specific checkbox with its Selenium {@link By} locator, component
 * type (see {@link CheckboxComponentType}), and optional before/after synchronization hooks.
 *
 * <p>Implements {@link CheckboxUiElement} to integrate with ROA fluent UI testing API, for
 * selecting and validating checkbox options.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .checkbox().select(CheckboxFields.PAST_ORDERS_CHECKBOX);
 * }</pre>
 *
 * <p>Before/after hooks leverage {@link SharedUi} functions to handle asynchronous page behavior,
 * ensuring checkboxes are fully rendered and interactive before test actions.
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum CheckboxFields implements CheckboxUiElement {
   PAST_ORDERS_CHECKBOX(By.tagName("vaadin-checkbox"), CheckboxFieldTypes.VA_CHECKBOX_TYPE);

   public static final class Data {

       public static final String PAST_ORDERS_CHECKBOX = "PAST_ORDERS_CHECKBOX";

       private Data() {
       }

   }

   private final By locator;
   private final CheckboxComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;

   CheckboxFields(By locator, CheckboxComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   CheckboxFields(By locator,
                  CheckboxComponentType componentType,
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
