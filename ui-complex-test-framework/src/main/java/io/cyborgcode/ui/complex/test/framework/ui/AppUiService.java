package io.cyborgcode.ui.complex.test.framework.ui;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.fluent.*;
import io.cyborgcode.roa.ui.service.tables.TableServiceFluent;

public class AppUiService extends UiServiceFluent<AppUiService> {

   public AppUiService(SmartWebDriver driver, SuperQuest quest) {
      super(driver);
      this.quest = quest;
      postQuestSetupInitialization();
   }

   public InputServiceFluent<AppUiService> input() {
      return getInputField();
   }

   public TableServiceFluent<AppUiService> table() {
      return getTable();
   }

   public InterceptorServiceFluent<AppUiService> interceptor() {
      return getInterceptor();
   }

   public InsertionServiceFluent<AppUiService> insertion() {
      return getInsertionService();
   }

   public ButtonServiceFluent<AppUiService> button() {
      return getButtonField();
   }

   public RadioServiceFluent<AppUiService> radio() {
      return getRadioField();
   }

   public SelectServiceFluent<AppUiService> select() {
      return getSelectField();
   }

   public CheckboxServiceFluent<AppUiService> checkbox() {
      return getCheckboxField();
   }

   public ListServiceFluent<AppUiService> list() {
      return getListField();
   }

   public LinkServiceFluent<AppUiService> link() {
      return getLinkField();
   }

   public AlertServiceFluent<AppUiService> alert() {
      return getAlertField();
   }

   public NavigationServiceFluent<AppUiService> browser() {
      return getNavigation();
   }

   public ValidationServiceFluent<AppUiService> validate() {
      return getValidation();
   }

}
