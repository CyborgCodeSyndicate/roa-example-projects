package io.cyborgcode.ui.simple.test.framework.ui.components.select;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.select.Select;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.util.strategy.StrategyGenerator;
import io.cyborgcode.ui.simple.test.framework.ui.types.SelectFieldTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

@ImplementationOfType(SelectFieldTypes.Data.BOOTSTRAP_SELECT)
public class SelectBootstrapImpl extends BaseComponent implements Select {

   public static final By OPTION_LOCATOR = By.tagName("option");
   public static final String DISABLED_CLASS_INDICATOR = "disabled";
   public static final String SELECTED_ATTRIBUTE_INDICATOR = "selected";
   public static final String HIDDEN_ATTRIBUTE_INDICATOR = "hidden";


   public SelectBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void selectOptions(final SmartWebElement container, final String... values) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements(container);
      for (String value : values) {
         SmartWebElement option = findOptionByText(options, value);
         option.click();
      }
      closeDdl(container);
   }


   @Override
   public void selectOptions(final By containerLocator, final String... values) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      selectOptions(container, values);
   }


   @Override
   public List<String> selectOptions(final SmartWebElement container, final Strategy strategy) {
      return selectOptionsWithStrategy(container, strategy);
   }


   @Override
   public List<String> selectOptions(final By containerLocator, Strategy strategy) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return selectOptions(container, strategy);
   }


   @Override
   public List<String> getAvailableOptions(final SmartWebElement container) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements(container);
      List<String> availableOptions = options.stream()
            .map(option -> option.getText().trim())
            .collect(Collectors.toList());
      closeDdl(container);
      return availableOptions;
   }


   @Override
   public List<String> getAvailableOptions(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getAvailableOptions(container);
   }


   @Override
   public List<String> getSelectedOptions(final SmartWebElement container) {
      openDdl(container);
      List<String> checkedOptions = getAllOptionsElements(container).stream()
            .filter(this::checkIfOptionIsSelected)
            .map(SmartWebElement::getText)
            .collect(Collectors.toList());
      closeDdl(container);
      return checkedOptions;
   }


   @Override
   public List<String> getSelectedOptions(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getSelectedOptions(container);
   }


   @Override
   public boolean isOptionVisible(final SmartWebElement container, final String value) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements(container);
      try {
         findOptionByText(options, value);
         return true;
      } catch (NotFoundException e) {
         return false;
      }
   }


   @Override
   public boolean isOptionVisible(final By containerLocator, final String value) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return isOptionVisible(container, value);
   }


   @Override
   public boolean isOptionEnabled(final SmartWebElement container, final String value) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements(container);
      SmartWebElement option = findOptionByText(options, value);
      return isOptionEnabled(option);
   }


   @Override
   public boolean isOptionEnabled(final By containerLocator, final String value) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return isOptionEnabled(container, value);
   }


   protected boolean isOptionEnabled(SmartWebElement option) {
      return option.getAttribute(DISABLED_CLASS_INDICATOR) == null;
   }


   protected List<String> selectOptionsWithStrategy(SmartWebElement container, Strategy strategy) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements(container);
      List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
      closeDdl(container);
      return selectedOptionsText;
   }


   protected List<String> selectOptionByStrategy(List<SmartWebElement> options, Strategy strategy) {
      List<SmartWebElement> optionElements = getOptionsByStrategy(options, strategy);
      return optionElements.stream()
            .peek(SmartWebElement::click)
            .map(SmartWebElement::getText)
            .collect(Collectors.toList());
   }


   protected List<SmartWebElement> getOptionsByStrategy(List<SmartWebElement> options, Strategy strategy) {
      return switch (strategy) {
         case FIRST -> List.of(StrategyGenerator.getFirstElementFromElements(options));
         case LAST -> List.of(StrategyGenerator.getLastElementFromElements(options));
         case RANDOM -> List.of(StrategyGenerator.getRandomElementFromElements(options));
         case ALL -> new ArrayList<>(options);
      };
   }


   protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text) {
      return options.stream()
            .filter(option -> option.getText().trim()
                  .equals(text))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
   }


   protected void openDdl(SmartWebElement container) {
      container.click();
   }


   protected void closeDdl(SmartWebElement container) {
      openDdl(container);
   }


   protected List<SmartWebElement> getAllOptionsElements(SmartWebElement container) {
      List<SmartWebElement> options = container.findSmartElements(OPTION_LOCATOR);
      return options.stream()
            .filter(option -> !checkIfOptionIsHidden(option))
            .collect(Collectors.toList());
   }


   protected boolean checkIfOptionIsSelected(SmartWebElement option) {
      return option.getAttribute(SELECTED_ATTRIBUTE_INDICATOR) != null;
   }


   protected boolean checkIfOptionIsHidden(SmartWebElement option) {
      return option.getAttribute(HIDDEN_ATTRIBUTE_INDICATOR) != null;
   }
}