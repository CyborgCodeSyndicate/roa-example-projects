package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.components.table.annotations.TableInfo;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

/**
 * Row projection model for the "Detailed Report" table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 * annotations. The class is wired to the table DOM via {@link TableInfo}, and each
 * field is bound to a specific cell and header using {@link TableCellLocator}.
 *
 * <p>Integration:
 * <ul>
 *   <li>{@link TableInfo} declares the table container, rows, and header row locators
 *       (container: {@code #detailedreport-1041}).</li>
 *   <li>{@link TableCellLocator} binds each {@link TableCell} to its column cell locator
 *       and corresponding header locator to ensure stable column mapping.</li>
 *   <li>Used together with the {@code Tables.DETAILED_REPORT} element to project table rows
 *       into instances of this model for readable assertions.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
      tableContainerLocator = @FindBy(id = "detailedreport-1041"),
      rowsLocator = @FindBy(xpath = ".//tbody//tr//td/.."),
      headerRowLocator = @FindBy(id = "headercontainer-1042"))
@NoArgsConstructor
@Getter
@Setter
public class DetailedReport {

   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1042-targetEl > div:nth-child(1) span"))
   private TableCell date;


   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1042-targetEl > div:nth-child(2) span"))
   private TableCell amount;
}
