package io.cyborgcode.api.test.framework.data.test;

import io.cyborgcode.utilities.config.ConfigSource;
import io.cyborgcode.utilities.config.PropertyConfig;
import org.aeonbits.owner.Config;

@ConfigSource("test-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface TestData extends PropertyConfig {

   @Key("username")
   String username();

   @Key("password")
   String password();

}
