package io.cyborgcode.ui.complex.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;

import java.util.function.Consumer;

/**
 * Defines reusable cleanup (data ripping) operations for the example test suite.
 *
 * <p>This enum integrates with ROA {@code @Ripper} mechanism via {@link DataRipper}: each constant
 * maps to a function that is executed after the test completes, allowing you to centralize and
 * reuse teardown logic.
 *
 * <ul>
 *   <li>{@link #DELETE_CREATED_ORDERS} — removes all orders created during the test execution.
 * </ul>
 *
 * The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DataCleaner implements DataRipper<DataCleaner> {

   DELETE_CREATED_ORDERS(DataCleanerFunctions::cleanAllOrders);

   public static final class Data {

      public static final String DELETE_CREATED_ORDERS = "DELETE_CREATED_ORDERS";

      private Data() {
      }

   }

   private final Consumer<SuperQuest> cleanUpFunction;

   DataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
      this.cleanUpFunction = cleanUpFunction;
   }


   @Override
   public Consumer<SuperQuest> eliminate() {
      return cleanUpFunction;
   }

   @Override
   public DataCleaner enumImpl() {
      return this;
   }

}
