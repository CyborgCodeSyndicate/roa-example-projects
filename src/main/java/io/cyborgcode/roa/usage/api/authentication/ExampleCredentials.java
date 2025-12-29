package io.cyborgcode.roa.usage.api.authentication;

import io.cyborgcode.roa.api.authentication.Credentials;
import io.cyborgcode.roa.usage.common.data.test_data.DataProperties;
import org.aeonbits.owner.ConfigCache;

/**
 * Example credentials provider.
 *
 * <p>Replace the accessors with fields relevant to your application
 * (e.g., admin user, test user, API key, etc.).</p>
 */
public class ExampleCredentials implements Credentials {

    private static final DataProperties DATA_PROPERTIES =
            ConfigCache.getOrCreate(DataProperties.class);

    @Override
    public String username() {
        // TODO: Replace with your own configuration keys
        return DATA_PROPERTIES.username();
    }

    @Override
    public String password() {
        // TODO: Replace with your own configuration keys
        return DATA_PROPERTIES.password();
    }
}
