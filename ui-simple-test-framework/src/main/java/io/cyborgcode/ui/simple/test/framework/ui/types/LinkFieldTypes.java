package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.link.LinkComponentType;

/**
 * Registry of link component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific link implementation technology (e.g., Bootstrap)
 * that can be used to tag link elements. ROA {@code @ImplementationOfType} annotation uses these
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code LinkBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap links
 * @ImplementationOfType(type = LinkFieldTypes.Data.BOOTSTRAP_LINK)
 * public class LinkBootstrapImpl implements LinkComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum LinkFieldTypes implements LinkComponentType {

   BOOTSTRAP_LINK_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
