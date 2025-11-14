package io.cyborgcode.ui.simple.test.framework.ui.functions;

import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;

/**
 * Functional interface for UI synchronization hooks that accept both driver and locator context.
 *
 * <p>This interface extends {@link Consumer}{@code <SmartWebDriver>} to enable use as before/after
 * hooks in UI element definitions, while also providing {@link #asConsumer(By)} to bind a specific
 * locator to the wait function.
 *
 * <p>Implementations (like {@link SharedUi}) can be used in two ways:
 * <ul>
 *   <li>Directly as a {@code Consumer<SmartWebDriver>} when no locator is needed</li>
 *   <li>Via {@code asConsumer(locator)} to create a locator-specific consumer</li>
 * </ul>
 *
 * <p> This pattern allows flexible reuse of wait strategies across different UI elements,
 * supporting both element-specific waits (with locator) and global waits (without locator).
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ContextConsumer extends Consumer<SmartWebDriver> {

   Consumer<SmartWebDriver> asConsumer(By locator);
}
