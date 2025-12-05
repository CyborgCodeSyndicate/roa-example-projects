package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.functions.ContextConsumer;
import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUi;
import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUiFunctions;
import io.cyborgcode.ui.complex.test.framework.ui.types.ButtonFieldTypes;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.button.ButtonComponentType;
import io.cyborgcode.roa.ui.selenium.ButtonUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

/**
 * Registry of button UI elements for the test application.
 *
 * <p>Each enum constant defines a specific button with its Selenium {@link By} locator, component
 * type (see {@link ButtonComponentType}), and optional before/after synchronization hooks to handle
 * dynamic page behavior.
 *
 * <p>Implements {@link ButtonUiElement} to integrate with ROA fluent UI testing API, enabling click
 * operations.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .button().click(ButtonFields.SIGN_IN_BUTTON);
 * }</pre>
 *
 * <p>Buttons can declare custom wait strategies via {@link SharedUi} and {@link ContextConsumer} to
 * ensure presence/clickability, wait for overlays, or verify element removal after interaction.
 * These patterns help stabilize interactions against asynchronous UI updates and page transitions.
 *
 * <p>The lifecycle hooks are exposed through {@link #before()} and {@link #after()}, which return
 * {@link Consumer} instances executed with the active {@link SmartWebDriver}. This allows
 * per-control synchronization without duplicating waits inside tests.
 *
 * <p>Typical usage targets Vaadin-styled buttons via the {@link ButtonFieldTypes} mapping to the
 * framework’s {@code Button} component implementation.
 *
 * <p>The nested {@link ButtonFields.Data} class provides string constants for annotation-based
 * references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ButtonFields implements ButtonUiElement {
   SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_FOR_LOADING),
   NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         ButtonFields::waitForPresence),
   REVIEW_ORDER_BUTTON(By.cssSelector("vaadin-button#review"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         SharedUi.WAIT_TO_BE_REMOVED),
   CANCEL_ORDER_BUTTON(By.cssSelector("vaadin-button#cancel"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_FOR_TIMEOUT,
         SharedUi.WAIT_TO_BE_REMOVED),
   PLACE_ORDER_BUTTON(By.cssSelector("vaadin-button#save"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         SharedUi.WAIT_TO_BE_REMOVED),
   CLEAR_SEARCH(By.cssSelector("vaadin-button#clear"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         SharedUi.WAIT_TO_BE_REMOVED);

   public static final class Data {

     public static final String SIGN_IN_BUTTON = "SIGN_IN_BUTTON";
     public static final String NEW_ORDER_BUTTON = "NEW_ORDER_BUTTON";

     private Data() {
     }

   }

   private final By locator;
   private final ButtonComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;

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

   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before,
                ContextConsumer after) {
      this(locator, componentType, before.asConsumer(locator), after.asConsumer(locator));
   }

   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before,
                Consumer<SmartWebDriver> after) {
      this(locator, componentType, before.asConsumer(locator), after);
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

   private static void waitForPresence(SmartWebDriver driver) {
      SharedUiFunctions.waitForPresence(driver,
              By.cssSelector("vaadin-dialog-overlay#overlay"));
   }

}
