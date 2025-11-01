package io.cyborgcode.ui.complex.test.framework.ui.authentication;

import io.cyborgcode.ui.complex.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.ui.authentication.BaseLoginClient;
import io.cyborgcode.roa.ui.service.fluent.UiServiceFluent;
import org.openqa.selenium.By;

public class AppUiLogging extends BaseLoginClient {


   @Override
   protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
      uiService
            .getNavigation().navigate("https://bakery-flow.demo.vaadin.com/")
            .getInputField().insert(InputFields.USERNAME_FIELD, username)
            .getInputField().insert(InputFields.PASSWORD_FIELD, password)
            .getButtonField().click(ButtonFields.SIGN_IN_BUTTON);
   }

   @Override
   protected By successfulLoginElementLocator() {
      return By.tagName("vaadin-app-layout");
   }

}
