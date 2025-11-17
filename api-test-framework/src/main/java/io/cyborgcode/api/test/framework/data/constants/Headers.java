package io.cyborgcode.api.test.framework.data.constants;

import lombok.experimental.UtilityClass;

/**
 * Common HTTP header keys and example values used throughout the tests.
 * <p>
 * Centralizes header-related configuration so that authentication,
 * custom headers, and API keys remain consistent and easy to change.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UtilityClass
public class Headers {

    public static final String EXAMPLE_HEADER = "ExampleHeader";
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_VALUE = "Bearer ";
    public static final String API_KEY_HEADER = "x-api-key";
    public static final String API_KEY_VALUE = "reqres-free-v1";

}