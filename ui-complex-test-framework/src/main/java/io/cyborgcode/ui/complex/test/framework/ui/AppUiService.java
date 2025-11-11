package io.cyborgcode.ui.complex.test.framework.ui;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.service.fluent.*;
import io.cyborgcode.roa.ui.service.tables.TableServiceFluent;

/**
 * Application-specific UI service facade for the Bakery Flow test suite.
 * <p>
 * This class extends {@link UiServiceFluent} to provide a type-safe, fluent API
 * for interacting with UI elements via Selenium. It acts as the "UI ring" in the
 * ROA framework and is accessed via {@code quest.use(RING_OF_UI)}.
 * </p>
 * <p>
 * The service exposes shorthand methods for all UI component types:
 * <ul>
 *   <li>{@link #input()} — text input operations</li>
 *   <li>{@link #button()} — button operations</li>
 *   <li>{@link #select()} — dropdown/combo-box interactions</li>
 *   <li>{@link #checkbox()}, {@link #radio()} — selection controls</li>
 *   <li>{@link #table()}, {@link #list()} — complex data display components</li>
 *   <li>{@link #browser()} — navigation and page-level operations</li>
 *   <li>{@link #interceptor()} — network request/response interception</li>
 *   <li>{@link #insertion()} — automatic form filling from domain objects</li>
 *   <li>{@link #validate()} — custom validation logic</li>
 * </ul>
 * </p>
 * <p>
 * This service maintains the fluent chain pattern, allowing tests to compose
 * complex UI interaction sequences in a readable, declarative style.
 * </p>
 */
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
