package io.cyborgcode.ui.simple.test.framework.ui.components.alert;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.alert.Alert;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.ui.simple.test.framework.ui.types.AlertFieldTypes;

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

   private static final PwBy ALERT_CONTAINER_LOCATOR = PwBy.css(".alert");


   public AlertBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public String getValue(final PwElement container) {
      return container.locator(ALERT_CONTAINER_LOCATOR.selector()).innerText();
   }


   @Override
   public String getValue(final PwBy containerLocator) {
      return driver.locator(containerLocator.selector()).innerText();
   }


   @Override
   public boolean isVisible(final PwElement container) {
      return container.locator(ALERT_CONTAINER_LOCATOR.selector()).isVisible();
   }


   @Override
   public boolean isVisible(final PwBy containerLocator) {
      return driver.locator(ALERT_CONTAINER_LOCATOR.selector()).isVisible();
   }
}