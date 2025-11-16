package io.cyborgcode.ui.complex.test.framework.ui.elements;

import io.cyborgcode.roa.ui.service.tables.DefaultTableTypes;
import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUiFunctions;
import io.cyborgcode.ui.complex.test.framework.ui.model.tables.TableEntry;
import io.cyborgcode.roa.ui.components.table.base.TableComponentType;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.tables.TableElement;

import java.util.function.Consumer;

/**
 * Registry of table UI elements for the test application.
 *
 * <p>Each enum constant defines a specific table by:
 *
 * <ul>
 *   <li>its row projection class (see {@link #rowsRepresentationClass()}),
 *   <li>an optional {@link TableComponentType} (e.g. {@link DefaultTableTypes#DEFAULT}),
 *   <li>optional {@code before}/{@code after} synchronization hooks as {@link Consumer}s of {@link SmartWebDriver}.
 * </ul>
 *
 * <p>Implements {@link TableElement} to integrate with ROA table service for locating tables and
 * working with strongly-typed row models (e.g., {@link TableEntry}).
 *
 * <p>Synchronization hooks commonly leverage {@link SharedUiFunctions} (e.g., {@code
 * waitForPresence(...)}) to handle dynamic loading before interacting with table content.
 *
 * <p>Typical usage targets Vaadin-styled tables while keeping interactions consistent and type-safe
 * through row representations and component typing.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
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
