package io.cyborgcode.ui.simple.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.table.annotations.CustomCellInsertion;
import io.cyborgcode.roa.ui.components.table.insertion.CellInsertionFunction;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableCellLocator;
import io.cyborgcode.roa.ui.playwright.components.table.annotations.TableInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Row projection model for the "Outflow" report table.
 *
 * <p>This model maps a single table row into strongly-typed cells using ROA table
 * and each field is bound to a specific cell and header using
 *
 * <p>Integration:
 * <ul>
 *       (container: {@code #report-1016}).</li>
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
      tableContainerSelector = "#report-1016",
      rowsSelector = "(//tbody)[4]//tr//td/..",
      headerRowSelector = "#headercontainer-1017")
@NoArgsConstructor
@Getter
@Setter
public class OutFlow {

   @TableCellLocator(cellSelector = "td:nth-of-type(1)",
         headerCellSelector = "#headercontainer-1017-targetEl > div:nth-child(1) span")
   private TableCell category;


   @TableCellLocator(cellSelector = "td:nth-of-type(2)",
         headerCellSelector = "#headercontainer-1017-targetEl > div:nth-child(2) span")
   private TableCell amount;


   @CustomCellInsertion(insertionFunction = CustomClickButton.class)
   @TableCellLocator(cellSelector = "td:nth-of-type(3)",
         headerCellSelector = "#headercontainer-1017-targetEl > div:nth-child(3) span")
   private TableCell details;


   private static class CustomClickButton implements CellInsertionFunction<PwElement> {//todo: Check this interface impl
//todo: CellInsertionFunctionCore -> Sel, PW
      @Override
      public void cellInsertionFunction(PwElement cellElement, String... values) {
         cellElement.locator("img").click();
      }

      @Override
      public void accept(PwElement element, String[] objects) {
         CellInsertionFunction.super.accept(element, objects);
      }
   }

}
