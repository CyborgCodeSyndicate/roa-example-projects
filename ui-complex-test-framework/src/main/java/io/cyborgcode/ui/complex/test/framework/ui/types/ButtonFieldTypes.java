package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.button.ButtonComponentType;

/**
 * Registry of button component type identifiers for the test framework.
 * <p>
 * Each enum constant represents a specific button implementation technology (Material Design,
 * Bootstrap, Vaadin) that can be used to tag button elements. ROA's {@code @ImplementationOfType}
 * annotation uses these type identifiers to automatically select the correct component
 * implementation class at runtime.
 * </p>
 * <p>
 * Available types:
 * <ul>
 *   <li>{@link #MD_BUTTON_TYPE} — Material Design buttons</li>
 *   <li>{@link #BOOTSTRAP_INPUT_TYPE} — Bootstrap-styled buttons</li>
 *   <li>{@link #VA_BUTTON_TYPE} — Vaadin buttons (used in Bakery Flow)</li>
 * </ul>
 * </p>
 * <p>
 * The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code ButtonVaImpl}) to their type.
 * </p>
 */
public enum ButtonFieldTypes implements ButtonComponentType {

   MD_BUTTON_TYPE, //Material Design
   BOOTSTRAP_INPUT_TYPE,
   VA_BUTTON_TYPE; //Vaadin

   public static final class Data {

      public static final String MD_BUTTON = "MD_BUTTON_TYPE";
      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
      public static final String VA_BUTTON = "VA_BUTTON_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }

}
