package io.cyborgcode.ui.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.link.LinkComponentType;

public enum LinkFieldTypes implements LinkComponentType {

   BOOTSTRAP_LINK_TYPE;

   public static final class Data {

      public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK_TYPE";
   }


   @Override
   public Enum getType() {
      return this;
   }
}
