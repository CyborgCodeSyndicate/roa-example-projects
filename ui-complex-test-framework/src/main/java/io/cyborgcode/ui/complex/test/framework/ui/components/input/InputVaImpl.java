package io.cyborgcode.ui.complex.test.framework.ui.components.input;

import io.cyborgcode.ui.complex.test.framework.ui.types.InputFieldTypes;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.input.Input;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

import java.util.List;
import java.util.Objects;

/**
 * Vaadin-specific implementation of the {@link Input} component interface.
 *
 * <p>This class provides the concrete logic for interacting with Vaadin input fields ({@code
 * <vaadin-text-field>} elements). It is automatically selected by ROA component resolution
 * mechanism when an input element is tagged with {@link InputFieldTypes#VA_INPUT_TYPE} via the
 * {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 *
 * <ul>
 *   <li>Insert text into fields by label, locator, or within a container
 *   <li>Clear field values
 *   <li>Retrieve current field values via {@code value} DOM attribute
 *   <li>Check enabled/disabled state via {@code disabled} DOM attribute
 *   <li>Retrieve validation error messages from {@code error-message} part
 *   <li>Find fields dynamically by label text
 * </ul>
 *
 * <p>This implementation handles Vaadin's DOM structure and attribute-based state management,
 * ensuring reliable interaction with input fields in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(InputFieldTypes.Data.VA_INPUT)
public class InputVaImpl extends BaseComponent implements Input {

   private static final By INPUT_FIELD_CONTAINER = By.tagName("vaadin-text-field");
   private static final By INPUT_LOCATOR = By.tagName("input");
   private static final By INPUT_FIELD_ERROR_MESSAGE_LOCATOR = By.cssSelector("div[part='error-message']");
   private static final By INPUT_FIELD_LABEL_LOCATOR = By.cssSelector("div[part='label']");
   private static final String ELEMENT_VALUE_ATTRIBUTE = "value";
   private static final String FIELD_DISABLE_CLASS_INDICATOR = "disabled";


   public InputVaImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void insert(final SmartWebElement container, final String value) {
      SmartWebElement inputFieldContainer = findInputField(container, null);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final SmartWebElement container, final String inputFieldLabel, final String value) {
      SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final String inputFieldLabel, final String value) {
      SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final By inputFieldContainerLocator, final String value) {
      SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void clear(final SmartWebElement container) {
      SmartWebElement inputFieldContainer = findInputField(container, null);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final SmartWebElement container, final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final By inputFieldContainerLocator) {
      SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
      clearInputField(inputFieldContainer);
   }


   @Override
   public String getValue(final SmartWebElement container) {
      SmartWebElement inputFieldContainer = findInputField(container, null);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final SmartWebElement container, final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final By inputFieldContainerLocator) {
      SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final SmartWebElement container) {
      SmartWebElement inputFieldContainer = findInputField(container, null);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final SmartWebElement container, final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final By inputFieldContainerLocator) {
      SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final SmartWebElement container) {
      SmartWebElement inputFieldContainer = findInputField(container, null);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final SmartWebElement container, final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final String inputFieldLabel) {
      SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final By inputFieldContainerLocator) {
      SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   private SmartWebElement findInputField(SmartWebElement container, String label) {
      List<SmartWebElement> fieldElements = Objects.nonNull(container)
            ? container.findSmartElements(INPUT_FIELD_CONTAINER)
            : driver.findSmartElements(INPUT_FIELD_CONTAINER);

      if (fieldElements.isEmpty()) {
         throw new NotFoundException("There is no input element");
      }

      return Objects.nonNull(label)
            ? fieldElements.stream()
            .filter(fieldElement -> label.trim().equalsIgnoreCase(getInputFieldLabel(fieldElement)))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("There is no input element with label: " + label))
            : fieldElements.get(0);
   }


   private void insertIntoInputField(SmartWebElement inputFieldContainer, String value) {
      if (isInputFieldEnabled(inputFieldContainer)) {
         SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
         inputElement.clearAndSendKeys(value);
      }
   }


   private void clearInputField(SmartWebElement inputFieldContainer) {
      if (isInputFieldEnabled(inputFieldContainer)) {
         SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
         inputElement.clear();
      }
   }


   private String getInputFieldValue(SmartWebElement inputFieldContainer) {
      SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
      return inputElement.getDomAttribute(ELEMENT_VALUE_ATTRIBUTE);
   }


   private String getInputFieldLabel(SmartWebElement fieldElement) {
      SmartWebElement labelElement = fieldElement.findSmartElement(INPUT_FIELD_LABEL_LOCATOR);
      return labelElement.getText().trim();
   }


   private String getInputFieldErrorMessage(SmartWebElement fieldElement) {
      SmartWebElement errorMessageElement = fieldElement.findSmartElement(INPUT_FIELD_ERROR_MESSAGE_LOCATOR);
      return errorMessageElement.getText().trim();
   }


   private boolean isInputFieldEnabled(SmartWebElement fieldElement) {
      String classAttribute = fieldElement.getDomAttribute(FIELD_DISABLE_CLASS_INDICATOR);
      return classAttribute == null;
   }

}
