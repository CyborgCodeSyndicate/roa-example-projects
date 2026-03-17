package io.cyborgcode.ui.simple.test.framework.ui.elements;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.button.ButtonComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.ButtonUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.ButtonFieldTypes;
import java.util.function.Consumer;

/**
 * Registry of button UI elements for the Zero Bank demo application.
 *
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
 * to ensure presence/clickability, wait for overlays, or verify element removal after interaction.
 * These patterns help stabilize interactions against asynchronous UI updates and page transitions.
 *
 * <p>The lifecycle hooks are exposed through {@link #before()} and {@link #after()}, which return
 * This allows per-control synchronization without duplicating waits inside tests.
 *
 * <p>Typical usage targets Bootstrap-styled buttons via the {@link ButtonFieldTypes}
 * mapping to the framework’s {@code Button} component implementation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ButtonFields implements ButtonUiElement {

   SIGN_IN_BUTTON(PwBy.css("#signin_button"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   SIGN_IN_FORM_BUTTON(PwBy.css("input[value='Sign in']"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   SUBMIT_BUTTON(PwBy.css("#btn_submit"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   CALCULATE_COST_BUTTON(PwBy.css("#pc_calculate_costs"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   PURCHASE_BUTTON(PwBy.css("#purchase_cash"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   MORE_SERVICES_BUTTON(PwBy.css("#online-banking"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   FIND_SUBMIT_BUTTON(PwBy.css("button[type='submit']"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE),
   PAY_BUTTON(PwBy.css("#pay_saved_payees"), ButtonFieldTypes.BOOTSTRAP_BUTTON_TYPE);

   private final PwBy locator;
   private final ButtonComponentType componentType;
   private final Consumer<Page> before;
   private final Consumer<Page> after;


   ButtonFields(final PwBy locator, final ButtonComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   ButtonFields(PwBy locator,
                ButtonComponentType componentType,
                Consumer<Page> before,
                Consumer<Page> after) {
      this.locator = locator;
      this.componentType = componentType;
      this.before = before;
      this.after = after;
   }


   @Override
   public PwBy locator() {
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
   public Consumer<Page> before() {
      return before;
   }


   @Override
   public Consumer<Page> after() {
      return after;
   }

}
