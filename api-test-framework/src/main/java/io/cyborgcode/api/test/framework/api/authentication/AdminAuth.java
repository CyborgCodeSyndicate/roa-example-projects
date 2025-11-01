package io.cyborgcode.api.test.framework.api.authentication;

import io.cyborgcode.api.test.framework.data.retriever.DataProperties;
import io.cyborgcode.roa.api.authentication.Credentials;
import org.aeonbits.owner.ConfigCache;

public class AdminAuth implements Credentials {

   private static final DataProperties DATA_PROPERTIES = ConfigCache.getOrCreate(DataProperties.class);

   @Override
   public String username() {
      return DATA_PROPERTIES.username();
   }

   @Override
   public String password() {
      return DATA_PROPERTIES.password();
   }

}
