package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.list.ItemListComponentType;

public enum ListFieldTypes implements ItemListComponentType {

   BOOTSTRAP_LIST_TYPE;


   public static final class Data {
      public static final String BOOTSTRAP_LIST = "BOOTSTRAP_LIST_TYPE";
   }


   @Override
   public Enum getType() {
      return this;
   }
}
