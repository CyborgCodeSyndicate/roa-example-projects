package io.cyborgcode.ui.simple.test.framework.ui.components.alert;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.alert.Alert;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.AlertFieldTypes;
import org.openqa.selenium.By;

@ImplementationOfType(AlertFieldTypes.Data.BOOTSTRAP_ALERT)
public class AlertBootstrapImpl extends BaseComponent implements Alert {

   private static final By ALERT_CONTAINER_LOCATOR = By.className("alert");


   public AlertBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public String getValue(final SmartWebElement container) {
      return container.findSmartElement(ALERT_CONTAINER_LOCATOR).getText();
   }


   @Override
   public String getValue(final By containerLocator) {
      return driver.findSmartElement(containerLocator).getText();
   }


   @Override
   public boolean isVisible(final SmartWebElement container) {
      return driver.checkNoException(() -> container.findSmartElement(ALERT_CONTAINER_LOCATOR));
   }


   @Override
   public boolean isVisible(final By containerLocator) {
      return driver.checkNoException(() -> driver.findSmartElement(containerLocator));
   }
}