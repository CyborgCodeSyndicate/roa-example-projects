package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public interface ContextConsumer extends Consumer<SmartWebDriver> {

   Consumer<SmartWebDriver> asConsumer(By locator);
}
