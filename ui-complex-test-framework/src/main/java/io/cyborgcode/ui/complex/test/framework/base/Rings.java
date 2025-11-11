package io.cyborgcode.ui.complex.test.framework.base;

import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.ui.complex.test.framework.service.CustomService;
import io.cyborgcode.ui.complex.test.framework.ui.AppUiService;
import io.cyborgcode.roa.api.service.fluent.RestServiceFluent;
import io.cyborgcode.roa.db.service.fluent.DatabaseServiceFluent;
import lombok.experimental.UtilityClass;

/**
 * Central registry for ROA "rings" (fluent API facades) available to tests in the Bakery suite.
 * <p>
 * A ring represents the concrete fluent service implementation that backs {@link Quest#use(Class)}.
 * Tests switch between rings to access different testing capabilities:
 * </p>
 * <ul>
 *   <li>{@link #RING_OF_API} — REST API operations via RestAssured</li>
 *   <li>{@link #RING_OF_DB} — Database query and validation operations</li>
 *   <li>{@link #RING_OF_UI} — Selenium-based UI interactions (inputs, buttons, selects, etc.)</li>
 *   <li>{@link #RING_OF_CUSTOM} — Delegate to a custom higher-level service with reusable flows</li>
 * </ul>
 * <p>
 * This indirection keeps test code expressive while cleanly separating concerns between low-level HTTP,
 * database interactions, UI operations and shared domain-specific actions.
 * </p>
 */
@UtilityClass
public class Rings {

   public static final Class<RestServiceFluent> RING_OF_API = RestServiceFluent.class;
   public static final Class<DatabaseServiceFluent> RING_OF_DB = DatabaseServiceFluent.class;
   public static final Class<AppUiService> RING_OF_UI = AppUiService.class;
   public static final Class<CustomService> RING_OF_CUSTOM = CustomService.class;

}
