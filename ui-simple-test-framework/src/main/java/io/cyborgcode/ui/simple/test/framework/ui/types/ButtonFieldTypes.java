package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.button.ButtonComponentType;

/**
 * Registry of button component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific button implementation technology (e.g., Bootstrap)
 * that can be used to tag button elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code ButtonBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap buttons
 * @ImplementationOfType(type = ButtonFieldTypes.Data.BOOTSTRAP_BUTTON)
 * public class ButtonBootstrapImpl implements ButtonComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ButtonFieldTypes implements ButtonComponentType {

   BOOTSTRAP_BUTTON_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_BUTTON = "BOOTSTRAP_BUTTON_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
