package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Custom Selenium {@link ExpectedCondition} utilities for robust UI waits.
 *
 * <p>This class provides synchronization conditions tailored for dynamic, JavaScript-driven pages
 * used in the demo application. Compared to standard Selenium
 * {@link ExpectedConditions}, these helpers are designed to be resilient
 * to transient DOM states and common timing issues (e.g., {@link StaleElementReferenceException}).
 *
 * <p>These conditions are consumed by {@link SharedUiFunctions} and higher-level wait strategies
 * (see {@link SharedUi}) to keep tests stable and expressive.
 *
 * <p>Example usage:
 * <pre>{@code
 * // Direct usage with SmartWebDriver
 * smartWebDriver.getWait().until(ExpectedConditionsStore
 *         .visibilityOfElementLocatedCustom(By.id("alert_content"))
 * );
 *
 * // Indirect usage via SharedUiFunctions
 * SharedUiFunctions.waitForPresence(smartWebDriver, By.id("alert_content"));
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ExpectedConditionsStore {

   private ExpectedConditionsStore() {
   }

   public static ExpectedCondition<Boolean> visibilityOfElementLocatedCustom(final By locator) {
      return new ExpectedCondition<>() {
         @Override
         public Boolean apply(WebDriver driver) {
            try {
               SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
               return smartWebDriver.findSmartElement(locator).isDisplayed();
            } catch (NullPointerException | NoSuchElementException | StaleElementReferenceException e) {
               return false;
            }
         }

         @Override
         public String toString() {
            return "element to be visible: " + locator;
         }
      };
   }
}
