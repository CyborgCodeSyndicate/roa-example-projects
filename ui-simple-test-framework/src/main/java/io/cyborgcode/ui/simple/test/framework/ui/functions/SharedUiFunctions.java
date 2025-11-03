package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.cyborgcode.ui.simple.test.framework.ui.functions.ExpectedConditionsStore.elementToBeClickableCustom;
import static io.cyborgcode.ui.simple.test.framework.ui.functions.ExpectedConditionsStore.invisibilityOfElementLocatedCustom;
import static io.cyborgcode.ui.simple.test.framework.ui.functions.ExpectedConditionsStore.visibilityOfElementLocatedCustom;

public class SharedUiFunctions {

   public static void waitForLoading(SmartWebDriver smartWebDriver) {
      smartWebDriver.getWait().until(
            ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
   }

   public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
   }

   public static void waitToBeClickable(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(elementToBeClickableCustom(locator));
   }

   public static void waitToBeRemoved(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(invisibilityOfElementLocatedCustom(locator));
   }
}
