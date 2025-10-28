package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.tab.TabComponentType;

public enum TabFieldTypes implements TabComponentType {

   MD_TAB_TYPE,
   BOOTSTRAP_TAB_TYPE;


   public static final String MD_TAB = "MD_TAB_TYPE";
   public static final String BOOTSTRAP_TAB = "BOOTSTRAP_TAB_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
