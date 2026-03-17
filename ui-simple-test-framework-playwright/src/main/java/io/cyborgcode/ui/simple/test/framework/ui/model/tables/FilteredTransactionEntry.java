package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableInfo;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Row projection model for the "Filtered Transactions" table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 *
 * <p>Integration:
 * <ul>
 *       (container: {@code #filtered_transactions_for_account}).</li>
 *       and corresponding header locator to ensure stable column mapping.</li>
 *   <li>Used together with the {@code Tables.FILTERED_TRANSACTIONS} element to project table rows
 *       into instances of this model for readable assertions.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
      tableContainerSelector = "#filtered_transactions_for_account",
      rowsSelector = "tbody tr",
      headerRowSelector = "thead tr")
@NoArgsConstructor
@Getter
@Setter
public class FilteredTransactionEntry {


   @TableCellLocator(cellSelector = "td:nth-of-type(1)",
         headerCellSelector = "th:nth-of-type(1)")
   private TableCell date;


   @TableCellLocator(cellSelector = "td:nth-of-type(2)",
         headerCellSelector = "th:nth-of-type(2)")
   private TableCell description;


   @TableCellLocator(cellSelector = "td:nth-of-type(3)",
         headerCellSelector = "th:nth-of-type(3)")
   private TableCell deposit;


   @TableCellLocator(cellSelector = "td:nth-of-type(4)",
         headerCellSelector = "th:nth-of-type(4)")
   private TableCell withdrawal;

}
