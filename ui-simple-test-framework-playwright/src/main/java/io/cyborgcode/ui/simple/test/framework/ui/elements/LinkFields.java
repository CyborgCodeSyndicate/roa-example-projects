package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.link.LinkComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.LinkUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.LinkFieldTypes;

/**
 * Registry of link UI elements for the Zero Bank demo application.
 *
 * locator and concrete component type (see {@link LinkComponentType}).
 * 
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

   TRANSFER_FUNDS_LINK(PwBy.css("#transfer_funds_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   ACCOUNT_ACTIVITY_LINK(PwBy.css("#account_activity_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   ACCOUNT_SUMMARY_LINK(PwBy.css("#account_summary_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   MY_MONEY_MAP_LINK(PwBy.css("#money_map_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   SP_PAYEE_DETAILS_LINK(PwBy.css("#sp_get_payee_details"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
   SETTINGS_LINK(PwBy.css("#settingsBox"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE);

   private final PwBy locator;
   private final LinkComponentType componentType;


   LinkFields(final PwBy locator, final LinkComponentType componentType) {
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
