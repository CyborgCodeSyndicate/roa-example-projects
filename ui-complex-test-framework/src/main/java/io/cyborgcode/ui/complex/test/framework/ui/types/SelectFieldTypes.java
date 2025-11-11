package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.select.SelectComponentType;

/**
 * Registry of select/dropdown component type identifiers for the test framework.
 * <p>
 * Each enum constant represents a specific select/dropdown implementation technology (Material Design,
 * Bootstrap, Vaadin) that can be used to tag select elements. ROA's {@code @ImplementationOfType}
 * annotation uses these type identifiers to automatically select the correct component
 * implementation class at runtime.
 * </p>
 * <p>
 * Available types:
 * <ul>
 *   <li>{@link #MD_SELECT_TYPE} — Material Design select/dropdown</li>
 *   <li>{@link #BOOTSTRAP_SELECT_TYPE} — Bootstrap-styled select/dropdown</li>
 *   <li>{@link #VA_SELECT_TYPE} — Vaadin combo boxes (used in Bakery Flow)</li>
 * </ul>
 * </p>
 * <p>
 * The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code SelectVaImpl}) to their type.
 * </p>
 */
public enum SelectFieldTypes implements SelectComponentType {

   MD_SELECT_TYPE,
   BOOTSTRAP_SELECT_TYPE,
   VA_SELECT_TYPE;

   public static final class Data {

      public static final String MD_SELECT = "MD_SELECT_TYPE";
      public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";
      public static final String VA_SELECT = "VA_SELECT_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }
}
