package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.ui.complex.test.framework.ui.model.tables.TableEntry;
import io.cyborgcode.roa.ui.components.table.base.TableComponentType;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.tables.TableElement;

import java.util.function.Consumer;

public enum Tables implements TableElement<Tables> {

   ORDERS(TableEntry.class);

   public static final class Data {

      public static final String ORDERS = "ORDERS";

      private Data() {
      }

   }

   private final Class<?> rowRepresentationClass;
   private final TableComponentType tableType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;

   <T> Tables(final Class<T> rowRepresentationClass) {
      this(rowRepresentationClass, null, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
         Consumer<SmartWebDriver> before, Consumer<SmartWebDriver> after) {
      this.rowRepresentationClass = rowRepresentationClass;
      this.tableType = tableType;
      this.before = before;
      this.after = after;
   }

   @Override
   public <T extends TableComponentType> T tableType() {
      if (tableType != null) {
         return (T) tableType;
      } else {
         return TableElement.super.tableType();
      }
   }

   @Override
   public <T> Class<T> rowsRepresentationClass() {
      return (Class<T>) rowRepresentationClass;
   }

   @Override
   public Tables enumImpl() {
      return this;
   }

   @Override
   public Consumer<SmartWebDriver> before() {
      return before;
   }

   @Override
   public Consumer<SmartWebDriver> after() {
      return after;
   }

}
