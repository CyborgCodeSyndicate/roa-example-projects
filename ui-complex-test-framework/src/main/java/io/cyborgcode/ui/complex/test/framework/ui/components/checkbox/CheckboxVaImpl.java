package io.cyborgcode.ui.complex.test.framework.ui.components.checkbox;

import io.cyborgcode.ui.complex.test.framework.ui.types.CheckboxFieldTypes;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.checkbox.Checkbox;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.cyborgcode.roa.ui.util.strategy.StrategyGenerator.*;


@ImplementationOfType(CheckboxFieldTypes.VA_CHECKBOX)
public class CheckboxVaImpl extends BaseComponent implements Checkbox {

   private static final By CHECKBOX_ELEMENT_SELECTOR = By.tagName("mat-checkbox");
   private static final String CHECKED_CLASS_INDICATOR = "checked";
   private static final String DISABLED_STATE = "disabled";
   public static final By CHECKBOX_LABEL_LOCATOR = By.className("mat-checkbox-label");


   public CheckboxVaImpl(SmartWebDriver driver) {
      super(driver);
   }

   @Override
   public void select(SmartWebElement container, String... checkBoxText) {
      performActionOnCheckboxes(container, checkBoxText, true);
   }

   @Override
   public String select(SmartWebElement container, Strategy strategy) {
      return performActionOnCheckboxesWithStrategy(container, strategy, true);
   }

   @Override
   public void select(String... checkBoxText) {
      performActionOnCheckboxes(null, checkBoxText, true);
   }

   @Override
   public void select(By... checkBoxLocator) {
      performActionOnCheckboxesByLocator(checkBoxLocator, true);
   }

   @Override
   public void deSelect(SmartWebElement container, String... checkBoxText) {
      performActionOnCheckboxes(container, checkBoxText, false);
   }

   @Override
   public String deSelect(SmartWebElement container, Strategy strategy) {
      return performActionOnCheckboxesWithStrategy(container, strategy, false);
   }

   @Override
   public void deSelect(String... checkBoxText) {
      performActionOnCheckboxes(null, checkBoxText, false);
   }

   @Override
   public void deSelect(By... checkBoxLocator) {
      performActionOnCheckboxesByLocator(checkBoxLocator, false);
   }

   @Override
   public boolean areSelected(SmartWebElement container, String... checkBoxText) {
      return checkCheckboxState(container, checkBoxText);
   }

   @Override
   public boolean areSelected(String... checkBoxText) {
      return checkCheckboxState(null, checkBoxText);
   }

   @Override
   public boolean areSelected(By... checkBoxLocator) {
      return checkCheckboxStateByLocator(checkBoxLocator);
   }

   @Override
   public boolean areEnabled(SmartWebElement container, String... checkBoxText) {
      return checkCheckboxEnabledState(container, checkBoxText);
   }

   @Override
   public boolean areEnabled(String... checkBoxText) {
      return checkCheckboxEnabledState(null, checkBoxText);
   }

   @Override
   public boolean areEnabled(By... checkBoxLocator) {
      return checkCheckboxEnabledStateByLocator(checkBoxLocator);
   }

   @Override
   public List<String> getSelected(SmartWebElement container) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, true);
      return checkBoxes.stream().map(this::getLabel).collect(Collectors.toList());
   }

   @Override
   public List<String> getSelected(By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getSelected(container);
   }

   @Override
   public List<String> getAll(SmartWebElement container) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, null);
      return checkBoxes.stream().map(this::getLabel).collect(Collectors.toList());
   }

   @Override
   public List<String> getAll(By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getAll(container);
   }

   private List<SmartWebElement> findCheckboxes(SmartWebElement container, Boolean onlySelected) {
      List<SmartWebElement> checkBoxes = container != null
            ? container.findSmartElements(CHECKBOX_ELEMENT_SELECTOR)
            : driver.findSmartElements(CHECKBOX_ELEMENT_SELECTOR);
      if (Objects.isNull(onlySelected)) {
         return checkBoxes;
      }
      return onlySelected ? checkBoxes.stream().filter(this::isChecked).collect(Collectors.toList()) :
            checkBoxes.stream().filter(checkBox -> !isChecked(checkBox)).collect(Collectors.toList());
   }

   private void performActionOnCheckboxes(SmartWebElement container, String[] checkBoxText, boolean select) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, !select);
      checkBoxes = filterCheckboxesByLabel(checkBoxes, checkBoxText);
      checkBoxes.forEach(this::clickIfEnabled);
   }

   private String performActionOnCheckboxesWithStrategy(SmartWebElement container, Strategy strategy, boolean select) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, !select);
      return applyStrategyAndClick(checkBoxes, strategy);
   }

   private void performActionOnCheckboxesByLocator(By[] checkBoxLocator, boolean select) {
      List<SmartWebElement> checkBoxes = Arrays.stream(checkBoxLocator)
            .map(driver::findSmartElement)
            .collect(Collectors.toList());
      checkBoxes = checkBoxes.stream().filter(checkBox -> select != isChecked(checkBox)).toList();
      checkBoxes.forEach(this::clickIfEnabled);
   }

   private boolean checkCheckboxState(SmartWebElement container, String[] checkBoxText) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, true);
      Set<String> labelSet = Set.of(checkBoxText);
      List<SmartWebElement> matchingCheckBoxes = checkBoxes.stream()
            .filter(checkBox -> labelSet.contains(getLabel(checkBox)))
            .toList();
      return matchingCheckBoxes.size() == checkBoxText.length;
   }

   private boolean checkCheckboxStateByLocator(By[] checkBoxLocator) {
      return Arrays.stream(checkBoxLocator)
            .map(driver::findSmartElement)
            .allMatch(this::isChecked);
   }

   private boolean checkCheckboxEnabledState(SmartWebElement container, String[] checkBoxText) {
      List<SmartWebElement> checkBoxes = findCheckboxes(container, null);
      Set<String> labelSet = Set.of(checkBoxText);
      return checkBoxes.stream()
            .filter(checkBox -> labelSet.contains(getLabel(checkBox)))
            .allMatch(this::isEnabled);
   }

   private boolean checkCheckboxEnabledStateByLocator(By[] checkBoxLocator) {
      return Arrays.stream(checkBoxLocator)
            .map(driver::findSmartElement)
            .allMatch(this::isEnabled);
   }

   private List<SmartWebElement> filterCheckboxesByLabel(List<SmartWebElement> checkBoxes, String[] labels) {
      Set<String> labelSet = Set.of(labels);
      return checkBoxes.stream().filter(checkBox -> labelSet.contains(getLabel(checkBox)))
            .collect(Collectors.toList());
   }

   private String applyStrategyAndClick(List<SmartWebElement> checkBoxes, Strategy strategy) {
      if (checkBoxes.isEmpty()) {
         return "No action required";
      }
      if (strategy != null) {
         String selectedCheckBoxLabel;
         switch (strategy) {
            case RANDOM:
               SmartWebElement randomCheckBox = getRandomElementFromElements(checkBoxes);
               clickIfEnabled(randomCheckBox);
               selectedCheckBoxLabel = getLabel(randomCheckBox);
               //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
               return selectedCheckBoxLabel;
            case FIRST:
               SmartWebElement firstCheckBox = getFirstElementFromElements(checkBoxes);
               clickIfEnabled(firstCheckBox);
               selectedCheckBoxLabel = getLabel(firstCheckBox);
               //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
               return selectedCheckBoxLabel;
            case LAST:
               SmartWebElement lastCheckBox = getLastElementFromElements(checkBoxes);
               clickIfEnabled(lastCheckBox);
               selectedCheckBoxLabel = getLabel(lastCheckBox);
               //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
               return selectedCheckBoxLabel;
            case ALL:
               String allSelected = checkBoxes.stream().map(this::getLabel).toList()
                     .toString();
               checkBoxes.forEach(this::clickIfEnabled);
               //info("Select or Deselect all checkboxes");
               return allSelected;
            default:
               throw new IllegalStateException("Unexpected strategy: " + strategy);
         }
      } else {
         checkBoxes.forEach(this::clickIfEnabled);
      }
      return null;
   }

   private void clickIfEnabled(SmartWebElement checkBox) {
      if (isEnabled(checkBox)) {
         String checkBoxClass = checkBox.getAttribute("class");
         checkBox.click();
         checkBox.waitUntilAttributeValueIsChanged("class", checkBoxClass);
         checkBoxClass = checkBox.getAttribute("class");
         checkBox.waitUntilAttributeValueIsChanged("class", checkBoxClass);
      }
   }

   private boolean isChecked(SmartWebElement checkBox) {
      return checkBox.getAttribute(CHECKED_CLASS_INDICATOR) != null;
   }

   private boolean isEnabled(SmartWebElement checkBox) {
      return checkBox.getAttribute(DISABLED_STATE) == null;
   }

   private String getLabel(SmartWebElement checkBox) {
      SmartWebElement label = checkBox.findSmartElement(CHECKBOX_LABEL_LOCATOR);
      return label.getText().trim();
   }
}
