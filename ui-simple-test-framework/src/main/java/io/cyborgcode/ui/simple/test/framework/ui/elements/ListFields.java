package io.cyborgcode.ui.simple.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.list.ItemListComponentType;
import io.cyborgcode.roa.ui.selenium.ListUiElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.ListFieldTypes;
import org.openqa.selenium.By;

/**
 * Registry of list UI elements (e.g., tab groups) for the Zero Bank demo application.
 *
 * <p>Each enum constant defines a specific list container with its Selenium {@link By}
 * locator and concrete component type (see {@link ItemListComponentType}).
 * 
 * <p>Implements {@link ListUiElement} to integrate with ROA
 * fluent UI testing API for selecting items by label and asserting list state.
 *
 * <p>Example usage:
 * <pre>{@code
 * quest
 *     .use(Rings.RING_OF_UI)
 *     .list().select(ListFields.NAVIGATION_TABS, "Pay Bills");
 * }</pre>
 *
 * <p>Typical usage targets Bootstrap-styled list groups or tab strips via {@link ListFieldTypes},
 * enabling consistent identification and interaction across flows.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ListFields implements ListUiElement {

   NAVIGATION_TABS(By.className("nav-tabs"), ListFieldTypes.BOOTSTRAP_LIST_TYPE),
   PAY_BILLS_TABS(By.className("ui-tabs-nav"), ListFieldTypes.BOOTSTRAP_LIST_TYPE),
   ACCOUNT_ACTIVITY_TABS(By.className("ui-tabs-nav"), ListFieldTypes.BOOTSTRAP_LIST_TYPE);

   private final By locator;
   private final ItemListComponentType componentType;


   ListFields(final By locator, final ItemListComponentType componentType) {
      this.locator = locator;
      this.componentType = componentType;
   }


   @Override
   public By locator() {
      return locator;
   }


   @Override
   public <T extends ComponentType> T componentType() {
      return (T) componentType;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }

}
