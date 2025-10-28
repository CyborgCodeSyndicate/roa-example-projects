package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
