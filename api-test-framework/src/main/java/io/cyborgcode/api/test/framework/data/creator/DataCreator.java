package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

/**
 * Central registry of reusable test data factories.
 * <p>
 * Each enum constant represents a named data model that can be referenced from
 * RoA annotations such as {@code @Craft} or framework internals. The associated
 * {@link Late} supplier is implemented in {@link DataCreatorFunctions} and is
 * responsible for constructing request/response objects on demand.
 * <p>
 * This indirection:
 * <ul>
 *    <li>keeps test classes free from hard-coded test data,</li>
 *    <li>allows data to be generated lazily and context-aware,</li>
 *    <li>provides a stable, string-based contract via {@link Data} for annotations.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DataCreator implements DataForge<DataCreator> {

   USER_LEADER(DataCreatorFunctions::leaderUser),
   LOGIN_ADMIN_USER(DataCreatorFunctions::loginAdminUser),
   USER_JUNIOR(DataCreatorFunctions::juniorUser),
   USER_SENIOR(DataCreatorFunctions::seniorUser),
   USER_INTERMEDIATE(DataCreatorFunctions::intermediateUser);

   public static final class Data {

      private Data() {
      }

      public static final String USER_LEADER = "USER_LEADER";
      public static final String LOGIN_ADMIN_USER = "LOGIN_ADMIN_USER";
      public static final String USER_JUNIOR = "USER_JUNIOR";
      public static final String USER_SENIOR = "USER_SENIOR";
      public static final String USER_INTERMEDIATE = "USER_INTERMEDIATE";

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
