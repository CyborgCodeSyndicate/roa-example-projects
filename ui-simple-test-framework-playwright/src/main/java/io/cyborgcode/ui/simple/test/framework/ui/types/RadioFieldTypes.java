package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.radio.RadioComponentType;

/**
 * Registry of radio component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific radio implementation technology (e.g., Bootstrap)
 * that can be used to tag radio elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code RadioBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap radios
 * @ImplementationOfType(type = RadioFieldTypes.Data.BOOTSTRAP_RADIO)
 * public class RadioBootstrapImpl implements RadioComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum RadioFieldTypes implements RadioComponentType {

   BOOTSTRAP_RADIO_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_RADIO = "BOOTSTRAP_RADIO_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
