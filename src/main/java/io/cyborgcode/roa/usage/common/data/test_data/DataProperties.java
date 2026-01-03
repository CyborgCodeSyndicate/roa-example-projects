package io.cyborgcode.roa.usage.common.data.test_data;

import io.cyborgcode.utilities.config.PropertyConfig;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
      "system:properties",
      "classpath:${test.data.file}.properties"
})
public interface DataProperties extends PropertyConfig {

   @Key("name")
   String name();

   @Key("job")
   String job();
}
