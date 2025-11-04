package io.cyborgcode.ui.complex.test.framework.api.authentication;

import io.cyborgcode.roa.api.authentication.Credentials;
import io.cyborgcode.ui.complex.test.framework.data.test_data.DataProperties;
import org.aeonbits.owner.ConfigCache;

public class AdminAuth implements Credentials {

    private static final DataProperties DATA_PROPERTIES = ConfigCache.getOrCreate(DataProperties.class);

    @Override
    public String username() {
        return DATA_PROPERTIES.username();
    }

    @Override
    public String password() {
        return DATA_PROPERTIES.password();
    }

}
