package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.ui.complex.test.framework.base.Rings;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminUi;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogging;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.ui.complex.test.framework.ui.elements.CheckboxFields.PAST_ORDERS_CHECKBOX;

@UI
public class BakerySessionReUsageTest extends BaseQuest {


   @Test
   @AuthenticateViaUi(credentials = AdminUi.class, type = AppUiLogging.class, cacheCredentials = true)
   public void scenario_four(Quest quest) throws InterruptedException {
      quest
            .use(Rings.RING_OF_UI)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
            .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
            .checkbox().select(PAST_ORDERS_CHECKBOX)
            .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
            .complete();

   }

   @Test
   @AuthenticateViaUi(credentials = AdminUi.class, type = AppUiLogging.class, cacheCredentials = true)
   public void scenario_five(Quest quest) throws InterruptedException {
      quest
            .use(Rings.RING_OF_UI)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
            .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
            .checkbox().select(PAST_ORDERS_CHECKBOX)
            .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
            .complete();

   }

}
