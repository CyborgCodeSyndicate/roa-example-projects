package io.cyborgcode.roa.usage.common.data.test_data;

import io.cyborgcode.roa.framework.data.StaticDataProvider;

import java.util.HashMap;
import java.util.Map;

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
