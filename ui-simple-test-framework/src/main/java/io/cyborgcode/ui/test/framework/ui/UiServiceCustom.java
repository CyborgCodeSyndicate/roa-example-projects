package io.cyborgcode.ui.test.framework.ui;


import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.fluent.AlertServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.ButtonServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.CheckboxServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.InputServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.InsertionServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.InterceptorServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.LinkServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.ListServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.NavigationServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.RadioServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.SelectServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.UiServiceFluent;
import io.cyborgcode.roa.ui.service.fluent.ValidationServiceFluent;
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
