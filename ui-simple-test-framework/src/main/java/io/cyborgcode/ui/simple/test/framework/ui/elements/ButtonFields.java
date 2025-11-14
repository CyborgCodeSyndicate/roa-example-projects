package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.button.ButtonComponentType;
import io.cyborgcode.roa.ui.selenium.ButtonUiElement;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.ui.simple.test.framework.ui.functions.ContextConsumer;
import io.cyborgcode.ui.simple.test.framework.ui.functions.SharedUi;
import io.cyborgcode.ui.simple.test.framework.ui.types.ButtonFieldTypes;
import java.util.function.Consumer;
import org.openqa.selenium.By;

/**
 * Registry of button UI elements for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific button with its Selenium {@link By}
 * locator, concrete component type (see {@link ButtonComponentType}),
 * and optional before/after synchronization hooks to handle dynamic page behavior.
 *
 * <p>Implements {@link ButtonUiElement} to integrate with ROA fluent UI testing API, enabling operations like:
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .button().click(ButtonFields.SIGN_IN_BUTTON);
 * }</pre>
 *
 * <p>Buttons can declare custom wait strategies via {@link SharedUi} and {@link ContextConsumer}
 * to ensure presence/clickability, wait for overlays, or verify element removal after interaction.
 * These patterns help stabilize interactions against asynchronous UI updates and page transitions.
 *
 * <p>The lifecycle hooks are exposed through {@link #before()} and {@link #after()}, which return
 * {@link Consumer} instances executed with the active {@link SmartWebDriver}.
 * This allows per-control synchronization without duplicating waits inside tests.
 *
 * <p>Typical usage targets Bootstrap-styled buttons via the {@link ButtonFieldTypes}
 * mapping to the framework’s {@code Button} component implementation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ButtonFields implements ButtonUiElement {

   SIGN_IN_BUTTON(By.id("signin_button"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE, SharedUi.WAIT_FOR_PRESENCE),
   SIGN_IN_FORM_BUTTON(By.cssSelector("input[value='Sign in']"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   SUBMIT_BUTTON(By.id("btn_submit"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   CALCULATE_COST_BUTTON(By.id("pc_calculate_costs"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   PURCHASE_BUTTON(By.id("purchase_cash"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   MORE_SERVICES_BUTTON(By.id("online-banking"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   FIND_SUBMIT_BUTTON(By.cssSelector("button[type='submit']"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   PAY_BUTTON(By.id("pay_saved_payees"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE);

   private final By locator;
   private final ButtonComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;


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
