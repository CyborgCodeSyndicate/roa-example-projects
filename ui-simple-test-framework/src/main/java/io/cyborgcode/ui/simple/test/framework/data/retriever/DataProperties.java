package io.cyborgcode.ui.simple.test.framework.data.retriever;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface DataProperties extends Config {

   @Key("username")
   String username();

   @Key("password")
   String password();

}
