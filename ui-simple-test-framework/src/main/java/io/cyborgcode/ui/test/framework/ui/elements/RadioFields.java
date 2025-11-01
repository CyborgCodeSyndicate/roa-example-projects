package io.cyborgcode.ui.test.framework.ui.elements;

import io.cyborgcode.roa.ui.components.base.ComponentType;
import io.cyborgcode.roa.ui.components.radio.RadioComponentType;
import io.cyborgcode.roa.ui.selenium.RadioUiElement;
import io.cyborgcode.ui.test.framework.ui.types.RadioFieldTypes;
import org.openqa.selenium.By;

public enum RadioFields implements RadioUiElement {

   DOLLARS_RADIO_FIELD(By.id("pc_inDollars_true"), RadioFieldTypes.BOOTSTRAP_RADIO_TYPE);

   private final By locator;
   private final RadioComponentType componentType;


   RadioFields(final By locator, final RadioComponentType componentType) {
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
