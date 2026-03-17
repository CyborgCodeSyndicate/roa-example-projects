package io.cyborgcode.ui.simple.test.framework.ui.components.input;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.input.Input;
import io.cyborgcode.ui.simple.test.framework.ui.types.InputFieldTypes;
import java.util.List;
import java.util.Objects;

/**
 * Bootstrap-specific implementation of the {@link Input} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap-styled input fields
 * within form containers. It is automatically selected by ROA component resolution
 * mechanism when an input field is tagged with
 * {@link InputFieldTypes#BOOTSTRAP_INPUT_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Insert text into fields by label, locator, or within a container</li>
 *   <li>Clear field values</li>
 *   <li>Retrieve current field values via {@code value} DOM attribute</li>
 *   <li>Check enabled/disabled state via {@code disabled} DOM attribute</li>
 *   <li>Retrieve validation error messages from {@code error-message} part</li>
 *   <li>Find fields dynamically by label text</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s form structure and class conventions to provide
 * reliable interactions and validations for inputs in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(InputFieldTypes.Data.BOOTSTRAP_INPUT)
public class InputBootstrapImpl extends BaseComponent implements Input {

   private static final PwBy INPUT_FIELD_CONTAINER = PwBy.css("form");
   private static final PwBy INPUT_FIELD_ERROR_MESSAGE_LOCATOR = PwBy.css(".alert-error");
   private static final PwBy INPUT_FIELD_LABEL_LOCATOR = PwBy.css("../label");
   public static final String ELEMENT_VALUE_ATTRIBUTE = "value";
   public static final String FIELD_DISABLE_CLASS_INDICATOR = "disabled";


   public InputBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void insert(final PwElement container, final String value) {
      PwElement inputFieldContainer = findInputField(container, null);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final PwElement container, final String inputFieldLabel, final String value) {
      PwElement inputFieldContainer = findInputField(container, inputFieldLabel);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final String inputFieldLabel, final String value) {
      PwElement inputFieldContainer = findInputField(null, inputFieldLabel);
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void insert(final PwBy inputFieldContainerLocator, final String value) {
      PwElement inputFieldContainer = PwElement.of(driver.locator(inputFieldContainerLocator.selector()));
      insertIntoInputField(inputFieldContainer, value);
   }


   @Override
   public void clear(final PwElement container) {
      PwElement inputFieldContainer = findInputField(container, null);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final PwElement container, final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(container, inputFieldLabel);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(null, inputFieldLabel);
      clearInputField(inputFieldContainer);
   }


   @Override
   public void clear(final PwBy inputFieldContainerLocator) {
      PwElement inputFieldContainer = PwElement.of(driver.locator(inputFieldContainerLocator.selector()));
      clearInputField(inputFieldContainer);
   }


   @Override
   public String getValue(final PwElement container) {
      PwElement inputFieldContainer = findInputField(container, null);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final PwElement container, final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public String getValue(final PwBy inputFieldContainerLocator) {
      PwElement inputFieldContainer = PwElement.of(driver.locator(inputFieldContainerLocator.selector()));
      return getInputFieldValue(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final PwElement container) {
      PwElement inputFieldContainer = findInputField(container, null);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final PwElement container, final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public boolean isEnabled(final PwBy inputFieldContainerLocator) {
      PwElement inputFieldContainer = PwElement.of(driver.locator(inputFieldContainerLocator.selector()));
      return isInputFieldEnabled(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final PwElement container) {
      PwElement inputFieldContainer = findInputField(container, null);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final PwElement container, final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(container, inputFieldLabel);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final String inputFieldLabel) {
      PwElement inputFieldContainer = findInputField(null, inputFieldLabel);
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   @Override
   public String getErrorMessage(final PwBy inputFieldContainerLocator) {
      PwElement inputFieldContainer = PwElement.of(driver.locator(inputFieldContainerLocator.selector()));
      return getInputFieldErrorMessage(inputFieldContainer);
   }


   private PwElement findInputField(PwElement container, String label) {
      List<PwElement> fieldElements = Objects.nonNull(container)
            ? container.locator(INPUT_FIELD_CONTAINER.selector()).allElements()
            : PwElement.of(driver.locator(INPUT_FIELD_CONTAINER.selector())).allElements();

      if (fieldElements.isEmpty()) {
         throw new RuntimeException("There is no input element");
      }

      return Objects.nonNull(label)
            ? fieldElements.stream()
            .filter(fieldElement -> label.trim().equalsIgnoreCase(getInputFieldLabel(fieldElement)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("There is no input element with label: " + label))
            : fieldElements.get(0);
   }


   private void insertIntoInputField(PwElement inputField, String value) {
      if (isInputFieldEnabled(inputField)) {
         inputField.clear();
         inputField.fill(value);
      }
   }


   private void clearInputField(PwElement inputField) {
      if (isInputFieldEnabled(inputField)) {
         inputField.clear();
      }
   }


   private String getInputFieldValue(PwElement inputField) {
      return inputField.getAttribute(ELEMENT_VALUE_ATTRIBUTE);
   }


   private String getInputFieldLabel(PwElement fieldElement) {
      PwElement labelElement = fieldElement.locator(INPUT_FIELD_LABEL_LOCATOR.selector());
      return labelElement.textContent().trim();
   }


   private String getInputFieldErrorMessage(PwElement fieldElement) {
      PwElement errorMessageElement = fieldElement.locator(INPUT_FIELD_ERROR_MESSAGE_LOCATOR.selector());
      return errorMessageElement.textContent().trim();
   }


   private boolean isInputFieldEnabled(PwElement fieldElement) {
      String classAttribute = fieldElement.getAttribute(FIELD_DISABLE_CLASS_INDICATOR);
      return classAttribute == null;
   }
}
