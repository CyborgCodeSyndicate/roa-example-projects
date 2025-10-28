package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.alert.AlertComponentType;

public enum AlertFieldTypes implements AlertComponentType {

   MD_ALERT_TYPE,
   BOOTSTRAP_ALERT_TYPE;


   public static final String MD_ALERT = "MD_ALERT_TYPE";
   public static final String BOOTSTRAP_ALERT = "BOOTSTRAP_ALERT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
