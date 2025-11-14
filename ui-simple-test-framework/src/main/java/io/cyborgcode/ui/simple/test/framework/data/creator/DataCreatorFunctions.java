package io.cyborgcode.ui.simple.test.framework.data.creator;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.ui.components.table.model.TableCell;
import io.cyborgcode.ui.simple.test.framework.ui.model.PurchaseForeignCurrency;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.OutFlow;

/**
 * Factory methods backing {@link DataCreator} entries.
 *
 * <p> Provides centralized, reusable builders for test data objects used across examples.
 * Some factories are context-aware: they can read from the active {@link SuperQuest}
 * storage or trigger prerequisite calls (e.g. fetching users) to derive dynamic input.
 *
 * <p> This keeps test classes focused on behavior while delegating all data construction
 * and lookup logic to a single, maintainable location.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class DataCreatorFunctions {

   private DataCreatorFunctions() {
   }

   public static PurchaseForeignCurrency createPurchaseForeignCurrency() {
      return PurchaseForeignCurrency.builder()
            .currency("Mexico (peso)")
            .amount("100")
            .usDollar(true)
            .build();
   }

   public static OutFlow outflowDetails() {
      OutFlow outFlow = new OutFlow();
      outFlow.setDetails(new TableCell("Details"));
      return outFlow;
   }
}
