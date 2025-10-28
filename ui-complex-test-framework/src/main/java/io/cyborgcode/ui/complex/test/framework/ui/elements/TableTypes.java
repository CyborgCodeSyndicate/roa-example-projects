package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.table.base.TableComponentType;

public enum TableTypes implements TableComponentType {

   SIMPLE;


   @Override
   public Enum<?> getType() {
      return this;
   }
}
