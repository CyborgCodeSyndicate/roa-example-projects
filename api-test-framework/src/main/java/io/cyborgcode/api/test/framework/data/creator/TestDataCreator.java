package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

public enum TestDataCreator implements DataForge {

   USER_LEADER_FLOW(DataCreationFunctions::createLeaderUser),
   LOGIN_ADMIN_USER_FLOW(DataCreationFunctions::createAdminLoginUser),
   USER_JUNIOR_FLOW(DataCreationFunctions::createJuniorUser),
   USER_SENIOR_FLOW(DataCreationFunctions::createSeniorUser),
   USER_INTERMEDIATE_FLOW(DataCreationFunctions::createIntermediateUser);

   public static final String USER_LEADER = "USER_LEADER_FLOW";
   public static final String LOGIN_ADMIN_USER = "LOGIN_ADMIN_USER_FLOW";
   public static final String USER_JUNIOR = "USER_JUNIOR_FLOW";
   public static final String USER_SENIOR = "USER_SENIOR_FLOW";
   public static final String USER_INTERMEDIATE = "USER_INTERMEDIATE_FLOW";

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
