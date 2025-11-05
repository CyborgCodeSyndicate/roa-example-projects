package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

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
