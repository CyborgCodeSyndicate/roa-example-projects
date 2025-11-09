package io.cyborgcode.api.test.framework.data.retriever;

import io.cyborgcode.utilities.config.ConfigSource;
import io.cyborgcode.utilities.config.PropertyConfig;
import org.aeonbits.owner.Config;

/**
 * TestData
 * <p>
 * Central configuration interface for test data used by the example suite.
 * <p>
 * Backed by the Owner library and {@link PropertyConfig}, it:
 * <ul>
 *   <li>Loads values from system properties and a configurable properties file
 *       (via {@code test.data.file}.properties on the classpath)</li>
 *   <li>Provides strongly-typed accessors for commonly used values
 *       such as {@code username} and {@code password}</li>
 *   <li>Serves as the single source of truth for credentials and other shared test inputs</li>
 * </ul>
 * This allows the tests to read configuration in a consistent,
 * framework-friendly way without hardcoding values in the test code.
 */
@ConfigSource("test-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface TestData extends PropertyConfig {

   @Key("username")
   String username();

   @Key("password")
   String password();

}
