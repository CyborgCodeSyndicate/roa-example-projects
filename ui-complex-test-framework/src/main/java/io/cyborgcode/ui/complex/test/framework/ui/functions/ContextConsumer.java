package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public interface ContextConsumer extends Consumer<SmartWebDriver> {

   /**
    Returns a Consumer that can be used to wait for the element at the given locator to be present
    in the DOM.
    <p>
    The returned Consumer will use the configured WebDriverWait of the SmartWebDriver to wait for the
    element, and will throw a NoSuchElementException if the element is not found.
    <p>
    This is a convenience method for tests that need to wait for the presence of an element, but do not
    need to perform any additional actions.
    <p>
    @param locator the By locator of the element to wait for
    @return a Consumer that can be used to wait for the element at the given locator
    */
   Consumer<SmartWebDriver> asConsumer(By locator);
}
