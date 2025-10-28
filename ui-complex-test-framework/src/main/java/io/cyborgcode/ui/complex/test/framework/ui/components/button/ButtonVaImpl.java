package io.cyborgcode.ui.complex.test.framework.ui.components.button;

import io.cyborgcode.ui.complex.test.framework.ui.types.ButtonFieldTypes;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.button.Button;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;


@ImplementationOfType(ButtonFieldTypes.VA_BUTTON)
public class ButtonVaImpl extends BaseComponent implements Button {

   private static final By BUTTON_TAG_NAME_SELECTOR = By.tagName("vaadin-button");
   private static final String DISABLED_STATE_INDICATOR = "disabled";
   public static final String NOT_VISIBLE_STATE_INDICATOR = "hidden";


   public ButtonVaImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void click(SmartWebElement container, String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      button.click();
   }


   @Override
   public void click(SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      button.click();
   }


   @Override
   public void click(String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      button.click();
   }


   @Override
   public void click(By buttonLocator) {
      SmartWebElement button;
      button = driver.findSmartElement(buttonLocator);
      button.click();
   }


   @Override
   public boolean isEnabled(SmartWebElement container, String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(By buttonLocator) {
      SmartWebElement button = driver.findSmartElement(buttonLocator);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isVisible(SmartWebElement container, String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(By buttonLocator) {
      SmartWebElement button = driver.findSmartElement(buttonLocator);
      return isButtonVisible(button);
   }


   private SmartWebElement findButtonInContainer(SmartWebElement container, String buttonText) {
      return container.findSmartElements(BUTTON_TAG_NAME_SELECTOR).stream()
            .filter(element -> buttonText == null || element.getText().contains(buttonText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
   }


   private SmartWebElement findButtonByText(String buttonText) {
      return driver.findSmartElements(BUTTON_TAG_NAME_SELECTOR).stream()
            .filter(element -> element.getText().contains(buttonText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
   }


   private boolean isButtonEnabled(SmartWebElement button) {
      return button.getAttribute(DISABLED_STATE_INDICATOR) == null;
   }


   private boolean isButtonVisible(SmartWebElement button) {
      return button.getAttribute(NOT_VISIBLE_STATE_INDICATOR) == null;
   }

}