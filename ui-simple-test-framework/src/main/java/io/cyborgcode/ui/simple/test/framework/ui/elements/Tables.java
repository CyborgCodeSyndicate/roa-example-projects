package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.table.base.TableComponentType;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.tables.DefaultTableTypes;
import io.cyborgcode.roa.ui.service.tables.TableElement;
import io.cyborgcode.ui.simple.test.framework.ui.functions.SharedUiFunctions;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.AllTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.CreditAccounts;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.DetailedReport;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.FilteredTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.OutFlow;
import java.util.function.Consumer;
import org.openqa.selenium.By;

import static io.cyborgcode.roa.ui.service.tables.DefaultTableTypes.DEFAULT;


/**
 * Registry of table UI elements for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific table by:
 * <ul>
 *   <li>its row projection class (see {@link #rowsRepresentationClass()}),</li>
 *   <li>an optional {@link TableComponentType} (e.g. {@link DefaultTableTypes#DEFAULT}),</li>
 *   <li>optional {@code before}/{@code after} synchronization hooks as {@link Consumer}s of {@link SmartWebDriver}.</li>
 * </ul>
 *
 * <p>Implements {@link TableElement} to integrate with ROA table service for locating tables
 * and working with strongly-typed row models (e.g., {@link FilteredTransactionEntry}, {@link CreditAccounts}).
 *
 * <p>Synchronization hooks commonly leverage {@link SharedUiFunctions}
 * (e.g., {@code waitForPresence(...)}) to handle dynamic loading before interacting with table content.
 *
 * <p>Typical usage targets Bootstrap-styled tables while keeping interactions consistent and type-safe
 * through row representations and component typing.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum Tables implements TableElement<Tables> {

   FILTERED_TRANSACTIONS(FilteredTransactionEntry.class),
   ALL_TRANSACTIONS(AllTransactionEntry.class),
   CREDIT_ACCOUNTS(CreditAccounts.class),
   OUTFLOW(OutFlow.class, DEFAULT,
         driver -> SharedUiFunctions.waitForPresence(driver, By.id("report-1016"))),
   DETAILED_REPORT(DetailedReport.class, DEFAULT,
         driver -> SharedUiFunctions.waitForPresence(driver, By.id("detailedreport-1041")));


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
              Consumer<SmartWebDriver> before) {
      this(rowRepresentationClass, tableType, before, smartWebDriver -> {
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
