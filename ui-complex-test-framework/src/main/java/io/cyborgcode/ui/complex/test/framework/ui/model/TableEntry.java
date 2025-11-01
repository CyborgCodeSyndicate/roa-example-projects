package io.cyborgcode.ui.complex.test.framework.ui.model;

import io.cyborgcode.ui.complex.test.framework.ui.types.InputFieldTypes;
import io.cyborgcode.roa.ui.components.input.InputComponentType;
import io.cyborgcode.roa.ui.components.table.annotations.*;
import io.cyborgcode.roa.ui.components.table.insertion.CellInsertionFunction;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@TableInfo(
      tableContainerLocator = @FindBy(className = "table-class"),
      rowsLocator = @FindBy(tagName = "tr"),
      headerRowLocator = @FindBy(className = "header"))
@Setter
@Builder
@Getter
public class TableEntry {


   @TableCellLocator(cellLocator = @FindBy(id = "select"), headerCellLocator = @FindBy(id = "select"))
   private TableCell selectRow;

   @CellFilter(type = InputComponentType.class, componentType = InputFieldTypes.Data.MD_INPUT)
   @CustomCellInsertion(insertionFunction = CustomInsertion.class, order = 1)
   @TableCellLocator(cellLocator = @FindBy(id = "name"), headerCellLocator = @FindBy(id = "header-name"))
   private TableCell studentName;


   @CellInsertion(type = InputComponentType.class, componentType = InputFieldTypes.Data.MD_INPUT, order = 2)
   @TableCellLocator(cellLocator = @FindBy(id = "surname"), headerCellLocator = @FindBy(id = "header-surname"))
   private List<TableCell> studentSurname;


   private static class CustomInsertion implements CellInsertionFunction {

      @Override
      public void cellInsertionFunction(final SmartWebElement cellElement, final String... values) {
         cellElement.findSmartElement(By.className("dsadas")).sendKeys(values);
      }

   }


}
