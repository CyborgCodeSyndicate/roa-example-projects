package io.cyborgcode.ui.simple.test.framework.base;

import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.ui.simple.test.framework.service.PurchaseService;
import io.cyborgcode.ui.simple.test.framework.ui.AppUiService;
import lombok.experimental.UtilityClass;

/**
 * Central registry of UI service rings for the ui-simple-test-framework.
 *
 * <p>A "ring" is a concrete fluent service class activated via {@link Quest#use(Class)}.
 * Switching rings lets tests choose between generic UI primitives and higher-level, domain-specific flows.
 *
 * <ul>
 *   <li>{@link #RING_OF_UI} - Selenium-based UI interactions (inputs, buttons, selects, etc.)</li></li>
 *   <li>{@link #RING_OF_PURCHASE_CURRENCY} - Delegate to a custom higher-level service with reusable flows</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UtilityClass
public class Rings {

   public static final Class<AppUiService> RING_OF_UI = AppUiService.class;
   public static final Class<PurchaseService> RING_OF_PURCHASE_CURRENCY = PurchaseService.class;

}
