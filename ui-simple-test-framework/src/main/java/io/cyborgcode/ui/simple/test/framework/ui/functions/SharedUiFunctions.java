package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.cyborgcode.ui.simple.test.framework.ui.functions.ExpectedConditionsStore.visibilityOfElementLocatedCustom;

/**
 * Reusable Selenium wait helpers for the demo application.
 *
 * <p>This utility centralizes synchronization logic used by UI flows. It wraps {@link SmartWebDriver}
 * waits and delegates to custom conditions from {@link ExpectedConditionsStore}
 * and standard Selenium {@link ExpectedConditions} where appropriate.
 *
 * <p>These functions are used by higher-level strategies in {@link SharedUi}
 * (via {@link ContextConsumer}) to compose per-control before/after hooks in UI element enums.
 *
 * <p>Example usage:
 * <pre>{@code
 * // Direct wait with SmartWebDriver
 * SharedUiFunctions.waitForPresence(smartWebDriver, By.id("alert_content"));
 *
 * // As an enum hook (see SharedUi)
 * SharedUi.WAIT_FOR_PRESENCE.accept(smartWebDriver, By.id("btn_submit"));
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class SharedUiFunctions {

   private SharedUiFunctions() {
   }

   public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
   }
}
