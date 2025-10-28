package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.toggle.ToggleComponentType;

public enum ToggleFieldTypes implements ToggleComponentType {

   MD_TOGGLE_TYPE;


   public static final String MD_TOGGLE = "MD_TOGGLE_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
