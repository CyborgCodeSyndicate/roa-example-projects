package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.input.InputComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.InputUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.InputFieldTypes;

/**
 * Registry of input UI elements for the Zero Bank demo application.
 *
 *
 * clearing, and validating input values.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .input().insert(USERNAME_FIELD, "admin");
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled inputs via {@link InputFieldTypes}, enabling
 * consistent identification and interaction across flows.
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum InputFields implements InputUiElement {

   USERNAME_FIELD(PwBy.css("#user_login"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   PASSWORD_FIELD(PwBy.css("#user_password"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AMOUNT_FIELD(PwBy.css("#tf_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AMOUNT_CURRENCY_FIELD(PwBy.css("#pc_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   TF_DESCRIPTION_FIELD(PwBy.css("#tf_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_DESCRIPTION_FIELD(PwBy.css("#aa_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_FROM_DATE_FIELD(PwBy.css("#aa_fromDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_TO_DATE_FIELD(PwBy.css("#aa_toDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_FROM_AMOUNT_FIELD(PwBy.css("#aa_fromAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_TO_AMOUNT_FIELD(PwBy.css("#aa_toAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_AMOUNT_FIELD(PwBy.css("#sp_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_DATE_FIELD(PwBy.css("#sp_date"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_DESCRIPTION_FIELD(PwBy.css("#sp_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE);


   public static final class Data {

      public static final String AMOUNT_CURRENCY_FIELD = "AMOUNT_CURRENCY_FIELD";

      private Data() {
      }

   }


   private final PwBy locator;
   private final InputComponentType componentType;


   InputFields(final PwBy locator, final InputComponentType componentType) {
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
