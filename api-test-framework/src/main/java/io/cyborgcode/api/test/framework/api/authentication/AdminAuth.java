package io.cyborgcode.api.test.framework.api.authentication;

import io.cyborgcode.api.test.framework.data.test_data.Data;
import io.cyborgcode.api.test.framework.data.test_data.DataProperties;
import io.cyborgcode.roa.api.authentication.Credentials;

/**
 * AdminAuth
 * <p>
 * Implementation of {@link Credentials} used by the ROA framework to authenticate
 * against Reqres (or a similar target) using admin-level test credentials.
 * <p>
 * The username and password are resolved from {@link DataProperties}, which is backed by
 * the Owner configuration and externalized test properties. This allows:
 * <ul>
 *   <li>Centralized management of authentication data for tests</li>
 *   <li>Clean integration with {@code @AuthenticateViaApi(credentials = AdminAuth.class, ...)}</li>
 *   <li>Reusability across multiple test classes without hardcoding secrets</li>
 * </ul>
 */
public class AdminAuth implements Credentials {

   @Override
   public String username() {
      return Data.testData().username();
   }

   @Override
   public String password() {
      return Data.testData().password();
   }

}
