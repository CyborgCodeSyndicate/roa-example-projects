package io.cyborgcode.api.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import java.util.function.Consumer;

/**
 * Defines reusable cleanup (data ripping) operations for the tutorial test suite.
 * <p>
 * This enum integrates with ROA's {@code @Ripper} mechanism via {@link DataRipper}:
 * each constant maps to a function that is executed after the test completes,
 * allowing you to centralize and reuse teardown logic.
 * </p>
 * <ul>
 *   <li>{@link #DELETE_ADMIN_USER} — removes the admin user created during tests.</li>
 * </ul>
 * <p>
 * The nested {@link Data} class provides string constants for annotation-based references.
 * </p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DataCleaner implements DataRipper<DataCleaner> {

   DELETE_ADMIN_USER(DataCleanerFunctions::deleteAdminUser);

   public static final class Data {

      private Data() {
      }

      public static final String DELETE_ADMIN_USER = "DELETE_ADMIN_USER";

   }

   private final Consumer<SuperQuest> cleanUpFunction;

   DataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
      this.cleanUpFunction = cleanUpFunction;
   }

   @Override
   public Consumer<SuperQuest> eliminate() {
      return cleanUpFunction;
   }

   @Override
   public DataCleaner enumImpl() {
      return this;
   }

}
