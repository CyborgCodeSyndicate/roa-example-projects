package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.list.ItemListComponentType;

/**
 * Registry of list component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific list implementation technology (e.g., Bootstrap)
 * that can be used to tag list elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code ItemListBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap item lists
 * @ImplementationOfType(type = ListFieldTypes.Data.BOOTSTRAP_LIST)
 * public class ItemListBootstrapImpl implements ItemListComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ListFieldTypes implements ItemListComponentType {

   BOOTSTRAP_LIST_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_LIST = "BOOTSTRAP_LIST_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
