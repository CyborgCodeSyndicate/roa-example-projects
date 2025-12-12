package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.table.annotations.CustomCellInsertion;
import io.cyborgcode.roa.ui.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.components.table.annotations.TableInfo;
import io.cyborgcode.roa.ui.components.table.insertion.CellInsertionFunction;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * Row projection model for the "Outflow" report table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 * annotations. The class is wired to the table DOM via {@link TableInfo},
 * and each field is bound to a specific cell and header using
 * {@link TableCellLocator}.
 *
 * <p>Integration:
 * <ul>
 *   <li>{@link TableInfo} declares the table container, rows, and header row locators
 *       (container: {@code #report-1016}).</li>
 *   <li>{@link TableCellLocator} binds each {@link TableCell} to its column cell locator
 *       and corresponding header locator to ensure stable column mapping.</li>
 *   <li>{@link CustomCellInsertion} on {@code details} uses {@code CustomClickButton}
 *       to trigger an action inside the cell (clicks an {@code <img>} element).</li>
 *   <li>Used together with the {@code Tables.OUTFLOW} element to project table rows into instances of this
 *       model for readable assertions.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TableInfo(
      tableContainerLocator = @FindBy(id = "report-1016"),
      rowsLocator = @FindBy(xpath = "(//tbody)[4]//tr//td/.."),
      headerRowLocator = @FindBy(id = "headercontainer-1017"))
@NoArgsConstructor
@Getter
@Setter
public class OutFlow {

   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(1) span"))
   private TableCell category;


   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(2) span"))
   private TableCell amount;


   @CustomCellInsertion(insertionFunction = CustomClickButton.class)
   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(3)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(3) span"))
   private TableCell details;


   private static class CustomClickButton implements CellInsertionFunction {

      @Override
      public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
         cellElement.findSmartElement(By.tagName("img")).click();
      }
   }

}
