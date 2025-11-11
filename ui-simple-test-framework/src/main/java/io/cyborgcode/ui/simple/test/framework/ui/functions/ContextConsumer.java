package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;

public interface ContextConsumer extends Consumer<SmartWebDriver> {

   Consumer<SmartWebDriver> asConsumer(By locator);
}
