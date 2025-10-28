package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.loader.LoaderComponentType;

public enum LoaderFieldTypes implements LoaderComponentType {

   MD_LOADER_TYPE;


   public static final String MD_LOADER = "MD_LOADER_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
