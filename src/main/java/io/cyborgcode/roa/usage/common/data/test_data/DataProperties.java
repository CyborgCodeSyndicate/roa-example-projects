package io.cyborgcode.roa.usage.common.data.test_data;

import io.cyborgcode.utilities.config.PropertyConfig;
import org.aeonbits.owner.Config;

/**
 * Strongly-typed configuration interface for test data.
 *
 * <p>Values are loaded from:
 * <ul>
 *   <li>System properties</li>
 *   <li>{@code ${test.data.file}.properties} on the classpath</li>
 * </ul></p>
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:${test.data.file}.properties"
})
public interface DataProperties extends PropertyConfig {

    @Key("username")
    String username();

    @Key("password")
    String password();

    @Key("name")
    String name();

    @Key("job")
    String job();
}
