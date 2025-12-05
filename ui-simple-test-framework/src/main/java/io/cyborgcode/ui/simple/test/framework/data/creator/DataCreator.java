package io.cyborgcode.ui.simple.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

/**
 * Central registry of reusable test data factories.
 * <p> Each enum constant represents a named data model that can be referenced from
 * ROA annotations such as {@code @Craft} or {@code @Journey}. The associated
 * {@link Late} supplier is implemented in {@link DataCreatorFunctions} and is
 * responsible for constructing domain objects (Seller, Order) on demand.
 *
 * <p> This indirection:
 * <ul>
 *    <li>keeps test classes free from hard-coded test data,</li>
 *    <li>allows data to be generated lazily and context-aware,</li>
 *    <li>provides a stable, string-based contract via {@link Data} for annotations,</li>
 *    <li>supports both eager and late-bound data creation patterns.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DataCreator implements DataForge<DataCreator> {

   PURCHASE_CURRENCY(DataCreatorFunctions::createPurchaseForeignCurrency),
   OUTFLOW_DATA(DataCreatorFunctions::outflowDetails);

   public static final class Data {

      public static final String PURCHASE_CURRENCY = "PURCHASE_CURRENCY";
      public static final String OUTFLOW_DATA = "OUTFLOW_DATA";

      private Data() {
      }

   }

   private final Late<Object> createDataFunction;

   DataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }

   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }

   @Override
   public DataCreator enumImpl() {
      return this;
   }

}
