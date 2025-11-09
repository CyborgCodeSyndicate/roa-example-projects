package io.cyborgcode.api.test.framework.base;

import io.cyborgcode.api.test.framework.service.CustomService;
import io.cyborgcode.api.test.framework.service.EvolutionService;
import io.cyborgcode.roa.api.service.fluent.RestServiceFluent;
import io.cyborgcode.roa.framework.quest.Quest;
import lombok.experimental.UtilityClass;

/**
 * Central registry for ROA "rings" used in the example test suite.
 * <p>
 * A ring represents the concrete fluent service implementation that backs {@link Quest#use(Class)}.
 * By switching rings, tests can:
 * </p>
 * <ul>
 *   <li>Use the default REST fluent API ({@link #RING_OF_API})</li>
 *   <li>Delegate to a custom higher-level service with reusable flows ({@link #RING_OF_CUSTOM})</li>
 *   <li>Demonstrate evolutionary patterns and advanced composition ({@link #RING_OF_EVOLUTION})</li>
 * </ul>
 * This indirection keeps test code expressive while cleanly separating concerns between low-level HTTP,
 * shared domain-specific actions, and tutorial/evolution scenarios.
 */
@UtilityClass
public class Rings {

   public static final Class<RestServiceFluent> RING_OF_API = RestServiceFluent.class;
   public static final Class<CustomService> RING_OF_CUSTOM = CustomService.class;
   public static final Class<EvolutionService> RING_OF_EVOLUTION = EvolutionService.class;

}
