package io.cyborgcode.api.test.framework.rest.authentication;

import io.cyborgcode.api.test.framework.data.test.TestData;
import io.cyborgcode.roa.api.authentication.Credentials;
import org.aeonbits.owner.ConfigCache;

public class AdminAuth implements Credentials {

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
