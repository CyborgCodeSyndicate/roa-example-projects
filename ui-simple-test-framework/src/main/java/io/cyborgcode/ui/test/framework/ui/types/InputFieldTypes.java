package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.input.InputComponentType;

public enum InputFieldTypes implements InputComponentType {

   BOOTSTRAP_INPUT_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
   }


   @Override
   public Enum<?> getType() {
      return this;
   }
}
