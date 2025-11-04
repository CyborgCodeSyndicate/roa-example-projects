package io.cyborgcode.ui.complex.test.framework;

import io.cyborgcode.ui.complex.test.framework.base.Rings;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AdminCredentials;
import io.cyborgcode.ui.complex.test.framework.ui.authentication.AppUiLogin;
import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.AuthenticateViaUi;
import io.cyborgcode.roa.ui.annotations.UI;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.ui.complex.test.framework.ui.elements.CheckboxFields.PAST_ORDERS_CHECKBOX;

@UI
class BakerySessionReUsageTest extends BaseQuest {


   @Test
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void scenario_four(Quest quest) {
      quest
            .use(Rings.RING_OF_UI)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
            .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
            .checkbox().select(PAST_ORDERS_CHECKBOX)
            .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
            .complete();

   }

   @Test
   @AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
   void scenario_five(Quest quest) {
      quest
            .use(Rings.RING_OF_UI)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
            .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
            .checkbox().select(PAST_ORDERS_CHECKBOX)
            .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
            .complete();

   }

}
