package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.select.SelectComponentType;

public enum SelectFieldTypes implements SelectComponentType {

   BOOTSTRAP_SELECT_TYPE;


   public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
