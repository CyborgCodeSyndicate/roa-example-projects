package io.cyborgcode.roa.usage.common.data.test_data;

import io.cyborgcode.roa.framework.data.StaticDataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides static test data for the example test suite.
 *
 * <p>This class implements {@link StaticDataProvider} to supply compile-time constant values that
 * can be referenced in tests via the {@code @StaticTestData} annotation. Values are sourced from
 * {@link DataProperties} configuration files, allowing environment-specific overrides while
 * maintaining type-safe access through string keys.
 *
 * <p>Tests retrieve values via {@code quest.getStorage().get(staticTestData(key), Class)} after
 * annotating the test method with {@code @StaticTestData(StaticData.class)}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class StaticData implements StaticDataProvider {

    public static final String NAME = "name";
    public static final String JOB = "job";

    @Override
    public Map<String, Object> staticTestData() {
        Map<String, Object> data = new HashMap<>();
        data.put(NAME, Data.testData().name());
        data.put(JOB, Data.testData().job());
        return data;
    }
}
