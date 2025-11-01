package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.alert.AlertComponentType;

public enum AlertFieldTypes implements AlertComponentType {

   BOOTSTRAP_ALERT_TYPE;


   public static final String BOOTSTRAP_ALERT = "BOOTSTRAP_ALERT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
