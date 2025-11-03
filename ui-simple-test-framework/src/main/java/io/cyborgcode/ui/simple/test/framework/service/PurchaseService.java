package io.cyborgcode.ui.simple.test.framework.service;

import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.ui.simple.test.framework.ui.elements.AlertFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ListFields;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;

import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

@Ring("PurchaseService")
public class PurchaseService extends FluentService {

   public PurchaseService purchaseCurrency(PurchaseForeignCurrency purchaseForeignCurrency) {
      quest
            .use(RING_OF_UI)
            .link().click(LinkFields.TRANSFER_FUNDS_LINK)
            .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
            .list().select(ListFields.PAY_BILLS_TABS, "Purchase Foreign Currency")
            .insertion().insertData(purchaseForeignCurrency)
            .button().click(ButtonFields.CALCULATE_COST_BUTTON)
            .button().click(ButtonFields.PURCHASE_BUTTON)
            .complete();
      return this;
   }

   public PurchaseService validatePurchase() {
      quest
            .use(RING_OF_UI)
            .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
            .complete();
      return this;
   }

}
