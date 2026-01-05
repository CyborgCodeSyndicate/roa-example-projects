package io.cyborgcode.roa.usage.common.data.test_data;

import org.aeonbits.owner.ConfigCache;

public final class Data {

   private Data() {
   }

   public static DataProperties testData() {
      return ConfigCache.getOrCreate(DataProperties.class);
   }
}
