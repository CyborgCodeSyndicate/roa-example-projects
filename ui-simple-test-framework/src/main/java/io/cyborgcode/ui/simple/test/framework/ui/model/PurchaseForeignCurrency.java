package io.cyborgcode.ui.simple.test.framework.ui.model;

import io.cyborgcode.roa.ui.annotations.InsertionElement;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.RadioFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields.Data.AMOUNT_CURRENCY_FIELD;
import static io.cyborgcode.ui.simple.test.framework.ui.elements.RadioFields.Data.DOLLARS_RADIO_FIELD;
import static io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields.Data.PC_CURRENCY_DDL;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PurchaseForeignCurrency {

   @InsertionElement(locatorClass = SelectFields.class, elementEnum = PC_CURRENCY_DDL, order = 1)
   private String currency;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = AMOUNT_CURRENCY_FIELD, order = 2)
   private String amount;

   @InsertionElement(locatorClass = RadioFields.class, elementEnum = DOLLARS_RADIO_FIELD, order = 3)
   private String usDollar;
}
