package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.checkbox.CheckboxComponentType;

/**
 * Registry of checkbox component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific checkbox implementation technology (Material Design,
 * Bootstrap, Vaadin) that can be used to tag checkbox elements. ROA {@code @ImplementationOfType}
 * annotation uses these type identifiers to automatically select the correct component
 * implementation class at runtime.
 *
 * <p>Available types:
 *
 * <ul>
 *   <li>{@link #MD_CHECKBOX_TYPE} — Material Design checkboxes
 *   <li>{@link #BOOTSTRAP_CHECKBOX_TYPE} — Bootstrap-styled checkboxes
 *   <li>{@link #VA_CHECKBOX_TYPE} — Vaadin checkboxes
 * </ul>
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code CheckboxVaImpl}) to their type.
 *
 * <p>Example:
 *
 * <pre>{@code
 * // Implementation class for Vaadin checkboxes
 * @ImplementationOfType(type = CheckboxFieldTypes.Data.VA_CHECKBOX)
 * public class CheckboxVaImpl implements CheckboxComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum CheckboxFieldTypes implements CheckboxComponentType {
   MD_CHECKBOX_TYPE,
   BOOTSTRAP_CHECKBOX_TYPE,
   VA_CHECKBOX_TYPE;

   public static final class Data {

      public static final String MD_CHECKBOX = "MD_CHECKBOX_TYPE";
      public static final String BOOTSTRAP_CHECKBOX = "BOOTSTRAP_CHECKBOX_TYPE";
      public static final String VA_CHECKBOX = "VA_CHECKBOX_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }

}
