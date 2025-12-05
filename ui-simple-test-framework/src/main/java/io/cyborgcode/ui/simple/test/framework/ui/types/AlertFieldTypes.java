package io.cyborgcode.ui.simple.test.framework.ui.types;

import io.cyborgcode.roa.ui.components.alert.AlertComponentType;

/**
 * Registry of alert component type identifiers for the test framework.
 *
 * <p>Each enum constant represents a specific alert implementation technology (e.g., Bootstrap)
 * that can be used to tag alert elements. ROA {@code @ImplementationOfType} annotation uses these 
 * type identifiers to automatically select the correct component implementation class at runtime.
 *
 * <p>The nested {@link Data} class provides string constants used in {@code @ImplementationOfType}
 * annotations to link implementation classes (e.g., {@code AlertBootstrapImpl}) to their type.
 *
 * <p>Example:
 * <pre>{@code
 * // Implementation class for Bootstrap alerts
 * @ImplementationOfType(type = AlertFieldTypes.Data.BOOTSTRAP_ALERT)
 * public class AlertBootstrapImpl implements AlertComponent {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum AlertFieldTypes implements AlertComponentType {

   BOOTSTRAP_ALERT_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_ALERT = "BOOTSTRAP_ALERT_TYPE";

      private Data() {
      }

   }


   @Override
   public Enum getType() {
      return this;
   }
}
