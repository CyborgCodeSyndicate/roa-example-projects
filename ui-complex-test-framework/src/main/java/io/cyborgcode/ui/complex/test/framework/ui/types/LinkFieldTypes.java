package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.link.LinkComponentType;

public enum LinkFieldTypes implements LinkComponentType {

   MD_LINK,
   BOOTSTRAP_LINK,
   VA_LINK;

   public static final class Data {

      public static final String MD_LINK = "MD_LINK";
      public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK";
      public static final String VA_LINK = "VA_LINK";

   }


   @Override
   public Enum getType() {
      return this;
   }
}
