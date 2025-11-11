package io.cyborgcode.ui.simple.test.framework.ui.authentication;

import io.cyborgcode.roa.ui.authentication.LoginCredentials;
import io.cyborgcode.ui.simple.test.framework.data.retriever.DataProperties;
import org.aeonbits.owner.ConfigCache;

public class AdminCredentials implements LoginCredentials {

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
