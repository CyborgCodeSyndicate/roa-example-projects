package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.modal.ModalComponentType;

public enum ModalFieldTypes implements ModalComponentType {

   MD_MODAL_TYPE;


   public static final String MD_MODAL = "MD_MODAL_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
