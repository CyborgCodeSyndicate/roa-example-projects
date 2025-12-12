package io.cyborgcode.ui.complex.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.table.annotations.*;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

/**
 * Row projection model for the "All Transactions" table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table annotations. The
 * class is wired to the table DOM via {@link TableInfo}, and each field is bound to a specific cell
 * and header using {@link TableCellLocator}.
 *
 * <p>Integration:
 *
 * <ul>
 *   <li>{@link TableInfo} declares the table container, rows, and header row locators (container:
 *       {@code table#table}).
 *   <li>{@link TableCellLocator} binds each {@link TableCell} to its column cell locator and
 *       corresponding header locator to ensure stable column mapping.
 *   <li>Used together with the {@code Tables.ORDERS} element to project table rows into
 *       instances of this model for readable assertions.
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
    tableContainerLocator = @FindBy(css = "table#table"),
    rowsLocator = @FindBy(css = "tbody tr"),
    headerRowLocator = @FindBy(css = "thead tr"))
@Setter
@NoArgsConstructor
@Getter
public class TableEntry {

   @TableCellLocator(cellLocator = @FindBy(tagName = "td"), headerCellLocator = @FindBy(tagName = "th"))
   private TableCell row;

}
