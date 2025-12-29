package io.cyborgcode.roa.usage.api.authentication;

import io.cyborgcode.roa.api.authentication.BaseAuthenticationClient;
import io.cyborgcode.roa.api.service.RestService;
import io.restassured.http.Header;

/**
 * Implements the authentication strategy for the application.
 * <p>
 * This client retrieval logic returns the {@link Header} (e.g., Bearer Token) required
 * by {@link BaseAuthenticationClient} to sign API requests.
 * </p>
 */
public class ExampleAuthenticationClient extends BaseAuthenticationClient {

    @Override
    protected Header authenticateImpl(RestService restService, String username, String password) {

        // Implementation returning the required Authorization header
        return new Header("Authorization", "Bearer example-token");
    }
}
