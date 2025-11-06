package io.cyborgcode.ui.complex.test.framework.data.test_data;

import io.cyborgcode.roa.framework.data.StaticDataProvider;

import java.util.HashMap;
import java.util.Map;

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
