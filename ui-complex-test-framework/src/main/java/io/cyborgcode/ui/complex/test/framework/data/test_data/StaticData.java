package io.cyborgcode.ui.complex.test.framework.data.test_data;

import io.cyborgcode.roa.framework.data.StaticDataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides static test data for the Bakery UI test suite.
 * <p>
 * This class implements {@link StaticDataProvider} to supply compile-time constant values
 * that can be referenced in tests via the {@code @StaticTestData} annotation. Values are
 * sourced from {@link DataProperties} configuration files, allowing environment-specific
 * overrides while maintaining type-safe access through string keys.
 * </p>
 * <p>
 * Tests retrieve values via {@code quest.getStorage().get(staticTestData(key), Class)}
 * after annotating the test method with {@code @StaticTestData(StaticData.class)}.
 * </p>
 */
public class StaticData implements StaticDataProvider {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Override
    public Map<String, Object> staticTestData() {
        Map<String, Object> data = new HashMap<>();
        data.put(USERNAME, Data.testData().username());
        data.put(PASSWORD, Data.testData().password());
        return data;
    }
}
