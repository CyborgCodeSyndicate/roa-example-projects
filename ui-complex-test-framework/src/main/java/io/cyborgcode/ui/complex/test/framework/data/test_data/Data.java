package io.cyborgcode.ui.complex.test.framework.data.test_data;

import org.aeonbits.owner.ConfigCache;

/**
 * Convenience accessor for test data configuration.
 * <p>
 * Provides a static factory method to retrieve the singleton {@link DataProperties}
 * instance, which is backed by the OWNER library's {@link ConfigCache}. This ensures
 * that configuration is loaded once and reused across all test executions.
 * </p>
 * <p>
 * Usage:
 * <pre>{@code
 * String username = Data.testData().username();
 * String password = Data.testData().password();
 * }</pre>
 * </p>
 * <p>
 * This pattern centralizes access to externalized test data, making it easy to
 * reference configuration values throughout the test suite without repeatedly
 * instantiating the config interface.
 * </p>
 */
public final class Data {

    private Data() {
    }

    public static DataProperties testData() {
      return getTestDataConfig();
   }

   private static DataProperties getTestDataConfig() {
      return ConfigCache.getOrCreate(DataProperties.class);
   }

}
