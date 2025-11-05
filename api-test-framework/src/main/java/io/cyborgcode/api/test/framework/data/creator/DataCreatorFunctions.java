package io.cyborgcode.api.test.framework.data.creator;


import io.cyborgcode.api.test.framework.api.dto.request.LoginRequest;
import io.cyborgcode.api.test.framework.api.dto.request.CreateUserRequest;
import io.cyborgcode.api.test.framework.api.dto.response.UserData;
import io.cyborgcode.api.test.framework.api.dto.response.GetUsersResponse;
import io.cyborgcode.api.test.framework.data.constants.TestConstants;
import io.cyborgcode.api.test.framework.data.retriever.DataProperties;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigCache;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.creator.DataCreator.USER_LEADER;

public final class DataCreatorFunctions {

   private static final DataProperties DATA_PROPERTIES = ConfigCache.getOrCreate(DataProperties.class);

   private DataCreatorFunctions() {
   }

   public static CreateUserRequest createLeaderUserRequest() {
      return CreateUserRequest.builder()
            .name(TestConstants.Roles.USER_LEADER_NAME)
            .job(TestConstants.Roles.USER_LEADER_JOB)
            .build();
   }

   public static LoginRequest createLoginAdminUserRequest() {
      return LoginRequest.builder()
            .email(DATA_PROPERTIES.username())
            .password(DATA_PROPERTIES.password())
            .build();
   }

   public static CreateUserRequest createJuniorUserRequest() {
      SuperQuest quest = QuestHolder.get();
      UserData userData;

      try {
         userData = extractFirstUserFromGetAllUsers(quest);
      } catch (Exception ex) {
         quest.use(RING_OF_API)
               .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO));
         userData = extractFirstUserFromGetAllUsers(quest);
      }

      return CreateUserRequest.builder()
            .name(userData.getFirstName() + " suffix")
            .job("Junior " + userData.getLastName() + " worker")
            .build();
   }

   public static CreateUserRequest createSeniorUserRequest() {
      SuperQuest quest = QuestHolder.get();

      CreateUserRequest userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.ARGUMENTS)
               .get(USER_LEADER, CreateUserRequest.class);
      } catch (Exception ex) {
         userLeader = createLeaderUserRequest();
      }

      return CreateUserRequest.builder()
            .name("Mr. " + userLeader.getName())
            .job("Senior " + userLeader.getJob())
            .build();
   }

   public static CreateUserRequest createIntermediateUserRequest() {
      SuperQuest quest = QuestHolder.get();

      CreateUserRequest userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.PRE_ARGUMENTS)
               .get(USER_LEADER, CreateUserRequest.class);
      } catch (Exception ex) {
         userLeader = createLeaderUserRequest();
      }

      return CreateUserRequest.builder()
            .name("Mr. " + userLeader.getName())
            .job("Intermediate " + userLeader.getJob())
            .build();
   }

   private static UserData extractFirstUserFromGetAllUsers(SuperQuest quest) {
      return quest.getStorage()
            .sub(StorageKeysApi.API)
            .get(GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersResponse.class)
            .getData()
            .get(0);
   }

}
