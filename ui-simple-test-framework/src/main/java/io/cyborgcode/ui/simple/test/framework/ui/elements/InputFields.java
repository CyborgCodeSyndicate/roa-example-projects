package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.input.InputComponentType;
import io.cyborgcode.roa.ui.selenium.InputUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.InputFieldTypes;
import org.openqa.selenium.By;

public enum InputFields implements InputUiElement {

   USERNAME_FIELD(By.id("user_login"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   PASSWORD_FIELD(By.id("user_password"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AMOUNT_FIELD(By.id("tf_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AMOUNT_CURRENCY_FIELD(By.id("pc_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   TF_DESCRIPTION_FIELD(By.id("tf_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_DESCRIPTION_FIELD(By.id("aa_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_FROM_DATE_FIELD(By.id("aa_fromDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_TO_DATE_FIELD(By.id("aa_toDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_FROM_AMOUNT_FIELD(By.id("aa_fromAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   AA_TO_AMOUNT_FIELD(By.id("aa_toAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_AMOUNT_FIELD(By.id("sp_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_DATE_FIELD(By.id("sp_date"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
   SP_DESCRIPTION_FIELD(By.id("sp_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE);


   public static final class Data {

      public static final String USERNAME_FIELD = "USERNAME_FIELD";
      public static final String PASSWORD_FIELD = "PASSWORD_FIELD";
      public static final String AMOUNT_FIELD = "AMOUNT_FIELD";
      public static final String AMOUNT_CURRENCY_FIELD = "AMOUNT_CURRENCY_FIELD";
      public static final String TF_DESCRIPTION_FIELD = "TF_DESCRIPTION_FIELD";
      public static final String AA_DESCRIPTION_FIELD = "AA_DESCRIPTION_FIELD";
      public static final String AA_FROM_DATE_FIELD = "AA_FROM_DATE_FIELD";
      public static final String AA_TO_DATE_FIELD = "AA_TO_DATE_FIELD";
      public static final String AA_FROM_AMOUNT_FIELD = "AA_FROM_AMOUNT_FIELD";
      public static final String AA_TO_AMOUNT_FIELD = "AA_TO_AMOUNT_FIELD";
      public static final String SP_AMOUNT_FIELD = "SP_AMOUNT_FIELD";
      public static final String SP_DATE_FIELD = "SP_DATE_FIELD";
      public static final String SP_DESCRIPTION_FIELD = "SP_DESCRIPTION_FIELD";


      private Data() {
      }

   }


   private final By locator;
   private final InputComponentType componentType;


   InputFields(final By locator, final InputComponentType componentType) {
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
