package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.button.ButtonComponentType;

public enum ButtonFieldTypes implements ButtonComponentType {

   BOOTSTRAP_INPUT_TYPE;


   public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
