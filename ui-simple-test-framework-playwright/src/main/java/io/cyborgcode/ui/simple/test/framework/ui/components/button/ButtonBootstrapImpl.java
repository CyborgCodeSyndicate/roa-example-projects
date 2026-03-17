package io.cyborgcode.ui.simple.test.framework.ui.components.button;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.button.Button;
import io.cyborgcode.ui.simple.test.framework.ui.types.ButtonFieldTypes;
import java.util.Objects;

/**
 * Bootstrap-specific implementation of the {@link Button} component.
 *
 * <p>This class provides the concrete logic for interacting with standard HTML
 * {@code <button>} elements styled by Bootstrap. It is automatically
 * selected by ROA component resolution mechanism when a button field is tagged with
 * {@link ButtonFieldTypes#BOOTSTRAP_BUTTON_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Click buttons by text, locator, or within a container</li>
 *   <li>Check enabled/disabled state via {@code disabled} DOM attribute</li>
 *   <li>Check visibility via {@code hidden} DOM attribute</li>
 *   <li>Find buttons dynamically by text content</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s class-based state management to provide
 * reliable interactions with buttons in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(ButtonFieldTypes.Data.BOOTSTRAP_BUTTON)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

   private static final PwBy BUTTON_TAG_NAME_SELECTOR = PwBy.css("button");
   private static final String DISABLED_STATE_INDICATOR = "disabled";
   private static final String NOT_VISIBLE_STATE_INDICATOR = "hidden";


   public ButtonBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void click(final PwElement container, final String buttonText) {
      PwElement button = findButtonInContainer(container, buttonText);
      button.click();
   }


   @Override
   public void click(final PwElement container) {
      PwElement button = findButtonInContainer(container, null);
      button.click();
   }


   @Override
   public void click(final String buttonText) {
      PwElement button = findButtonByText(buttonText);
      button.click();
   }


   @Override
   public void click(final PwBy buttonLocator) {
      driver.locator(buttonLocator.selector()).click();
   }


   @Override
   public boolean isEnabled(final PwElement container, final String buttonText) {
      PwElement button = findButtonInContainer(container, buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final PwElement container) {
      PwElement button = findButtonInContainer(container, null);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final String buttonText) {
      PwElement button = findButtonByText(buttonText);
      return isButtonEnabled(button);
   }


   @Override
   public boolean isEnabled(final PwBy buttonLocator) {
      PwElement button = PwElement.of(driver.locator(buttonLocator.selector()));
      return isButtonEnabled(button);
   }


   @Override
   public boolean isVisible(final PwElement container, final String buttonText) {
      PwElement button = findButtonInContainer(container, buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final PwElement container) {
      PwElement button = findButtonInContainer(container, null);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final String buttonText) {
      PwElement button = findButtonByText(buttonText);
      return isButtonVisible(button);
   }


   @Override
   public boolean isVisible(final PwBy buttonLocator) {
      PwElement button = PwElement.of(driver.locator(buttonLocator.selector()));
      return isButtonVisible(button);
   }


   private PwElement findButtonInContainer(PwElement container, String buttonText) {

      return container.locator(BUTTON_TAG_NAME_SELECTOR.selector()).allElements()
            .stream().filter(locator -> locator.textContent().contains(buttonText))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("fdsfsd"));
   }


   private PwElement findButtonByText(String buttonText) {
      return driver.locator(BUTTON_TAG_NAME_SELECTOR.selector()).all()
            .stream()
            .map(PwElement::of)
            .filter(element -> element.innerText().contains(buttonText))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Button with text " + buttonText + " not found"));
   }


   private boolean isButtonEnabled(PwElement button) {
      return !Objects.requireNonNull(button.getAttribute("class")).contains(DISABLED_STATE_INDICATOR);
   }


   private boolean isButtonVisible(PwElement button) {
      return !Objects.requireNonNull(button.getAttribute("class")).contains(NOT_VISIBLE_STATE_INDICATOR);
   }

}