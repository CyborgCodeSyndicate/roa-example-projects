package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.openqa.selenium.By;

/**
 * Reusable UI synchronization strategies for the demo application.
 *
 * <p>Each enum constant wraps a wait function (see {@link SharedUiFunctions}) that can be used:
 * <ul>
 *   <li>as a plain {@link Consumer}{@code <SmartWebDriver>} (no locator), or</li>
 *   <li>bound to a specific {@link By} via {@link #asConsumer(By)}.</li>
 * </ul>
 * 
 * <p>Implements {@link ContextConsumer} so it can be passed directly as before/after hooks
 * in UI element enums (e.g., `ButtonFields`) to stabilize interactions on dynamic pages.
 *
 * <p>Backed by custom conditions in {@link ExpectedConditionsStore}
 * to handle transient DOM states and timing issues in JS-driven UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum SharedUi implements ContextConsumer {

   WAIT_FOR_PRESENCE(SharedUiFunctions::waitForPresence);

   private final BiConsumer<SmartWebDriver, By> function;

   SharedUi(BiConsumer<SmartWebDriver, By> function) {
      this.function = function;
   }

   @Override
   public Consumer<SmartWebDriver> asConsumer(By locator) {
      return driver -> function.accept(driver, locator);
   }

   @Override
   public void accept(SmartWebDriver driver) {
      accept(driver, null);
   }

   public void accept(SmartWebDriver driver, By locator) {
      function.accept(driver, locator);
   }
}
