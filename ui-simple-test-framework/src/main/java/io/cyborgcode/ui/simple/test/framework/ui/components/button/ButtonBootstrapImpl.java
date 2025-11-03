package io.cyborgcode.ui.simple.test.framework.ui.components.button;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.button.Button;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.ButtonFieldTypes;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

@ImplementationOfType(ButtonFieldTypes.Data.BOOTSTRAP_INPUT)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

   private static final By BUTTON_TAG_NAME_SELECTOR = By.tagName("button");
   private static final String DISABLED_STATE_INDICATOR = "disabled";
   private static final String NOT_VISIBLE_STATE_INDICATOR = "hidden";


   public ButtonBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void click(final SmartWebElement container, final String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      button.click();
   }


   @Override
   public void click(final SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      button.click();
   }


   @Override
   public void click(final String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      button.click();
   }


   @Override
   public void click(final By buttonLocator) {
      SmartWebElement button = driver.findSmartElement(buttonLocator);
      button.click();
   }


   @Override
   public boolean isEnabled(final SmartWebElement container, final String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final By buttonLocator) {
      SmartWebElement button = driver.findSmartElement(buttonLocator);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isVisible(final SmartWebElement container, final String buttonText) {
      SmartWebElement button = findButtonInContainer(container, buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final SmartWebElement container) {
      SmartWebElement button = findButtonInContainer(container, null);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final String buttonText) {
      SmartWebElement button = findButtonByText(buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final By buttonLocator) {
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
      return !Objects.requireNonNull(button.getAttribute("class")).contains(DISABLED_STATE_INDICATOR);
   }


   private boolean isButtonVisible(SmartWebElement button) {
      return !Objects.requireNonNull(button.getAttribute("class")).contains(NOT_VISIBLE_STATE_INDICATOR);
   }

}