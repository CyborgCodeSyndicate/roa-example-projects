package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.input.InputComponentType;

/**
 * Registry of input field component type identifiers for the test framework.
 * <p>
 * Each enum constant represents a specific input field implementation technology (Material Design,
 * Bootstrap, Vaadin) that can be used to tag input elements. ROA's {@code @ImplementationOfType}
 * annotation uses these type identifiers to automatically select the correct component
 * implementation class at runtime.
 * </p>
 * <p>
 * Available types:
 * <ul>
 *   <li>{@link #MD_INPUT_TYPE} — Material Design input fields</li>
 *   <li>{@link #BOOTSTRAP_INPUT_TYPE} — Bootstrap-styled input fields</li>
 *   <li>{@link #VA_INPUT_TYPE} — Vaadin input fields (used in Bakery Flow)</li>
 * </ul>
 * </p>
 * <p>
 * The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code InputVaImpl}) to their type.
 * </p>
 */
public enum InputFieldTypes implements InputComponentType {

   MD_INPUT_TYPE,
   BOOTSTRAP_INPUT_TYPE,
   VA_INPUT_TYPE;

   public static final class Data {

      public static final String MD_INPUT = "MD_INPUT_TYPE";
      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
      public static final String VA_INPUT = "VA_INPUT_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }

}
