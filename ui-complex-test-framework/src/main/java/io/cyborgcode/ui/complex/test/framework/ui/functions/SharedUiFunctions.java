package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.cyborgcode.ui.complex.test.framework.ui.functions.ExpectedConditionsStore.*;

public class SharedUiFunctions {

   public static void waitForTimeout(SmartWebDriver smartWebDriver) {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         throw new RuntimeException("Thread was interrupted during sleep", e);
      }
   }

   public static void waitForLoading(SmartWebDriver smartWebDriver) {
      smartWebDriver.getWait().until(
            ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
      smartWebDriver.getWait().until(
            ExpectedConditions.attributeToBe(By.cssSelector(".v-loading-indicator.first"),
                  "style", "display: none;"));
   }

   public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
      try {
         smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
      } catch (Exception ignore) {
         //handle failure
      }
   }

   public static void waitToBeClickable(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(elementToBeClickableCustom(locator));
   }

   public static void waitToBeRemoved(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(invisibilityOfElementLocatedCustom(locator));
   }

   public static void waitForElementLoading(SmartWebDriver smartWebDriver, SmartWebElement element) {
      smartWebDriver.getWait().until(attributeLoadingToBeRemovedCustom(element));
   }
}
