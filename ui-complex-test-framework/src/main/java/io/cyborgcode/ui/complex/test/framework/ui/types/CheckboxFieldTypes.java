package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.checkbox.CheckboxComponentType;

public enum CheckboxFieldTypes implements CheckboxComponentType {

   MD_CHECKBOX_TYPE,
   VA_CHECKBOX_TYPE;


   public static final String MD_CHECKBOX = "MD_CHECKBOX_TYPE";
   public static final String VA_CHECKBOX = "VA_CHECKBOX_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
