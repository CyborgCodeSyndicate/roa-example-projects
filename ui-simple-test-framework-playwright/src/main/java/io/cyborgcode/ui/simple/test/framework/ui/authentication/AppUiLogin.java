package io.cyborgcode.ui.simple.test.framework.ui.authentication;

import io.cyborgcode.roa.ui.playwright.authentication.BaseLoginClient;
import io.cyborgcode.roa.ui.playwright.service.fluent.UiServiceFluent;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;

import static io.cyborgcode.roa.ui.config.UiConfigHolderCore.getUiConfig;
import static io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields.SETTINGS_LINK;

/**
 * Application-specific login implementation for the UI Flow application.
 *
 * <p> This class extends {@link BaseLoginClient} to provide the concrete login workflow
 * for the application. It integrates with ROA {@code @AuthenticateViaUi}
 * annotation to perform automatic authentication before test execution.
 *
 * <p> Usage example:
 * <pre>{@code
 * @Test
 * @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
 * void myTest(Quest quest) {
 *     // Test begins with user authenticated
 * }
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class AppUiLogin extends BaseLoginClient {

   @Override
   protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
      uiService.getUiSession().getPage().navigate(getUiConfig().baseUrl());
      uiService
            .getButtonField().click(ButtonFields.SIGN_IN_BUTTON)
            .getInputField().insert(InputFields.USERNAME_FIELD, username)
            .getInputField().insert(InputFields.PASSWORD_FIELD, password)
            .getButtonField().click(ButtonFields.SIGN_IN_FORM_BUTTON);
      uiService.getUiSession().getPage().goBack();
   }

   @Override
   protected String successfulLoginSelector() {
      return SETTINGS_LINK.locator().selector();
   }
}
