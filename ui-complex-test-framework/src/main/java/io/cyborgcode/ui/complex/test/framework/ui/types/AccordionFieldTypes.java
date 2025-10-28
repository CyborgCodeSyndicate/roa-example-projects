package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.accordion.AccordionComponentType;

public enum AccordionFieldTypes implements AccordionComponentType {

   MD_ACCORDION_TYPE;


   public static final String MD_ACCORDION = "MD_ACCORDION_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
