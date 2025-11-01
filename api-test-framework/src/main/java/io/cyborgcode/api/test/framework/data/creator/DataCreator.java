package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;

public enum DataCreator implements DataForge<DataCreator> {

   USER_LEADER_REQUEST(DataCreatorFunctions::createLeaderUserRequest),
   LOGIN_ADMIN_USER_REQUEST(DataCreatorFunctions::createLoginAdminUserRequest),
   USER_JUNIOR_REQUEST(DataCreatorFunctions::createJuniorUserRequest),
   USER_SENIOR_REQUEST(DataCreatorFunctions::createSeniorUserRequest),
   USER_INTERMEDIATE_REQUEST(DataCreatorFunctions::createIntermediateUserRequest);

   public static final class Data {

      private Data() {
      }

      public static final String USER_LEADER_REQUEST = "USER_LEADER_REQUEST";
      public static final String LOGIN_ADMIN_USER_REQUEST = "LOGIN_ADMIN_USER_REQUEST";
      public static final String USER_JUNIOR_REQUEST = "USER_JUNIOR_REQUEST";
      public static final String USER_SENIOR_REQUEST = "USER_SENIOR_REQUEST";
      public static final String USER_INTERMEDIATE_REQUEST = "USER_INTERMEDIATE_REQUEST";

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
