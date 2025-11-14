package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.select.SelectComponentType;

/**
 * Registry of select (dropdown) component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific select implementation technology (e.g., Bootstrap)
 * that can be used to tag select elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code SelectBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap selects
 * @ImplementationOfType(type = SelectFieldTypes.Data.BOOTSTRAP_SELECT)
 * public class SelectBootstrapImpl implements SelectComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum SelectFieldTypes implements SelectComponentType {

   BOOTSTRAP_SELECT_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
