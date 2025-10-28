package io.cyborgcode.ui.complex.test.framework.ui.components.select;

import io.cyborgcode.ui.complex.test.framework.ui.functions.SharedUiFunctions;
import io.cyborgcode.ui.complex.test.framework.ui.types.SelectFieldTypes;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.select.Select;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ImplementationOfType(SelectFieldTypes.VA_SELECT)
public class SelectVaImpl extends BaseComponent implements Select {

   public static final By OPTIONS_CONTAINER_LOCATOR = By.cssSelector("vaadin-combo-box-overlay#overlay");
   public static final By OPTIONS_ROOT_LOCATOR = By.cssSelector("iron-list#selector vaadin-combo-box-item");
   public static final By OPEN_DDL_BUTTON_LOCATOR = By.id("toggleButton");
   public static final By OPTION_LOCATOR = By.cssSelector("vaadin-combo-box-item");
   public static final By OPTION_TEXT_LOCATOR = By.cssSelector("div#content");


   public static final String DISABLED_CLASS_INDICATOR = "disabled";

   public SelectVaImpl(SmartWebDriver driver) {
      super(driver);
   }

   @Override
   public void selectOptions(final SmartWebElement container, final String... values) {
      openDdl(container);
      List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
      for (String value : values) {
         SmartWebElement option = findOptionByText(options, value);
         selectIfNotChecked(option);
      }
      closeDdl(container);
   }

   @Override
   public void selectOptions(final By containerLocator, final String... values) {
      selectOptions(driver.findSmartElement(containerLocator), values);
   }

   @Override
   public List<String> selectOptions(SmartWebElement container, Strategy strategy) {
      return selectItemsWithStrategy(container, strategy);
   }

   @Override
   public List<String> selectOptions(final By containerLocator, Strategy strategy) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return selectOptions(container, strategy);
   }

   @Override
   public List<String> getAvailableOptions(SmartWebElement container) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements();
      List<String> availableOptions = options.stream()
            .map(option -> option.findSmartElement(OPTION_TEXT_LOCATOR).getText().trim())
            .collect(Collectors.toList());
      closeDdl(container);
      return availableOptions;
   }

   @Override
   public List<String> getAvailableOptions(By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getAvailableOptions(container);
   }

   @Override
   public List<String> getSelectedOptions(SmartWebElement container) {
      openDdl(container);
      List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
      List<String> checkedOptions = options.stream()
            .filter(this::checkIfOptionIsSelected)
            .map(SmartWebElement::getText)
            .collect(Collectors.toList());
      closeDdl(container);
      return checkedOptions;
   }

   @Override
   public List<String> getSelectedOptions(By containerLocator) {
      //todo: fix StaleElement for shadowRoot
      try {
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
         wait.ignoring(StaleElementReferenceException.class)
               .until(ExpectedConditions.stalenessOf(driver.findSmartElement(containerLocator)));
      } catch (TimeoutException e) {
         e.printStackTrace();
      }

      List<SmartWebElement> containers = driver.findSmartElements(containerLocator);
      if (containers.size() == 1) {
         return getSelectedOptions(containers.get(0));
      } else {
         SmartWebElement container = containers.stream()
               .filter(c -> c.getAttribute("has-value") != null)
               .findFirst().get();
         return getSelectedOptions(container);
      }
   }

   @Override
   public boolean isOptionVisible(SmartWebElement container, String value) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements();
      try {
         findOptionByText(options, value);
         return true;
      } catch (NotFoundException e) {
         return false;
      }
   }

   @Override
   public boolean isOptionVisible(By containerLocator, String value) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return isOptionVisible(container, value);
   }

   @Override
   public boolean isOptionEnabled(SmartWebElement container, String value) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements();
      SmartWebElement option = findOptionByText(options, value);
      return isOptionEnabled(option);
   }

   @Override
   public boolean isOptionEnabled(By containerLocator, String value) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return isOptionEnabled(container, value);
   }


   protected boolean isOptionEnabled(SmartWebElement option) {
      return option.getAttribute(DISABLED_CLASS_INDICATOR) == null;
   }

   protected void openDdl(SmartWebElement ddlButton) {
      if (!"true".equals(ddlButton.getAttribute("opened"))) {
         SmartWebElement toggleButton = findDdlButton(ddlButton);
         toggleButton.click();
         SharedUiFunctions.waitForElementLoading(driver, ddlButton);
      }
   }

   protected void closeDdl(SmartWebElement ddlButton) {
      if ("true".equals(ddlButton.getAttribute("opened"))) { //todo- getAttribute: StaleElementReferenceException
         SmartWebElement toggleButton = findDdlButton(ddlButton);
         toggleButton.click();
      }
   }

   protected SmartWebElement findDdlButton(SmartWebElement ddlRoot) {
      return ddlRoot.findSmartElement(OPEN_DDL_BUTTON_LOCATOR);
   }

   protected List<SmartWebElement> getAllOptionsElements() {
      List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
      return options.stream()
            .filter(element -> element.getAttribute("hidden") == null)
            .collect(Collectors.toList());
   }

   protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text) {
      return findOptionByText(options, text, OPTION_TEXT_LOCATOR);
   }

   protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text, By locator) {
      return options.stream()
            .filter(option -> option.findSmartElement(locator).getText().trim()
                  .equals(text))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
   }

   protected boolean checkIfOptionIsSelected(SmartWebElement option) {
      return option.getAttribute("selected") != null;
   }

   protected List<String> selectItemsWithStrategy(final SmartWebElement container, final Strategy strategy) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements();
      List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
      //closeDdl(container);
      return selectedOptionsText;
   }

   protected List<String> selectOptionByStrategy(List<SmartWebElement> options, Strategy strategy) {
      List<SmartWebElement> optionElements = getOptionsByStrategy(options, strategy);
      return optionElements.stream()
            .peek(this::selectIfNotChecked)
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

   protected void selectIfNotChecked(SmartWebElement option) {
      if (!checkIfOptionIsSelected(option)) {
         option.click(); //todo- ElementNotInteractableException
      }
   }
}