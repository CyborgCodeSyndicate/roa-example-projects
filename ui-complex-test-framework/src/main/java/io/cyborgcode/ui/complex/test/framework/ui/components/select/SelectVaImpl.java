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
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Vaadin-specific implementation of the {@link Select} component interface.
 * <p>
 * This class provides the concrete logic for interacting with Vaadin combo boxes
 * ({@code <vaadin-combo-box>} elements) in the Bakery Flow application. It is automatically
 * selected by ROA's component resolution mechanism when a select element is tagged with
 * {@link SelectFieldTypes#VA_SELECT_TYPE} via the {@code @ImplementationOfType} annotation.
 * </p>
 * <p>
 * Key capabilities:
 * <ul>
 *   <li>Select options by text, locator, or strategy (FIRST, LAST, RANDOM, ALL)</li>
 *   <li>Retrieve available and selected options</li>
 *   <li>Check option visibility and enabled state via {@code disabled} DOM attribute</li>
 *   <li>Handle Vaadin's overlay-based dropdown rendering ({@code <vaadin-combo-box-overlay>})</li>
 *   <li>Manage dropdown open/close state via {@code opened} attribute</li>
 *   <li>Handle stale element references during dynamic option loading</li>
 * </ul>
 * </p>
 * <p>
 * This implementation handles Vaadin's DOM structure, attribute-based state management
 * and asynchronous option loading, ensuring reliable interaction with navigation tabs in dynamic UIs.
 * </p>
 */
@ImplementationOfType(SelectFieldTypes.Data.VA_SELECT)
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
            .map(option -> option.findSmartElement(OPTION_TEXT_LOCATOR).getText().trim()).toList();
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
            .toList();
      closeDdl(container);
      return checkedOptions;
   }

   @Override
   public List<String> getSelectedOptions(By containerLocator) {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.ignoring(StaleElementReferenceException.class)
            .until(ExpectedConditions.stalenessOf(driver.findSmartElement(containerLocator)));

      List<SmartWebElement> containers = driver.findSmartElements(containerLocator);
      if (containers.size() == 1) {
         return getSelectedOptions(containers.get(0));
      } else {
         SmartWebElement container = containers.stream()
               .filter(c -> c.getDomAttribute("has-value") != null)
               .findFirst()
               .orElseThrow(() -> new IllegalStateException("No container with 'has-value' found"));
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
      return option.getDomAttribute(DISABLED_CLASS_INDICATOR) == null;
   }

   protected void openDdl(SmartWebElement ddlButton) {
      if (!"true".equals(ddlButton.getDomAttribute("opened"))) {
         SmartWebElement toggleButton = findDdlButton(ddlButton);
         toggleButton.click();
         SharedUiFunctions.waitForElementLoading(driver, ddlButton);
      }
   }

   protected void closeDdl(SmartWebElement ddlButton) {
      try {
          if ("true".equals(ddlButton.getDomAttribute("opened"))) {
              SmartWebElement toggleButton = findDdlButton(ddlButton);
              toggleButton.click();
          }
      } catch (StaleElementReferenceException e) {
          driver.findSmartElement(By.cssSelector("h2#title")).click();
          System.out.println("\n h2#title");
      }
   }

   protected SmartWebElement findDdlButton(SmartWebElement ddlRoot) {
      return ddlRoot.findSmartElement(OPEN_DDL_BUTTON_LOCATOR);
   }

   protected List<SmartWebElement> getAllOptionsElements() {
      List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
      return options.stream()
            .filter(element -> element.getDomAttribute("hidden") == null).toList();
   }

   protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text) {
      Wait<SmartWebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(2))
            .pollingEvery(Duration.ofMillis(500))
            .ignoring(NotFoundException.class);

      return wait.until(driver -> findOptionByText(options, text, OPTION_TEXT_LOCATOR));
   }

   protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text, By locator) {
      return options.stream()
            .filter(option -> option.findSmartElement(locator).getText().trim()
                  .equals(text))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
   }

   protected boolean checkIfOptionIsSelected(SmartWebElement option) {
      return option.getDomAttribute("selected") != null;
   }

   protected List<String> selectItemsWithStrategy(final SmartWebElement container, final Strategy strategy) {
      openDdl(container);
      List<SmartWebElement> options = getAllOptionsElements();
      return selectOptionByStrategy(options, strategy);
   }

   protected List<String> selectOptionByStrategy(List<SmartWebElement> options, Strategy strategy) {
      List<SmartWebElement> optionElements = getOptionsByStrategy(options, strategy);
      optionElements.forEach(this::selectIfNotChecked);
      return optionElements.stream()
              .map(SmartWebElement::getText)
              .toList();
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
          try {
              option.click();
          } catch (ElementNotInteractableException e) {
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
          }
      }
   }
}