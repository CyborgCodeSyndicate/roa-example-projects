package io.cyborgcode.ui.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.select.SelectComponentType;
import io.cyborgcode.roa.ui.selenium.SelectUiElement;
import io.cyborgcode.ui.test.framework.ui.types.SelectFieldTypes;
import org.openqa.selenium.By;

public enum SelectFields implements SelectUiElement {

   TF_FROM_ACCOUNT_DDL(By.id("tf_fromAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   TF_TO_ACCOUNT_DDL(By.id("tf_toAccountId"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   PC_CURRENCY_DDL(By.id("pc_currency"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   AA_TYPE_DDL(By.id("aa_type"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_PAYEE_DDL(By.id("sp_payee"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE),
   SP_ACCOUNT_DDL(By.id("sp_account"), SelectFieldTypes.BOOTSTRAP_SELECT_TYPE);

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
