package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.radio.RadioComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.RadioUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.RadioFieldTypes;

/**
 * Registry of radio UI elements for the Zero Bank demo application.
 *
 * locator and concrete component type (see {@link RadioComponentType}).
 *
 * for selecting and validating radio options.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .radio().select(RadioFields.DOLLARS_RADIO_FIELD);
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled radios via {@link RadioFieldTypes},
 * enabling consistent identification and interaction across flows.
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum RadioFields implements RadioUiElement {

   DOLLARS_RADIO_FIELD(PwBy.css("#pc_inDollars_true"), RadioFieldTypes.BOOTSTRAP_RADIO_TYPE);

   public static final class Data {

      public static final String DOLLARS_RADIO_FIELD = "DOLLARS_RADIO_FIELD";

      private Data() {
      }

   }

   private final PwBy locator;
   private final RadioComponentType componentType;


   RadioFields(final PwBy locator, final RadioComponentType componentType) {
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
