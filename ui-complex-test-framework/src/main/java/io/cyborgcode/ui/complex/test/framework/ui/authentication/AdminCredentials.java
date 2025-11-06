package io.cyborgcode.ui.complex.test.framework.ui.authentication;

import io.cyborgcode.roa.ui.authentication.LoginCredentials;
import io.cyborgcode.ui.complex.test.framework.data.test_data.Data;

public class AdminCredentials implements LoginCredentials {

   @Override
   public String username() {
      return Data.testData().username();
   }

   @Override
   public String password() {
      return Data.testData().password();
   }

}
