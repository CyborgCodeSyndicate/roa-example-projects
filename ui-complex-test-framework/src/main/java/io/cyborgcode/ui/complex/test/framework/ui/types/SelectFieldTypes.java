package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.select.SelectComponentType;

public enum SelectFieldTypes implements SelectComponentType {

   MD_SELECT_TYPE,
   BOOTSTRAP_SELECT_TYPE,
   VA_SELECT_TYPE;


   public static final String MD_SELECT = "MD_SELECT_TYPE";
   public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";
   public static final String VA_SELECT = "VA_SELECT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
