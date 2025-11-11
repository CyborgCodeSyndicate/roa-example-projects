package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Registry of reusable synchronization functions for UI element interactions.
 * <p>
 * This enum provides pre-configured wait strategies that can be used as before/after
 * hooks in UI element definitions ({@link io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields},
 * {@link io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields}, etc.).
 * Each constant wraps a specific wait condition from {@link SharedUiFunctions}.
 * </p>
 * <p>
 * Available wait strategies:
 * <ul>
 *   <li>{@link #WAIT_FOR_TIMEOUT} — fixed 1-second delay</li>
 *   <li>{@link #WAIT_FOR_LOADING} — waits for Vaadin loading indicators to disappear</li>
 *   <li>{@link #WAIT_FOR_PRESENCE} — waits for element to be visible in the DOM</li>
 *   <li>{@link #WAIT_TO_BE_CLICKABLE} — waits for element to be visible and enabled</li>
 *   <li>{@link #WAIT_TO_BE_REMOVED} — waits for element to be removed from the DOM</li>
 * </ul>
 * </p>
 * <p>
 * These functions integrate with ROA's {@link ContextConsumer} pattern, allowing them
 * to be passed as {@code Consumer<SmartWebDriver>} to element hooks, ensuring proper
 * synchronization with dynamic UI behavior.
 * </p>
 */
public enum SharedUi implements ContextConsumer {

   WAIT_FOR_TIMEOUT((driver, by) -> SharedUiFunctions.waitForTimeout(driver)),
   WAIT_FOR_LOADING((driver, by) -> SharedUiFunctions.waitForLoading(driver)),
   WAIT_FOR_PRESENCE(SharedUiFunctions::waitForPresence),
   WAIT_TO_BE_CLICKABLE(SharedUiFunctions::waitToBeClickable),
   WAIT_TO_BE_REMOVED(SharedUiFunctions::waitToBeRemoved);

   private final BiConsumer<SmartWebDriver, By> function;

   SharedUi(BiConsumer<SmartWebDriver, By> function) {
      this.function = function;
   }

   /**
    * Returns a Consumer that accepts a SmartWebDriver and applies the given
    * By locator to the function represented by this SharedUi instance.
    *
    * @param locator the locator to be applied to the function
    * @return a Consumer that applies the function to the given locator
    */
   @Override
   public Consumer<SmartWebDriver> asConsumer(By locator) {
      return driver -> function.accept(driver, locator);
   }

   /**
    * Calls the underlying function with the given driver and null as the locator.
    *
    * @param driver the SmartWebDriver to be passed to the function
    */
   @Override
   public void accept(SmartWebDriver driver) {
      accept(driver, null);
   }

   /**
    * Calls the underlying function with the given driver and locator.
    *
    * @param driver the SmartWebDriver to be passed to the function
    * @param locator the locator to be passed to the function
    */
   public void accept(SmartWebDriver driver, By locator) {
      function.accept(driver, locator);
   }
}
