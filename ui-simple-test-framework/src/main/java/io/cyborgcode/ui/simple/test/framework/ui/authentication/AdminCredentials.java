package io.cyborgcode.ui.simple.test.framework.ui.authentication;

import io.cyborgcode.roa.ui.authentication.LoginCredentials;
import io.cyborgcode.ui.simple.test.framework.data.test_data.Data;

/**
 * Provides default admin credentials for the UI Flow application.
 *
 * <p> This class implements {@link LoginCredentials} to supply credentials
 * for authentication operations. It integrates with ROA {@code @AuthenticateViaUi}
 * annotation to perform automatic login before tests execute.
 *
 * <p> Credentials are sourced from {@link Data#testData()} configuration, allowing
 * environment-specific overrides without code changes. This pattern centralizes
 * credential management and supports credential rotation or environment-based variations.
 *
 * <p> Usage example:
 * <pre>{@code
 * @Test
 * @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
 * void myTest(Quest quest) {
 *     // Test starts with user already logged in
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
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
