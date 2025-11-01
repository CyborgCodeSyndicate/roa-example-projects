package io.cyborgcode.ui.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.list.ItemListComponentType;
import io.cyborgcode.roa.ui.selenium.ListUiElement;
import io.cyborgcode.ui.test.framework.ui.types.ListFieldTypes;
import org.openqa.selenium.By;

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
