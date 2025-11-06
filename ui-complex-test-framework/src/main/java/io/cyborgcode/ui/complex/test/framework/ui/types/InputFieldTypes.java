package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.input.InputComponentType;

public enum InputFieldTypes implements InputComponentType {

   MD_INPUT,
   BOOTSTRAP_INPUT_TYPE,
   VA_INPUT_TYPE;

   public static final class Data {

      public static final String MD_INPUT = "MD_INPUT";
      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
      public static final String VA_INPUT = "VA_INPUT_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }

}
