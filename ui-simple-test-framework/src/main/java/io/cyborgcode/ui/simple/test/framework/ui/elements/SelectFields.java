package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.select.SelectComponentType;
import io.cyborgcode.roa.ui.selenium.SelectUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.SelectFieldTypes;
import org.openqa.selenium.By;

/**
 * Registry of select (dropdown) UI elements for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific select control with its Selenium {@link By}
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

   TF_FROM_ACCOUNT_DDL(By.id("tf_fromAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   TF_TO_ACCOUNT_DDL(By.id("tf_toAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   PC_CURRENCY_DDL(By.id("pc_currency"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   AA_TYPE_DDL(By.id("aa_type"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_PAYEE_DDL(By.id("sp_payee"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_ACCOUNT_DDL(By.id("sp_account"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE);


   public static final class Data {

      public static final String PC_CURRENCY_DDL = "PC_CURRENCY_DDL";

      private Data() {
      }

   }


   private final By locator;
   private final SelectComponentType componentType;


   SelectFields(final By locator, final SelectComponentType componentType) {
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
