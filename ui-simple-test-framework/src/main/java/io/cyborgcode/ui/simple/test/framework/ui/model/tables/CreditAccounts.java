package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.link.LinkComponentType;
import io.cyborgcode.roa.ui.components.table.annotations.CellInsertion;
import io.cyborgcode.roa.ui.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.components.table.annotations.TableInfo;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import io.cyborgcode.ui.simple.test.framework.ui.types.LinkFieldTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

/**
 * Row projection model for the "Credit Accounts" table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 * annotations. The class is wired to the table DOM via {@link TableInfo}, and each
 * field is bound to a specific cell and header using {@link TableCellLocator}.
 *
 * <p>Integration:
 * <ul>
 *   <li>{@link TableInfo} declares the table container, rows,
 *       and header row locators (container: third {@code .board-content} section).</li>
 *   <li>{@link TableCellLocator} binds each
 *       {@link TableCell} to its column cell locator and corresponding
 *       header locator to ensure stable column mapping.</li>
 *   <li>{@link CellInsertion} on {@code account} enables link interaction within that cell using
 *       {@link LinkComponentType} with {@code componentType = LinkFieldTypes.Data.BOOTSTRAP_LINK}.</li>
 *   <li>Used together with the {@code Tables.CREDIT_ACCOUNTS} element to project table rows into instances of this
 *       model for readable assertions.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
      tableContainerLocator = @FindBy(xpath = "(//div[@class='board-content'])[3]"),
      rowsLocator = @FindBy(css = "tbody tr"),
      headerRowLocator = @FindBy(css = "thead tr"))
@NoArgsConstructor
@Getter
@Setter
public class CreditAccounts {

   @CellInsertion(type = LinkComponentType.class, componentType = LinkFieldTypes.Data.BOOTSTRAP_LINK)
   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
         headerCellLocator = @FindBy(css = "th:nth-of-type(1)"))
   private TableCell account;


   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
         headerCellLocator = @FindBy(css = "th:nth-of-type(2)"))
   private TableCell creditCard;


   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(3)"),
         headerCellLocator = @FindBy(css = "th:nth-of-type(3)"))
   private TableCell balance;

}
