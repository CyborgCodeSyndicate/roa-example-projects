package io.cyborgcode.ui.simple.test.framework.ui.components.alert;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.alert.Alert;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.AlertFieldTypes;
import org.openqa.selenium.By;

/**
 * Bootstrap-specific implementation of the {@link Alert} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap alert containers
 * (elements with the {@code alert} CSS class). It is automatically selected by ROA
 * component resolution mechanism when an alert field is tagged with
 * {@link AlertFieldTypes#BOOTSTRAP_ALERT_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Read alert text from a provided container or a direct locator</li>
 *   <li>Check alert visibility by safely probing for the alert container</li>
 *   <li>Use Bootstrap’s {@code .alert} class to resolve the alert element consistently</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s DOM and class-based structure to ensure
 * reliable retrieval and visibility checks for alerts in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
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