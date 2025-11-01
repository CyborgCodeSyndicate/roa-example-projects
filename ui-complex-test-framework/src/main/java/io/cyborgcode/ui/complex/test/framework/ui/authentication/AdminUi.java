package io.cyborgcode.ui.complex.test.framework.ui.authentication;

import io.cyborgcode.ui.complex.test.framework.data.retriever.DataProperties;
import io.cyborgcode.roa.ui.authentication.LoginCredentials;
import org.aeonbits.owner.ConfigCache;

public class AdminUi implements LoginCredentials {

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
