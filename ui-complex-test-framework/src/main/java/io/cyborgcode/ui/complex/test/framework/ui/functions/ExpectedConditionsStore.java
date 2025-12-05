package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
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
 * used in the demo application. Compared to standard Selenium {@link ExpectedConditions}, these
 * helpers are designed to be resilient to transient DOM states and common timing issues (e.g.,
 * {@link StaleElementReferenceException}).
 *
 * <p>These conditions are consumed by {@link SharedUiFunctions} and higher-level wait strategies
 * (see {@link SharedUi}) to keep tests stable and expressive.
 *
 * <p>Example usage:
 *
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

   /**
    * Returns an ExpectedCondition that checks if the element found by the given locator is visible.
    * The element is considered visible if it is displayed and not stale.
    * If the element can't be found or is stale, it's considered invisible.
    * @param locator The locator of the element to check for visibility.
    * @return An ExpectedCondition that checks if the element is visible.
    */
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


   /**
    * Returns an ExpectedCondition that checks for the invisibility of an element found by the given locator.
    * The element is considered invisible if it is not visible, or if it can't be found (e.g. NoSuchElementException)
    * or if it is stale (e.g. StaleElementReferenceException).
    *
    * @param locator The locator of the element to check for invisibility.
    * @return An ExpectedCondition that checks for the invisibility of the element.
    */
   public static ExpectedCondition<Boolean> invisibilityOfElementLocatedCustom(final By locator) {
      return new ExpectedCondition<>() {
         @Override
         public Boolean apply(WebDriver driver) {
            try {
               SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
               return !smartWebDriver.findSmartElement(locator).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
               // If the element can't be found or is stale, it's considered invisible
               return true;
            }
         }

         @Override
         public String toString() {
            return "element to no longer be visible: " + locator;
         }
      };
   }


   /**
    * Returns an ExpectedCondition that checks if the element found by the given locator is clickable.
    * The element is considered clickable when it is visible and enabled.
    *
    * @param locator the By locator of the element to wait for
    * @return an ExpectedCondition that checks if the element is clickable
    */
   public static ExpectedCondition<Boolean> elementToBeClickableCustom(final By locator) {
      return new ExpectedCondition<>() {
         @Override
         public Boolean apply(WebDriver driver) {
            try {
               SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
               SmartWebElement element = smartWebDriver.findSmartElement(locator);
               return element.isDisplayed() && element.isEnabled();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
               return false;
            }
         }

         @Override
         public String toString() {
            return "element to be clickable: " + locator;
         }
      };
   }


   /**
    * Returns an ExpectedCondition that checks if the "loading" attribute has been removed from
    * the given SmartWebElement.
    * This can be used to wait for an element to finish loading before performing an action on it.
    * @param element The SmartWebElement to wait for.
    * @return An ExpectedCondition that checks if the "loading" attribute has been removed from the given SmartWebElement.
    */
   public static ExpectedCondition<Boolean> attributeLoadingToBeRemovedCustom(final SmartWebElement element) {
      return new ExpectedCondition<>() {
         @Override
         public Boolean apply(WebDriver driver) {
            try {
                String v = element.getDomAttribute("loading");
                return v == null || v.isEmpty() || "false".equalsIgnoreCase(v);
            } catch (StaleElementReferenceException e) {
                return false;
            }
         }

         @Override
         public String toString() {
            return "loading attribute to be removed from element: " + element;
         }
      };
   }

}
