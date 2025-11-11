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
