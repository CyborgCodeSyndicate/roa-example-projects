package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.cyborgcode.ui.complex.test.framework.ui.functions.ExpectedConditionsStore.*;

/**
 * Reusable Selenium wait helpers for the demo application.
 *
 * <p>This utility centralizes synchronization logic used by UI flows. It wraps {@link SmartWebDriver}
 * waits and delegates to custom conditions from {@link ExpectedConditionsStore} and
 * standard Selenium {@link ExpectedConditions} where appropriate.
 *
 * <p>These functions are used by higher-level strategies in {@link SharedUi} (via
 * {@link ContextConsumer}) to compose per-control before/after hooks in UI element enums.
 *
 * <p>Example usage:
 *
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

   /**
    * Waits for 1 second. This can be used to add a delay when
    * needed in the UI.
    */
   @SuppressWarnings("unused")
   public static void waitForTimeout(SmartWebDriver driver) {
     try {
        Thread.sleep(1000);
     } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Thread was interrupted during sleep", e);
     }
   }

   /**
    * Waits for the loading indicator to appear and then disappear.
    * The loading indicator is the loader animation in the UI.
    * The function waits for the loader to appear and then waits for it to disappear.
    * @param smartWebDriver The SmartWebDriver instance.
    */
   public static void waitForLoading(SmartWebDriver smartWebDriver) {
      smartWebDriver.getWait().until(
            ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
      smartWebDriver.getWait().until(
            ExpectedConditions.attributeToBe(By.cssSelector(".v-loading-indicator.first"),
                  "style", "display: none;"));
   }

   /**
    * Waits for the presence of the element specified by the locator.
    * The presence of an element is checked by waiting for the element to be visible.
    * If the element is not visible after the default timeout, the function will throw an exception.
    * @param smartWebDriver The SmartWebDriver instance.
    * @param locator The locator of the element to wait for.
    */
   public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
      try {
         smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
      } catch (Exception ignore) {
         //handle failure
      }
   }

   /**
    * Waits for the element to be clickable. The element is considered clickable when
    * it is visible and enabled.
    *
    * @param smartWebDriver The SmartWebDriver instance.
    * @param locator The locator of the element to wait for.
    */
   public static void waitToBeClickable(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(elementToBeClickableCustom(locator));
   }

   /**
    * Waits for the element to be removed from the DOM.
    * The element is considered removed when it is no longer present in the DOM.
    * @param smartWebDriver The SmartWebDriver instance.
    * @param locator The locator of the element to wait for.
    */
   public static void waitToBeRemoved(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(invisibilityOfElementLocatedCustom(locator));
   }

   /**
    * Waits for the "loading" attribute to be removed from the given SmartWebElement.
    * This can be used to wait for an element to finish loading before performing an action on it.
    * @param smartWebDriver The SmartWebDriver instance.
    * @param element The SmartWebElement to wait for.
    */
   public static void waitForElementLoading(SmartWebDriver smartWebDriver, SmartWebElement element) {
      smartWebDriver.getWait().until(attributeLoadingToBeRemovedCustom(element));
   }
}
