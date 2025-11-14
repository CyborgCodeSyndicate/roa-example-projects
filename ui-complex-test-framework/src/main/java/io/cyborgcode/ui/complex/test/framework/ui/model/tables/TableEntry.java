package io.cyborgcode.ui.complex.test.framework.ui.model.tables;

import io.cyborgcode.roa.ui.components.table.annotations.*;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

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
