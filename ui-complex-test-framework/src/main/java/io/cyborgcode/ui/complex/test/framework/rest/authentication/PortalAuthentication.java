package io.cyborgcode.ui.complex.test.framework.rest.authentication;

import io.cyborgcode.roa.api.authentication.BaseAuthenticationClient;
import io.cyborgcode.roa.api.service.RestService;
import io.restassured.http.Header;

public class PortalAuthentication extends BaseAuthenticationClient {

   @Override
   protected Header authenticateImpl(final RestService restService, final String username, final String password) {

      return new Header("header", "portal");
   }

}
