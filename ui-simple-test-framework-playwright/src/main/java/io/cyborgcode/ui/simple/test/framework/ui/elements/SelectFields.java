package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.select.SelectComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.elements.SelectUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.SelectFieldTypes;

/**
 * Registry of select (dropdown) UI elements for the Zero Bank demo application.
 *
 * locator and concrete component type (see {@link SelectComponentType}).
 *
 * <p>Implements {@link SelectUiElement} to integrate with ROA fluent UI testing API
 * for selecting options and asserting dropdown state.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .select().selectOption(SelectFields.PC_CURRENCY_DDL, "Mexico (peso)");
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled selects via {@link SelectFieldTypes},
 * enabling consistent identification and interaction across flows.
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum SelectFields implements SelectUiElement {

   TF_FROM_ACCOUNT_DDL(PwBy.css("#tf_fromAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   TF_TO_ACCOUNT_DDL(PwBy.css("#tf_toAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   PC_CURRENCY_DDL(PwBy.css("#pc_currency"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   AA_TYPE_DDL(PwBy.css("#aa_type"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_PAYEE_DDL(PwBy.css("#sp_payee"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_ACCOUNT_DDL(PwBy.css("#sp_account"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE);


   public static final class Data {

      public static final String PC_CURRENCY_DDL = "PC_CURRENCY_DDL";

      private Data() {
      }

   }


   private final PwBy locator;
   private final SelectComponentType componentType;


   SelectFields(final PwBy locator, final SelectComponentType componentType) {
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
