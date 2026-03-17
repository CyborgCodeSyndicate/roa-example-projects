package io.cyborgcode.ui.simple.test.framework.ui.components.select;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.select.Select;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.playwright.util.strategy.StrategyGenerator;
import io.cyborgcode.ui.simple.test.framework.ui.types.SelectFieldTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * Bootstrap-specific implementation of the {@link Select} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap-styled select elements
 * (dropdowns composed of {@code <option>} items). It is automatically selected by ROA’s
 * component resolution mechanism when a select field is tagged with
 * {@link SelectFieldTypes#BOOTSTRAP_SELECT_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Select options by visible text (single or multiple) within a container or by locator</li>
 *   <li>Strategy-driven selection (RANDOM, FIRST, LAST, ALL) via {@link Strategy}</li>
 *   <li>Inspect options: {@code getAvailableOptions(...)} and {@code getSelectedOptions(...)}</li>
 *   <li>Check option visibility and enabled state</li>
 *   <li>Honor Bootstrap semantics: hidden via {@code hidden}, selected via {@code selected}, disabled via {@code disabled}</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap dropdown markup and attribute conventions to ensure
 * reliable interactions and assertions for select components in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(SelectFieldTypes.Data.BOOTSTRAP_SELECT)
public class SelectBootstrapImpl extends BaseComponent implements Select {

   public static final PwBy OPTION_LOCATOR = PwBy.css("option");
   public static final String DISABLED_CLASS_INDICATOR = "disabled";
   public static final String SELECTED_ATTRIBUTE_INDICATOR = "selected";
   public static final String HIDDEN_ATTRIBUTE_INDICATOR = "hidden";


   public SelectBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void selectOptions(final PwElement container, final String... values) {
      openDdl(container);
      List<PwElement> options = getAllOptionsElements(container);
      for (String value : values) {
         PwElement option = findOptionByText(options, value);
         option.click();
      }
      closeDdl(container);
   }


   @Override
   public void selectOptions(final PwBy containerLocator, final String... values) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      selectOptions(container, values);
   }


   @Override
   public List<String> selectOptions(final PwElement container, final Strategy strategy) {
      return selectOptionsWithStrategy(container, strategy);
   }


   @Override
   public List<String> selectOptions(final PwBy containerLocator, Strategy strategy) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return selectOptions(container, strategy);
   }


   @Override
   public List<String> getAvailableOptions(final PwElement container) {
      openDdl(container);
      List<PwElement> options = getAllOptionsElements(container);
      List<String> availableOptions = options.stream()
            .map(option -> option.textContent().trim())
            .toList();
      closeDdl(container);
      return availableOptions;
   }


   @Override
   public List<String> getAvailableOptions(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getAvailableOptions(container);
   }


   @Override
   public List<String> getSelectedOptions(final PwElement container) {
      openDdl(container);
      List<String> checkedOptions = getAllOptionsElements(container).stream()
            .filter(this::checkIfOptionIsSelected)
            .map(PwElement::textContent)
            .toList();
      closeDdl(container);
      return checkedOptions;
   }


   @Override
   public List<String> getSelectedOptions(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getSelectedOptions(container);
   }


   @Override
   public boolean isOptionVisible(final PwElement container, final String value) {
      openDdl(container);
      List<PwElement> options = getAllOptionsElements(container);
      try {
         findOptionByText(options, value);
         return true;
      } catch (RuntimeException e) {
         return false;
      }
   }


   @Override
   public boolean isOptionVisible(final PwBy containerLocator, final String value) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return isOptionVisible(container, value);
   }


   @Override
   public boolean isOptionEnabled(final PwElement container, final String value) {
      openDdl(container);
      List<PwElement> options = getAllOptionsElements(container);
      PwElement option = findOptionByText(options, value);
      return isOptionEnabled(option);
   }


   @Override
   public boolean isOptionEnabled(final PwBy containerLocator, final String value) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return isOptionEnabled(container, value);
   }


   protected boolean isOptionEnabled(PwElement option) {
      return option.getAttribute(DISABLED_CLASS_INDICATOR) == null;
   }


   protected List<String> selectOptionsWithStrategy(PwElement container, Strategy strategy) {
      openDdl(container);
      List<PwElement> options = getAllOptionsElements(container);
      List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
      closeDdl(container);
      return selectedOptionsText;
   }


   protected List<String> selectOptionByStrategy(List<PwElement> options, Strategy strategy) {
      return getOptionsByStrategy(options, strategy).stream()
            .map(element -> {
               element.click();
               return element.textContent();
            })
            .toList();
   }


   protected List<PwElement> getOptionsByStrategy(List<PwElement> options, Strategy strategy) {
      return switch (strategy) {
         case FIRST -> List.of(StrategyGenerator.getFirstElementFromElements(options));
         case LAST -> List.of(StrategyGenerator.getLastElementFromElements(options));
         case RANDOM -> List.of(StrategyGenerator.getRandomElementFromElements(options));
         case ALL -> new ArrayList<>(options);
      };
   }


   protected PwElement findOptionByText(List<PwElement> options, String text) {
      return options.stream()
            .filter(option -> option.textContent().trim()
                  .equals(text))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Option with text '" + text + "' not found"));
   }


   protected void openDdl(PwElement container) {
      container.click();
   }


   protected void closeDdl(PwElement container) {
      openDdl(container);
   }


   protected List<PwElement> getAllOptionsElements(PwElement container) {
      List<PwElement> options = container.locator(OPTION_LOCATOR.selector()).allElements();
      return options.stream()
            .filter(option -> !checkIfOptionIsHidden(option))
            .toList();
   }


   protected boolean checkIfOptionIsSelected(PwElement option) {
      return option.getAttribute(SELECTED_ATTRIBUTE_INDICATOR) != null;
   }


   protected boolean checkIfOptionIsHidden(PwElement option) {
      return option.getAttribute(HIDDEN_ATTRIBUTE_INDICATOR) != null;
   }
}