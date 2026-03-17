package io.cyborgcode.ui.simple.test.framework.ui.elements;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.components.table.base.TableComponentType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.service.tables.TableElement;
import io.cyborgcode.roa.ui.service.tables.DefaultTableTypes;
import io.cyborgcode.ui.simple.test.framework.ui.SharedUiFunctions;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.AllTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.CreditAccounts;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.DetailedReport;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.FilteredTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.OutFlow;
import java.util.function.Consumer;

import static io.cyborgcode.roa.ui.service.tables.DefaultTableTypes.DEFAULT;


/**
 * Registry of table UI elements for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific table by:
 * <ul>
 *   <li>its row projection class (see {@link #rowsRepresentationClass()}),</li>
 *   <li>an optional {@link TableComponentType} (e.g. {@link DefaultTableTypes#DEFAULT}),</li>
 * </ul>
 *
 * and working with strongly-typed row models (e.g., {@link FilteredTransactionEntry}, {@link CreditAccounts}).
 *
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
         driver -> SharedUiFunctions.waitForPresence(driver, PwBy.css("#report-1016"))),
   DETAILED_REPORT(DetailedReport.class, DEFAULT,
         driver -> SharedUiFunctions.waitForPresence(driver, PwBy.css("#detailedreport-1041")));


   private final Class<?> rowRepresentationClass;
   private final TableComponentType tableType;
   private final Consumer<Page> before;
   private final Consumer<Page> after;


   <T> Tables(final Class<T> rowRepresentationClass) {
      this(rowRepresentationClass, null, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }

   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
              Consumer<Page> before) {
      this(rowRepresentationClass, tableType, before, smartWebDriver -> {
      });
   }


   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
              Consumer<Page> before, Consumer<Page> after) {
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
   public Consumer<Page> before() {
      return before;
   }


   @Override
   public Consumer<Page> after() {
      return after;
   }

}
