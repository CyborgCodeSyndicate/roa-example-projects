package io.cyborgcode.ui.simple.test.framework.ui.components.radio;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.radio.Radio;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.playwright.util.strategy.StrategyGenerator;
import io.cyborgcode.ui.simple.test.framework.ui.types.RadioFieldTypes;
import java.util.List;
import java.util.Objects;

/**
 * Bootstrap-specific implementation of the {@link Radio} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap-styled radio groups
 * (inputs rendered as {@code <input type="radio">} with label text resolved from a parent container).
 * It is automatically selected by ROA component resolution mechanism when a radio field is tagged with
 * {@link RadioFieldTypes#BOOTSTRAP_RADIO_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Select a radio by label, locator, or container; or select via {@link Strategy}
 *       (RANDOM, FIRST, LAST)</li>
 *   <li>Check state: selected, enabled, visible</li>
 *   <li>Retrieve the selected radio label or enumerate all radio labels</li>
 *   <li>Resolve label text via a parent container of the {@code input[type='radio']}</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s class-based state conventions:
 * selection via {@code checked}, disabled via {@code disabled}, and visibility via the absence of {@code hidden}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(RadioFieldTypes.Data.BOOTSTRAP_RADIO)
public class RadioBootstrapImpl extends BaseComponent implements Radio {

   private static final PwBy RADIO_ELEMENT_SELECTOR = PwBy.css("input[type='radio']");
   private static final PwBy RADIO_ELEMENT_CONTENT_LOCATOR = PwBy.css("../.");
   public static final String CHECKED_CLASS_INDICATOR = "checked";
   public static final String DISABLED_CLASS_INDICATOR = "disabled";
   public static final String VISIBLE_CLASS_INDICATOR = "hidden";


   public RadioBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void select(final PwElement container, final String radioButtonText) {
      PwElement radioButton = findRadioButton(container, radioButtonText, null);
      selectElement(radioButton);
   }


   @Override
   public String select(final PwElement container, final Strategy strategy) {
      PwElement radioButton = findRadioButton(container, null, strategy);
      return selectElement(radioButton);
   }


   @Override
   public void select(final String radioButtonText) {
      PwElement radioButton = findRadioButton(null, radioButtonText, null);
      selectElement(radioButton);
   }


   @Override
   public void select(final PwBy radioButtonLocator) {
      PwElement radioButton = PwElement.of(driver.locator(radioButtonLocator.selector()));
      selectElement(radioButton);
   }


   @Override
   public boolean isEnabled(final PwElement container, final String radioButtonText) {
      PwElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isEnabled(final String radioButtonText) {
      PwElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isEnabled(final PwBy radioButtonLocator) {
      PwElement radioButton = PwElement.of(driver.locator(radioButtonLocator.selector()));
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isSelected(final PwElement container, final String radioButtonText) {
      PwElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementSelected(radioButton);
   }


   @Override
   public boolean isSelected(final String radioButtonText) {
      PwElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementSelected(radioButton);
   }


   @Override
   public boolean isSelected(final PwBy radioButtonLocator) {
      PwElement radioButton = PwElement.of(driver.locator(radioButtonLocator.selector()));
      return isElementSelected(radioButton);
   }


   @Override
   public boolean isVisible(final PwElement container, final String radioButtonText) {
      PwElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementVisible(radioButton);
   }


   @Override
   public boolean isVisible(final String radioButtonText) {
      PwElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementVisible(radioButton);
   }


   @Override
   public boolean isVisible(final PwBy radioButtonLocator) {
      PwElement radioButton = PwElement.of(driver.locator(radioButtonLocator.selector()));
      return isElementVisible(radioButton);
   }


   @Override
   public String getSelected(final PwElement container) {
      PwElement selectedRadioElement = container.locator(RADIO_ELEMENT_SELECTOR.selector()).allElements()
            .stream().filter(this::isElementSelected)
            .findFirst().orElseThrow(() -> new RuntimeException("There is not radio element in the container"));
      return getElementText(selectedRadioElement);
   }


   @Override
   public String getSelected(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getSelected(container);
   }


   @Override
   public List<String> getAll(final PwElement container) {
      return container.locator(RADIO_ELEMENT_SELECTOR.selector()).allElements()
            .stream().map(this::getElementText)
            .toList();
   }


   @Override
   public List<String> getAll(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getAll(container);
   }


   private PwElement findRadioButton(PwElement container, String value, Strategy strategy) {

      List<PwElement> radioButtons = Objects.nonNull(container)
            ? container.locator(RADIO_ELEMENT_SELECTOR.selector()).allElements()
            : PwElement.of(driver.locator(RADIO_ELEMENT_SELECTOR.selector())).allElements();

      PwElement targetedRadioButton = null;

      if (Objects.nonNull(value)) {
         targetedRadioButton = radioButtons.stream().filter(
                     radio -> getElementText(radio)
                           .equalsIgnoreCase(value.trim())).findFirst()
               .orElseThrow(() -> new RuntimeException("Element with text: " + value + " can't be found"));
      }
      if (Objects.nonNull(strategy)) {

         targetedRadioButton = switch (strategy) {
            case RANDOM -> StrategyGenerator.getRandomElementFromElements(radioButtons);
            case FIRST -> StrategyGenerator.getFirstElementFromElements(radioButtons);
            case LAST -> StrategyGenerator.getLastElementFromElements(radioButtons);
            case ALL -> throw new IllegalArgumentException("Only single radio button can be selected");
         };
      }

      return targetedRadioButton;
   }


   private boolean hasClass(PwElement element, String classToken) {
      String classes = element.getAttribute("class");
      return classes != null && classes.contains(classToken);
   }


   private boolean isElementEnabled(PwElement radioButtonElement) {
      return !hasClass(radioButtonElement, DISABLED_CLASS_INDICATOR);
   }


   private boolean isElementSelected(PwElement radioButtonElement) {
      return hasClass(radioButtonElement, CHECKED_CLASS_INDICATOR);
   }


   private boolean isElementVisible(PwElement radioButtonElement) {
      return !hasClass(radioButtonElement, VISIBLE_CLASS_INDICATOR);
   }


   private String getElementText(PwElement radioButtonElement) {
      return radioButtonElement.locator(RADIO_ELEMENT_CONTENT_LOCATOR.selector()).textContent().trim();
   }


   private String selectElement(PwElement radioButtonElement) {
      String elementText = null;
      if (isElementVisible(radioButtonElement) && isElementEnabled(radioButtonElement)
            && !isElementSelected(radioButtonElement)) {
         radioButtonElement.click();
         elementText = getElementText(radioButtonElement);
      }
      return elementText;
   }
}