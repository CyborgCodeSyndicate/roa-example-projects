package io.cyborgcode.ui.complex.test.framework.ui.authentication;

import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.ui.authentication.BaseLoginClient;
import io.cyborgcode.roa.ui.service.fluent.UiServiceFluent;
import org.openqa.selenium.By;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;

/**
 * Application-specific login implementation for the Bakery Flow UI.
 * <p>
 * This class extends {@link BaseLoginClient} to provide the concrete login workflow
 * for the Bakery application. It integrates with ROA's {@code @AuthenticateViaUi}
 * annotation to perform automatic authentication before test execution.
 * </p>
 * <p>
 * Usage example:
 * <pre>{@code
 * @Test
 * @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
 * void myTest(Quest quest) {
 *     // Test begins with user authenticated
 * }
 * }</pre>
 * </p>
 */
public class AppUiLogin extends BaseLoginClient {

   @Override
   protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
      uiService
            .getNavigation().navigate(getUiConfig().baseUrl())
            .getInputField().insert(InputFields.USERNAME_FIELD, username)
            .getInputField().insert(InputFields.PASSWORD_FIELD, password)
            .getButtonField().click(ButtonFields.SIGN_IN_BUTTON);
   }

   @Override
   protected By successfulLoginElementLocator() {
      return By.tagName("vaadin-app-layout");
   }

}
