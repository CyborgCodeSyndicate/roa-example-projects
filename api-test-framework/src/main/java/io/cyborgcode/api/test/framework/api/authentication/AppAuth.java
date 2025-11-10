package io.cyborgcode.api.test.framework.api.authentication;

import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.roa.api.authentication.BaseAuthenticationClient;
import io.cyborgcode.roa.api.service.RestService;
import io.restassured.http.Header;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_KEY;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_VALUE;

/**
 * Application authentication client for ROA-based tests.
 * <p>
 * Serves as a concrete {@link BaseAuthenticationClient} implementation that knows how to
 * obtain an authorization header for the application under test, based on provided
 * credentials. This allows tests to:
 * </p>
 * <ul>
 *   <li>Centralize authentication logic in one place.</li>
 *   <li>Reuse authenticated headers across requests via the underlying caching mechanism.</li>
 *   <li>Integrate with {@code @AuthenticateViaApi(type = AppAuth.class)} for declarative
 *       authentication without repeating login flows in each test.</li>
 * </ul>
 * Intended as a reusable template for plugging in any real application authentication strategy.
 */
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
