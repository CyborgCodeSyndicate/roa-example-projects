package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.input.InputComponentType;

/**
 * Registry of input component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific input implementation technology (e.g., Bootstrap)
 * that can be used to tag input elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code InputBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap inputs
 * @ImplementationOfType(type = InputFieldTypes.Data.BOOTSTRAP_INPUT)
 * public class InputBootstrapImpl implements InputComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum InputFieldTypes implements InputComponentType {

   BOOTSTRAP_INPUT_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum<?> getType() {
      return this;
   }
}
