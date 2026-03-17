package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.alert.AlertComponentType;
import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.AlertUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.AlertFieldTypes;

/**
 * Registry of alert UI elements for the Zero Bank demo application.
 *
 * locator and concrete component type (see {@link AlertComponentType}).
 *
 * <p>Implements {@link AlertUiElement} to integrate with
 * ROA fluent UI testing API for resolving and asserting alert messages.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Dollar")
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled alert banners via {@link AlertFieldTypes},
 * enabling consistent identification and validation of success/error/info messages across flows.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum AlertFields implements AlertUiElement {

   SUBMITTED_TRANSACTION(PwBy.css(".alert"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE),
   FOREIGN_CURRENCY_CASH(PwBy.css("#alert_content"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE),
   PAYMENT_MESSAGE(PwBy.css("#alert_content"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE);

   private final PwBy locator;
   private final AlertComponentType componentType;


   AlertFields(final PwBy locator, final AlertComponentType componentType) {
      this.locator = locator;
      this.componentType = componentType;
   }


   @Override
   public PwBy locator() {
      return locator;
   }


   @Override
   public <T extends ComponentType> T componentType() {
      return (T) componentType;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }

}
