package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableInfo;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Row projection model for the "Detailed Report" table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 *
 * <p>Integration:
 * <ul>
 *       (container: {@code #detailedreport-1041}).</li>
 *       and corresponding header locator to ensure stable column mapping.</li>
 *   <li>Used together with the {@code Tables.DETAILED_REPORT} element to project table rows
 *       into instances of this model for readable assertions.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
      tableContainerSelector = "#detailedreport-1041",
      rowsSelector = ".//tbody//tr//td/..",
      headerRowSelector = "#headercontainer-1042")
@NoArgsConstructor
@Getter
@Setter
public class DetailedReport {

   @TableCellLocator(cellSelector = "td:nth-of-type(1)",
         headerCellSelector = "#headercontainer-1042-targetEl > div:nth-child(1) span")
   private TableCell date;


   @TableCellLocator(cellSelector = "td:nth-of-type(2)",
         headerCellSelector = "#headercontainer-1042-targetEl > div:nth-child(2) span")
   private TableCell amount;
}
