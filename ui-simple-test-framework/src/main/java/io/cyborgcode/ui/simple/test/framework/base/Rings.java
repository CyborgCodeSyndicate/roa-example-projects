package io.cyborgcode.ui.simple.test.framework.base;

import io.cyborgcode.ui.simple.test.framework.service.PurchaseService;
import io.cyborgcode.ui.simple.test.framework.ui.UiServiceCustom;
import lombok.experimental.UtilityClass;


@UtilityClass
public class Rings {

   public static final Class<UiServiceCustom> RING_OF_UI = UiServiceCustom.class;
   public static final Class<PurchaseService> RING_OF_PURCHASE_CURRENCY = PurchaseService.class;

}
