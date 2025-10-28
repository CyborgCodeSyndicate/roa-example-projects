package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.button.ButtonComponentType;

public enum ButtonFieldTypes implements ButtonComponentType {

   MD_BUTTON_TYPE,
   BOOTSTRAP_INPUT_TYPE,
   VA_BUTTON_TYPE;


   public static final String MD_BUTTON = "MD_BUTTON_TYPE";
   public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
   public static final String VA_BUTTON = "VA_BUTTON_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
