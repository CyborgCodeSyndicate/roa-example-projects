package io.cyborgcode.ui.simple.test.framework.ui;


import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.playwright.service.fluent.AlertServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.ButtonServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.InputServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.InsertionServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.LinkServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.ListServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.RadioServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.SelectServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.fluent.UiServiceFluent;
import io.cyborgcode.roa.ui.playwright.service.tables.TableServiceFluent;
import io.cyborgcode.roa.ui.playwright.session.UiSession;

/**
 * Application-specific UI service facade for the demo test application.
 *
 * <p>This class extends {@link UiServiceFluent} to provide a type-safe, fluent API for 
 * interacting with UI elements via Selenium. It acts as the "UI ring" in the ROA framework
 * and is accessed via {@code quest.use(Rings.RING_OF_UI)}.
 *
 * <p>The service exposes shorthand methods for all UI component types:
 * <ul>
 *   <li>{@link #input()} — text input operations</li>
 *   <li>{@link #button()} — button operations</li>
 *   <li>{@link #select()} — dropdown/combo-box interactions</li>
 *   <li>{@link #radio()} — selection controls</li>
 *   <li>{@link #table()} and {@link #list()} — complex data display components</li>
 *   <li>{@link #link()} — hyperlink interactions</li>
 *   <li>{@link #alert()} — alert/message component interactions</li>
 *   <li>{@link #insertion()} — automatic form filling from domain objects</li>
 * </ul>
 *
 * <p>This service maintains the fluent chain pattern, allowing tests to compose
 * complex UI interaction sequences in a readable, declarative style.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class AppUiService extends UiServiceFluent<AppUiService> {

   public AppUiService(UiSession session, SuperQuest quest) {
      super(session);
      this.quest = quest;
      postQuestSetupInitialization();
   }

   public InputServiceFluent<AppUiService> input() {
      return (InputServiceFluent<AppUiService>) getInputField();
   }

   public TableServiceFluent<AppUiService> table() {
      return (TableServiceFluent<AppUiService>) getTable();
   }

   public InsertionServiceFluent<AppUiService> insertion() {
      return (InsertionServiceFluent<AppUiService>) getInsertionService();
   }

   public ButtonServiceFluent<AppUiService> button() {
      return (ButtonServiceFluent<AppUiService>) getButtonField();
   }

   public RadioServiceFluent<AppUiService> radio() {
      return (RadioServiceFluent<AppUiService>) getRadioField();
   }

   public SelectServiceFluent<AppUiService> select() {
      return (SelectServiceFluent<AppUiService>) getSelectField();
   }

   public ListServiceFluent<AppUiService> list() {
      return (ListServiceFluent<AppUiService>) getListField();
   }

   public LinkServiceFluent<AppUiService> link() {
      return (LinkServiceFluent<AppUiService>) getLinkField();
   }

   public AlertServiceFluent<AppUiService> alert() {
      return (AlertServiceFluent<AppUiService>) getAlertField();
   }

//   public NavigationServiceFluent<AppUiService> browser() {
//      return getNavigation();
//   }
//
//   public ValidationServiceFluent<AppUiService> validate() {
//      return getValidation();
//   }

}
