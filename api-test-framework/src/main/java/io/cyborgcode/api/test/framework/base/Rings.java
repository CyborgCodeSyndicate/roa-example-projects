package io.cyborgcode.api.test.framework.base;

import io.cyborgcode.api.test.framework.service.CustomService;
import io.cyborgcode.api.test.framework.service.EvolutionService;
import io.cyborgcode.roa.api.service.fluent.RestServiceFluent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Rings {

   public static final Class<RestServiceFluent> RING_OF_API = RestServiceFluent.class;
   public static final Class<CustomService> RING_OF_CUSTOM = CustomService.class;
   public static final Class<EvolutionService> RING_OF_EVOLUTION = EvolutionService.class;

}
