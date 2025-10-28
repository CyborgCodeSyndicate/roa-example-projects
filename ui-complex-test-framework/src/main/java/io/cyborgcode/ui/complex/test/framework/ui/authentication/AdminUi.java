package io.cyborgcode.ui.complex.test.framework.ui.authentication;

import io.cyborgcode.ui.complex.test.framework.data.test.TestData;
import io.cyborgcode.roa.ui.authentication.LoginCredentials;
import org.aeonbits.owner.ConfigCache;

public class AdminUi implements LoginCredentials {

   private static final TestData testData = ConfigCache.getOrCreate(TestData.class);


   @Override
   public String username() {
      return testData.username();
   }


   @Override
   public String password() {
      return testData.password();
   }

}
