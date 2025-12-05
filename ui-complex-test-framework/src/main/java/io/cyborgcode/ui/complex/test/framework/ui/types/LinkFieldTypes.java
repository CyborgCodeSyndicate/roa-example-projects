package io.cyborgcode.ui.complex.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.link.LinkComponentType;

/**
 * Registry of link/navigation component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific link implementation technology (Material Design,
 * Bootstrap, Vaadin) that can be used to tag link/tab elements. ROA {@code @ImplementationOfType}
 * annotation uses these type identifiers to automatically select the correct component
 * implementation class at runtime.
 *
 * <p>Available types:
 *
 * <ul>
 *   <li>{@link #MD_LINK_TYPE} — Material Design links
 *   <li>{@link #BOOTSTRAP_LINK_TYPE} — Bootstrap-styled links
 *   <li>{@link #VA_LINK_TYPE} — Vaadin tabs/links
 * </ul>
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code LinkVaImpl}) to their type.
 *
 * <p>Example:
 *
 * <pre>{@code
 * // Implementation class for Vaadin links
 * @ImplementationOfType(type = LinkFieldTypes.Data.VA_LINK)
 * public class LinkVaImpl implements LinkComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum LinkFieldTypes implements LinkComponentType {

   MD_LINK_TYPE,
   BOOTSTRAP_LINK_TYPE,
   VA_LINK_TYPE;

   public static final class Data {

      public static final String MD_LINK = "MD_LINK_TYPE";
      public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK_TYPE";
      public static final String VA_LINK = "VA_LINK_TYPE";

      private Data() {
      }

   }

   @Override
   public Enum getType() {
      return this;
   }
}
