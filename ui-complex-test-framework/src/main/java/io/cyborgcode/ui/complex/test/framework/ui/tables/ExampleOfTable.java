package io.cyborgcode.ui.complex.test.framework.ui.tables;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.table.service.TableImpl;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;

@ImplementationOfType("SIMPLE")
public class ExampleOfTable extends TableImpl {

   protected ExampleOfTable(final SmartWebDriver smartWebDriver) {
      super(smartWebDriver);
   }


}

