package io.cyborgcode.api.test.framework.api.authentication;

import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.roa.api.authentication.BaseAuthenticationClient;
import io.cyborgcode.roa.api.service.RestService;
import io.restassured.http.Header;

import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_KEY;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_VALUE;

public class AppAuth extends BaseAuthenticationClient {

   @Override
   protected Header authenticateImpl(final RestService restService, final String username, final String password) {
      String token = restService
            .request(POST_LOGIN_USER, new LoginDto(username, password))
            .getBody()
            .jsonPath()
            .getString(TOKEN.getJsonPath());
      return new Header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token);
   }

}
