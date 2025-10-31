package io.cyborgcode.ui.complex.test.framework.base;

import io.cyborgcode.ui.complex.test.framework.service.CustomService;
import io.cyborgcode.ui.complex.test.framework.ui.UiServiceCustom;
import io.cyborgcode.roa.api.service.fluent.RestServiceFluent;
import io.cyborgcode.roa.db.service.fluent.DatabaseServiceFluent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Rings {

   public static final Class<RestServiceFluent> RING_OF_API = RestServiceFluent.class;
   public static final Class<DatabaseServiceFluent> RING_OF_DB = DatabaseServiceFluent.class;
   public static final Class<UiServiceCustom> RING_OF_UI = UiServiceCustom.class;
   public static final Class<CustomService> RING_OF_CUSTOM = CustomService.class;

}
