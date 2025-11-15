package io.cyborgcode.ui.simple.test.framework.ui.components.list;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.list.ItemList;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.util.strategy.StrategyGenerator;
import io.cyborgcode.ui.simple.test.framework.ui.types.ListFieldTypes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;

/**
 * Bootstrap-specific implementation of the {@link ItemList} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap-styled list groups
 * (lists composed of {@code <li>} items with nested {@code <a>} labels). It is automatically
 * selected by ROA component resolution mechanism when a list field is tagged with
 * {@link ListFieldTypes#BOOTSTRAP_LIST_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Select or deselect items by label, container, or direct locator</li>
 *   <li>Strategy-driven selection (RANDOM, FIRST, LAST, ALL) via {@link Strategy}</li>
 *   <li>State checks: selected, enabled, visible — for specific labels or locators</li>
 *   <li>Retrieve item labels: {@code getSelected(...)} and {@code getAll(...)}</li>
 *   <li>Consistent label resolution via nested {@code <a>} elements</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap list markup and class conventions:
 * selected state via {@code selected} CSS class, disabled state via {@code disabled} CSS class,
 * and item labeling via anchor text.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(ListFieldTypes.Data.BOOTSTRAP_LIST)
public class ListBootstrapImpl extends BaseComponent implements ItemList {

   private static final By LIST_ITEM_ELEMENT_SELECTOR = By.tagName("li");
   private static final By ITEM_LABEL_LOCATOR = By.tagName("a");
   private static final String SELECTED_STATE = "active";
   private static final String DISABLED_STATE = "disabled";


   public ListBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void select(final SmartWebElement container, final String... itemText) {
      performActionOnListItems(container, itemText, true);
   }


   @Override
   public void select(final By containerLocator, final String... itemText) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      select(container, itemText);
   }


   @Override
   public String select(final SmartWebElement container, final Strategy strategy) {
      return performActionOnListItemsWithStrategy(container, strategy, true);
   }


   @Override
   public String select(final By containerLocator, final Strategy strategy) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return select(container, strategy);
   }


   @Override
   public void select(final String... itemText) {
      performActionOnListItems(null, itemText, true);
   }


   @Override
   public void select(final By... itemListLocator) {
      performActionOnListItemsByLocator(itemListLocator, true);
   }


   @Override
   public void deSelect(final SmartWebElement container, final String... itemText) {
      performActionOnListItems(container, itemText, false);
   }


   @Override
   public void deSelect(final By containerLocator, final String... itemText) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      deSelect(container, itemText);
   }


   @Override
   public String deSelect(final SmartWebElement container, final Strategy strategy) {
      return performActionOnListItemsWithStrategy(container, strategy, false);
   }


   @Override
   public String deSelect(final By containerLocator, final Strategy strategy) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return deSelect(container, strategy);
   }


   @Override
   public void deSelect(final String... itemText) {
      performActionOnListItems(null, itemText, false);
   }


   @Override
   public void deSelect(final By... itemListLocator) {
      performActionOnListItemsByLocator(itemListLocator, false);
   }


   @Override
   public boolean areSelected(final SmartWebElement container, final String... itemText) {
      return checkListItemState(container, itemText);
   }


   @Override
   public boolean areSelected(final By containerLocator, final String... itemText) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return areSelected(container, itemText);
   }


   @Override
   public boolean areSelected(final String... itemText) {
      return checkListItemState(null, itemText);
   }


   @Override
   public boolean areSelected(final By... itemListLocator) {
      return checkListItemStateByLocator(itemListLocator);
   }


   @Override
   public boolean areEnabled(final SmartWebElement container, final String... itemText) {
      return checkListItemsEnabledState(container, itemText);
   }


   @Override
   public boolean areEnabled(final By containerLocator, final String... itemText) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return areEnabled(container, itemText);
   }


   @Override
   public boolean areEnabled(final String... itemText) {
      return checkListItemsEnabledState(null, itemText);
   }


   @Override
   public boolean areEnabled(final By... itemLocator) {
      return checkListItemEnabledStateByLocator(itemLocator);
   }


   @Override
   public boolean areVisible(final SmartWebElement container, final String... itemText) {
      return checkListItemsVisibleState(container, itemText);
   }


   @Override
   public boolean areVisible(final By containerLocator, final String... itemText) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return areVisible(container, itemText);
   }


   @Override
   public boolean areVisible(final String... itemText) {
      return checkListItemsVisibleState(null, itemText);
   }


   @Override
   public boolean areVisible(final By... itemLocator) {
      return checkListItemsVisibleStateByLocator(itemLocator);
   }


   @Override
   public List<String> getSelected(final SmartWebElement container) {
      List<SmartWebElement> listItems = findListItems(container, true);
      return listItems.stream().map(this::getLabel).toList();
   }


   @Override
   public List<String> getSelected(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getSelected(container);
   }


   @Override
   public List<String> getAll(final SmartWebElement container) {
      List<SmartWebElement> listItems = findListItems(container, null);
      return listItems.stream().map(this::getLabel).toList();
   }


   @Override
   public List<String> getAll(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getAll(container);
   }


   private List<SmartWebElement> findListItems(SmartWebElement container, Boolean onlySelected) {
      List<SmartWebElement> listItems = container != null
            ? container.findSmartElements(LIST_ITEM_ELEMENT_SELECTOR)
            : driver.findSmartElements(LIST_ITEM_ELEMENT_SELECTOR);
      if (Objects.isNull(onlySelected)) {
         return listItems;
      }
      return onlySelected.booleanValue()
            ? listItems.stream().filter(this::isSelected).toList()
            : listItems.stream().filter(listItem -> !isSelected(listItem)).toList();
   }


   private void performActionOnListItems(SmartWebElement container, String[] itemText, boolean select) {
      List<SmartWebElement> listItems = findListItems(container, !select);
      listItems = filterListItemsByLabel(listItems, itemText);
      listItems.forEach(this::clickIfEnabled);
   }


   private String performActionOnListItemsWithStrategy(SmartWebElement container, Strategy strategy, boolean select) {
      List<SmartWebElement> listItems = findListItems(container, !select);
      return applyStrategyAndClick(listItems, strategy);
   }


   private void performActionOnListItemsByLocator(By[] itemLocator, boolean select) {
      List<SmartWebElement> listItems = Arrays.stream(itemLocator)
            .map(driver::findSmartElement)
            .toList();
      listItems = listItems.stream().filter(listItem -> select != isSelected(listItem)).toList();
      listItems.forEach(this::clickIfEnabled);
   }


   private boolean checkListItemState(SmartWebElement container, String[] itemText) {
      List<SmartWebElement> listItems = findListItems(container, true);
      Set<String> labelSet = Set.of(itemText);
      List<SmartWebElement> matchingListItems = listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .toList();
      return matchingListItems.size() == itemText.length;
   }


   private boolean checkListItemStateByLocator(By[] itemLocator) {
      return Arrays.stream(itemLocator)
            .map(driver::findSmartElement)
            .allMatch(this::isSelected);
   }


   private boolean checkListItemsVisibleState(SmartWebElement container, String[] itemText) {
      List<SmartWebElement> listItems = findListItems(container, null);
      Set<String> labelSet = Set.of(itemText);
      List<String> itemListAllLabels = listItems.stream().map(this::getLabel).toList();
      return new HashSet<>(itemListAllLabels).containsAll(labelSet);
   }


   private boolean checkListItemsVisibleStateByLocator(By[] itemLocator) {
      List<SmartWebElement> listItems = findListItems(null, null);
      Set<SmartWebElement> labelSet = Arrays.stream(itemLocator)
            .map(driver::findSmartElement)
            .collect(Collectors.toSet());
      return new HashSet<>(listItems).containsAll(labelSet);
   }


   private boolean checkListItemsEnabledState(SmartWebElement container, String[] itemText) {
      List<SmartWebElement> listItems = findListItems(container, null);
      Set<String> labelSet = Set.of(itemText);
      return listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .allMatch(this::isEnabled);
   }

   private boolean checkListItemEnabledStateByLocator(By[] itemLocator) {
      return Arrays.stream(itemLocator)
            .map(driver::findSmartElement)
            .allMatch(this::isEnabled);
   }


   private List<SmartWebElement> filterListItemsByLabel(List<SmartWebElement> listItems, String[] labels) {
      Set<String> labelSet = Set.of(labels);
      return listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .toList();
   }


   private String applyStrategyAndClick(List<SmartWebElement> listItems, Strategy strategy) {
      if (listItems.isEmpty()) {
         return "No action required";
      }
      if (strategy != null) {
         String selectedListItemLabel;
         switch (strategy) {
            case RANDOM -> {
               SmartWebElement randomListItem = StrategyGenerator.getRandomElementFromElements(listItems);
               clickIfEnabled(randomListItem);
               selectedListItemLabel = getLabel(randomListItem);
               return selectedListItemLabel;
            }
            case FIRST -> {
               SmartWebElement firstListItem = StrategyGenerator.getFirstElementFromElements(listItems);
               clickIfEnabled(firstListItem);
               selectedListItemLabel = getLabel(firstListItem);
               return selectedListItemLabel;
            }
            case LAST -> {
               SmartWebElement lastListItems = StrategyGenerator.getLastElementFromElements(listItems);
               clickIfEnabled(lastListItems);
               selectedListItemLabel = getLabel(lastListItems);
               return selectedListItemLabel;
            }
            case ALL -> {
               String allSelected = listItems.stream()
                     .map(this::getLabel)
                     .toList()
                     .toString();
               listItems.forEach(this::clickIfEnabled);
               return allSelected;
            }
            default -> throw new IllegalStateException("Unexpected strategy: " + strategy);
         }
      } else {
         listItems.forEach(this::clickIfEnabled);
      }
      return null;
   }


   private void clickIfEnabled(SmartWebElement listItem) {
      if (isEnabled(listItem)) {
         listItem.click();
      }
   }


   private String getLabel(SmartWebElement listItem) {
      SmartWebElement label = listItem.findSmartElement(ITEM_LABEL_LOCATOR);
      return label.getText().trim();
   }


   private boolean hasClass(SmartWebElement element, String classToken) {
      String classes = element.getDomAttribute("class");
      return classes != null && classes.contains(classToken);
   }


   private boolean isSelected(SmartWebElement listItem) {
      return hasClass(listItem, SELECTED_STATE);
   }


   private boolean isEnabled(SmartWebElement listItem) {
      return !hasClass(listItem, DISABLED_STATE);
   }
}