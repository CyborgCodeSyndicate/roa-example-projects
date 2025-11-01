package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.radio.RadioComponentType;

public enum RadioFieldTypes implements RadioComponentType {

   BOOTSTRAP_RADIO_TYPE;


   public static final String BOOTSTRAP_RADIO = "BOOTSTRAP_RADIO_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
