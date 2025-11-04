package io.cyborgcode.ui.complex.test.framework.data.test_data;

import org.aeonbits.owner.ConfigCache;

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
