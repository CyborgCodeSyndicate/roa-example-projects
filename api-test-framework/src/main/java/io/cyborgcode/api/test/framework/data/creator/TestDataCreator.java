package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

public enum TestDataCreator implements DataForge {

   USER_LEADER(DataCreationFunctions::createLeaderUser),
   LOGIN_ADMIN_USER(DataCreationFunctions::createAdminLoginUser),
   USER_JUNIOR(DataCreationFunctions::createJuniorUser),
   USER_SENIOR(DataCreationFunctions::createSeniorUser),
   USER_INTERMEDIATE(DataCreationFunctions::createIntermediateUser);

   public static final class Data {

      public static final String USER_LEADER = "USER_LEADER";
      public static final String LOGIN_ADMIN_USER = "LOGIN_ADMIN_USER";
      public static final String USER_JUNIOR = "USER_JUNIOR";
      public static final String USER_SENIOR = "USER_SENIOR";
      public static final String USER_INTERMEDIATE = "USER_INTERMEDIATE";

      private Data() {
      }

   }

   private final Late<Object> createDataFunction;

   TestDataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }

   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }

   @Override
   public Enum<?> enumImpl() {
      return this;
   }

}
