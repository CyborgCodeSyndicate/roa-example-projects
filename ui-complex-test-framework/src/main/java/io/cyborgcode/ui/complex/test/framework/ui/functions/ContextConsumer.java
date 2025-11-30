package io.cyborgcode.ui.complex.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

/**
 * Functional interface for UI synchronization hooks that accept both driver and locator context.
 *
 * <p>This interface extends {@link Consumer}{@code <SmartWebDriver>} to enable use as before/after
 * hooks in UI element definitions, while also providing {@link #asConsumer(By)} to bind a specific
 * locator to the wait function.
 *
 * <p>Implementations (like {@link SharedUi}) can be used in two ways:
 *
 * <ul>
 *   <li>Directly as a {@code Consumer<SmartWebDriver>} when no locator is needed
 *   <li>Via {@code asConsumer(locator)} to create a locator-specific consumer
 * </ul>
 *
 * <p>This pattern allows flexible reuse of wait strategies across different UI elements, supporting
 * both element-specific waits (with locator) and global waits (without locator).
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ContextConsumer extends Consumer<SmartWebDriver> {

   /**
    * Returns a Consumer that can be used to wait for the element at the given locator to be present in the DOM.
    * The returned Consumer will use the configured WebDriverWait of the SmartWebDriver to wait for the
    * element, and will throw a NoSuchElementException if the element is not found.
    * This is a convenience method for tests that need to wait for the presence of an element, but do not
    * need to perform any additional actions.
    * @param locator the By locator of the element to wait for
    * @return a Consumer that can be used to wait for the element at the given locator
    */
   Consumer<SmartWebDriver> asConsumer(By locator);
}
