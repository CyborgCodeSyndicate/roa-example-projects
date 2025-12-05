package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Reusable UI synchronization strategies for the demo application.
 *
 * <p>Each enum constant wraps a wait function (see {@link SharedUiFunctions}) that can be used:
 *
 * <ul>
 *   <li>as a plain {@link Consumer}{@code <SmartWebDriver>} (no locator), or
 *   <li>bound to a specific {@link By} via {@link #asConsumer(By)}.
 * </ul>
 *
 * <p>Implements {@link ContextConsumer} so it can be passed directly as before/after hooks in UI
 * element enums (e.g., `ButtonFields`) to stabilize interactions on dynamic pages.
 *
 * <p>Backed by custom conditions in {@link ExpectedConditionsStore} to handle transient DOM states
 * and timing issues in JS-driven UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
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
