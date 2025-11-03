package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ExpectedConditionsStore {

   private ExpectedConditionsStore() {
   }

   /**
    * Returns an ExpectedCondition that checks for the visibility of
    * an element found by the given locator.
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
    * Returns an ExpectedCondition that checks for the invisibility
    * of an element found by the given locator.
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
    * Returns an ExpectedCondition that checks if the element found
    * by the given locator is both displayed and enabled.
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
}
