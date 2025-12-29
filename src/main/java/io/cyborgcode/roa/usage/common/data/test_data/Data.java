package io.cyborgcode.roa.usage.common.data.test_data;

import org.aeonbits.owner.ConfigCache;

/**
 * Provides convenient access to test data configuration.
 *
 * <p>This utility wraps the OWNER library's {@link ConfigCache} to give tests a
 * single source of strongly-typed input values loaded from
 * {@code ${test.data.file}.properties} and system properties.</p>
 */
public final class Data {

    private Data() {
    }

    public static DataProperties testData() {
        return ConfigCache.getOrCreate(DataProperties.class);
    }
}
