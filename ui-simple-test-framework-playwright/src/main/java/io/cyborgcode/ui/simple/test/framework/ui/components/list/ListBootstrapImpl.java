package io.cyborgcode.ui.simple.test.framework.ui.components.list;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.list.ItemList;
import io.cyborgcode.roa.ui.util.strategy.Strategy;
import io.cyborgcode.roa.ui.playwright.util.strategy.StrategyGenerator;
import io.cyborgcode.ui.simple.test.framework.ui.types.ListFieldTypes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

   private static final PwBy LIST_ITEM_ELEMENT_SELECTOR = PwBy.css("li");
   private static final PwBy ITEM_LABEL_LOCATOR = PwBy.css("a");
   private static final String SELECTED_STATE = "active";
   private static final String DISABLED_STATE = "disabled";


   public ListBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void select(final PwElement container, final String... itemText) {
      performActionOnListItems(container, itemText, true);
   }


   @Override
   public void select(final PwBy containerLocator, final String... itemText) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      select(container, itemText);
   }


   @Override
   public String select(final PwElement container, final Strategy strategy) {
      return performActionOnListItemsWithStrategy(container, strategy, true);
   }


   @Override
   public String select(final PwBy containerLocator, final Strategy strategy) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return select(container, strategy);
   }


   @Override
   public void select(final String... itemText) {
      performActionOnListItems(null, itemText, true);
   }


   @Override
   public void select(final PwBy... itemListLocator) {
      performActionOnListItemsByLocator(itemListLocator, true);
   }


   @Override
   public void deSelect(final PwElement container, final String... itemText) {
      performActionOnListItems(container, itemText, false);
   }


   @Override
   public void deSelect(final PwBy containerLocator, final String... itemText) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      deSelect(container, itemText);
   }


   @Override
   public String deSelect(final PwElement container, final Strategy strategy) {
      return performActionOnListItemsWithStrategy(container, strategy, false);
   }


   @Override
   public String deSelect(final PwBy containerLocator, final Strategy strategy) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return deSelect(container, strategy);
   }


   @Override
   public void deSelect(final String... itemText) {
      performActionOnListItems(null, itemText, false);
   }


   @Override
   public void deSelect(final PwBy... itemListLocator) {
      performActionOnListItemsByLocator(itemListLocator, false);
   }


   @Override
   public boolean areSelected(final PwElement container, final String... itemText) {
      return checkListItemState(container, itemText);
   }


   @Override
   public boolean areSelected(final PwBy containerLocator, final String... itemText) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return areSelected(container, itemText);
   }


   @Override
   public boolean areSelected(final String... itemText) {
      return checkListItemState(null, itemText);
   }


   @Override
   public boolean areSelected(final PwBy... itemListLocator) {
      return checkListItemStateByLocator(itemListLocator);
   }


   @Override
   public boolean areEnabled(final PwElement container, final String... itemText) {
      return checkListItemsEnabledState(container, itemText);
   }


   @Override
   public boolean areEnabled(final PwBy containerLocator, final String... itemText) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return areEnabled(container, itemText);
   }


   @Override
   public boolean areEnabled(final String... itemText) {
      return checkListItemsEnabledState(null, itemText);
   }


   @Override
   public boolean areEnabled(final PwBy... itemLocator) {
      return checkListItemEnabledStateByLocator(itemLocator);
   }


   @Override
   public boolean areVisible(final PwElement container, final String... itemText) {
      return checkListItemsVisibleState(container, itemText);
   }


   @Override
   public boolean areVisible(final PwBy containerLocator, final String... itemText) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return areVisible(container, itemText);
   }


   @Override
   public boolean areVisible(final String... itemText) {
      return checkListItemsVisibleState(null, itemText);
   }


   @Override
   public boolean areVisible(final PwBy... itemLocator) {
      return checkListItemsVisibleStateByLocator(itemLocator);
   }


   @Override
   public List<String> getSelected(final PwElement container) {
      List<PwElement> listItems = findListItems(container, true);
      return listItems.stream().map(this::getLabel).toList();
   }


   @Override
   public List<String> getSelected(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getSelected(container);
   }


   @Override
   public List<String> getAll(final PwElement container) {
      List<PwElement> listItems = findListItems(container, null);
      return listItems.stream().map(this::getLabel).toList();
   }


   @Override
   public List<String> getAll(final PwBy containerLocator) {
      PwElement container = PwElement.of(driver.locator(containerLocator.selector()));
      return getAll(container);
   }


   private List<PwElement> findListItems(PwElement container, Boolean onlySelected) {
      List<PwElement> listItems = container != null
            ? container.locator(LIST_ITEM_ELEMENT_SELECTOR.selector()).allElements()
            : PwElement.of(driver.locator(LIST_ITEM_ELEMENT_SELECTOR.selector())).allElements();
      if (Objects.isNull(onlySelected)) {
         return listItems;
      }
      return onlySelected.booleanValue()
            ? listItems.stream().filter(this::isSelected).toList()
            : listItems.stream().filter(listItem -> !isSelected(listItem)).toList();
   }


   private void performActionOnListItems(PwElement container, String[] itemText, boolean select) {
      List<PwElement> listItems = findListItems(container, !select);
      listItems = filterListItemsByLabel(listItems, itemText);
      listItems.forEach(this::clickIfEnabled);
   }


   private String performActionOnListItemsWithStrategy(PwElement container, Strategy strategy, boolean select) {
      List<PwElement> listItems = findListItems(container, !select);
      return applyStrategyAndClick(listItems, strategy);
   }


   private void performActionOnListItemsByLocator(PwBy[] itemLocator, boolean select) {
      List<PwElement> listItems = Arrays.stream(itemLocator)
            .map(locator -> PwElement.of(driver.locator(locator.selector())))
            .toList();
      listItems = listItems.stream()
            .filter(listItem -> select != isSelected(listItem))
            .toList();
      listItems.forEach(this::clickIfEnabled);
   }


   private boolean checkListItemState(PwElement container, String[] itemText) {
      List<PwElement> listItems = findListItems(container, true);
      Set<String> labelSet = Set.of(itemText);
      List<PwElement> matchingListItems = listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .toList();
      return matchingListItems.size() == itemText.length;
   }


   private boolean checkListItemStateByLocator(PwBy[] itemLocator) {
      return Arrays.stream(itemLocator)
            .map(locator -> PwElement.of(driver.locator(locator.selector())))
            .allMatch(this::isSelected);
   }


   private boolean checkListItemsVisibleState(PwElement container, String[] itemText) {
      List<PwElement> listItems = findListItems(container, null);
      Set<String> labelSet = Set.of(itemText);
      List<String> itemListAllLabels = listItems.stream().map(this::getLabel).toList();
      return new HashSet<>(itemListAllLabels).containsAll(labelSet);
   }


   private boolean checkListItemsVisibleStateByLocator(PwBy[] itemLocator) {
      List<PwElement> listItems = findListItems(null, null);
      Set<PwElement> labelSet = Arrays.stream(itemLocator)
            .map(locator -> PwElement.of(driver.locator(locator.selector())))
            .collect(Collectors.toSet());
      return new HashSet<>(listItems).containsAll(labelSet);
   }


   private boolean checkListItemsEnabledState(PwElement container, String[] itemText) {
      List<PwElement> listItems = findListItems(container, null);
      Set<String> labelSet = Set.of(itemText);
      return listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .allMatch(this::isEnabled);
   }

   private boolean checkListItemEnabledStateByLocator(PwBy[] itemLocator) {
      return Arrays.stream(itemLocator)
            .map(locator -> PwElement.of(driver.locator(locator.selector())))
            .allMatch(this::isEnabled);
   }


   private List<PwElement> filterListItemsByLabel(List<PwElement> listItems, String[] labels) {
      Set<String> labelSet = Set.of(labels);
      return listItems.stream()
            .filter(listItem -> labelSet.contains(getLabel(listItem)))
            .toList();
   }


   private String applyStrategyAndClick(List<PwElement> listItems, Strategy strategy) {
      if (listItems.isEmpty()) {
         return "No action required";
      }
      if (strategy != null) {
         String selectedListItemLabel;
         switch (strategy) {
            case RANDOM -> {
               PwElement randomListItem = StrategyGenerator.getRandomElementFromElements(listItems);
               clickIfEnabled(randomListItem);
               selectedListItemLabel = getLabel(randomListItem);
               return selectedListItemLabel;
            }
            case FIRST -> {
               PwElement firstListItem = StrategyGenerator.getFirstElementFromElements(listItems);
               clickIfEnabled(firstListItem);
               selectedListItemLabel = getLabel(firstListItem);
               return selectedListItemLabel;
            }
            case LAST -> {
               PwElement lastListItems = StrategyGenerator.getLastElementFromElements(listItems);
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


   private void clickIfEnabled(PwElement listItem) {
      if (isEnabled(listItem)) {
         listItem.click();
      }
   }


   private String getLabel(PwElement listItem) {
      PwElement label = listItem.locator(ITEM_LABEL_LOCATOR.selector());
      return label.textContent().trim();
   }


   private boolean hasClass(PwElement element, String classToken) {
      String classes = element.getAttribute("class");
      return classes != null && classes.contains(classToken);
   }


   private boolean isSelected(PwElement listItem) {
      return hasClass(listItem, SELECTED_STATE);
   }


   private boolean isEnabled(PwElement listItem) {
      return !hasClass(listItem, DISABLED_STATE);
   }
}