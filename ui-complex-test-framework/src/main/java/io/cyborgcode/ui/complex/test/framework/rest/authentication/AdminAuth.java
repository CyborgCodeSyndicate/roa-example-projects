package io.cyborgcode.ui.complex.test.framework.rest.authentication;

import io.cyborgcode.roa.api.authentication.Credentials;

public class AdminAuth implements Credentials {

   @Override
   public String username() {
      return "portal";
   }


   @Override
   public String password() {
      return "";
   }

}
