package io.cyborgcode.ui.simple.test.framework.ui;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;

/**
 * Reusable Selenium wait helpers for the demo application.
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

   public static void waitForPresence(Page page, PwBy locator) {
      try {
         PwElement element = PwElement.of(page.locator(locator.selector()));
         element.waitFor(
               new Locator.WaitForOptions()
                     .setState(WaitForSelectorState.VISIBLE)
         );
      } catch (Exception ignore) {
         // handle failure
      }
   }
}
