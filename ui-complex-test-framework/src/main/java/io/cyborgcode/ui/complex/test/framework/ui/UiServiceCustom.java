package io.cyborgcode.ui.complex.test.framework.ui;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.fluent.*;
import io.cyborgcode.roa.ui.service.tables.TableServiceFluent;

public class UiServiceCustom extends UiServiceFluent<UiServiceCustom> {

   public UiServiceCustom(SmartWebDriver driver, SuperQuest quest) {
      super(driver);
      this.quest = quest;
      postQuestSetupInitialization();
   }

   public InputServiceFluent<UiServiceCustom> input() {
      return getInputField();
   }

   public TableServiceFluent<UiServiceCustom> table() {
      return getTable();
   }

   public InterceptorServiceFluent<UiServiceCustom> interceptor() {
      return getInterceptor();
   }

   public InsertionServiceFluent<UiServiceCustom> insertion() {
      return getInsertionService();
   }

   public ButtonServiceFluent<UiServiceCustom> button() {
      return getButtonField();
   }

   public RadioServiceFluent<UiServiceCustom> radio() {
      return getRadioField();
   }

   public SelectServiceFluent<UiServiceCustom> select() {
      return getSelectField();
   }

   public CheckboxServiceFluent<UiServiceCustom> checkbox() {
      return getCheckboxField();
   }

   public ListServiceFluent<UiServiceCustom> list() {
      return getListField();
   }

   public LinkServiceFluent<UiServiceCustom> link() {
      return getLinkField();
   }

   public AlertServiceFluent<UiServiceCustom> alert() {
      return getAlertField();
   }

   public NavigationServiceFluent<UiServiceCustom> browser() {
      return getNavigation();
   }

   public ValidationServiceFluent<UiServiceCustom> validate() {
      return getValidation();
   }

}
