package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.link.LinkComponentType;
import io.cyborgcode.roa.ui.selenium.LinkUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.LinkFieldTypes;
import org.openqa.selenium.By;

/**
 * Registry of link UI elements for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific link with its Selenium {@link By}
 * locator and concrete component type (see {@link LinkComponentType}).
 * 
 * <p>Implements {@link LinkUiElement} to integrate with ROA fluent UI testing API
 * for navigating and triggering link-driven flows.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .link().click(LinkFields.TRANSFER_FUNDS_LINK);
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled links via {@link LinkFieldTypes}, enabling
 * consistent identification and interaction across flows.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum LinkFields implements LinkUiElement {

   TRANSFER_FUNDS_LINK(By.id("transfer_funds_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   ACCOUNT_ACTIVITY_LINK(By.id("account_activity_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   ACCOUNT_SUMMARY_LINK(By.id("account_summary_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   MY_MONEY_MAP_LINK(By.id("money_map_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   SP_PAYEE_DETAILS_LINK(By.id("sp_get_payee_details"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   SETTINGS_LINK(By.id("settingsBox"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE);

   private final By locator;
   private final LinkComponentType componentType;


   LinkFields(final By locator, final LinkComponentType componentType) {
      this.locator = locator;
      this.componentType = componentType;
   }


   @Override
   public By locator() {
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
