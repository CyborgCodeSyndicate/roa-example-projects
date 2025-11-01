package io.cyborgcode.api.test.framework.data.creator;


import io.cyborgcode.api.test.framework.api.dto.request.LoginUser;
import io.cyborgcode.api.test.framework.api.dto.request.User;
import io.cyborgcode.api.test.framework.api.dto.response.DataResponse;
import io.cyborgcode.api.test.framework.api.dto.response.GetUsersResponse;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.restassured.response.Response;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.creator.DataCreator.USER_LEADER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;

public final class DataCreatorFunctions {

   private DataCreatorFunctions() {
   }

   public static User createLeaderUser() {
      return User.builder()
            .name("Morpheus")
            .job("Leader")
            .build();
   }

   public static LoginUser createAdminLoginUser() {
      return LoginUser.builder()
            .email("eve.holt@reqres.in")
            .password("cityslicka")
            .build();
   }

   public static User createJuniorUser() {
      SuperQuest quest = QuestHolder.get();
      DataResponse dataResponse;

      try {
         dataResponse = extractFirstUserFromGetAllUsers(quest);
      } catch (Exception ex) {
         quest.use(RING_OF_API)
               .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO));
         dataResponse = extractFirstUserFromGetAllUsers(quest);
      }

      return User.builder()
            .name(dataResponse.getFirstName() + " suffix")
            .job("Junior " + dataResponse.getLastName() + " worker")
            .build();
   }

   public static User createSeniorUser() {
      SuperQuest quest = QuestHolder.get();

      User userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.ARGUMENTS)
               .get(USER_LEADER, User.class);
      } catch (Exception ex) {
         userLeader = createLeaderUser();
      }

      return User.builder()
            .name("Mr. " + userLeader.getName())
            .job("Senior " + userLeader.getJob())
            .build();
   }

   public static User createIntermediateUser() {
      SuperQuest quest = QuestHolder.get();

      User userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.PRE_ARGUMENTS)
               .get(USER_LEADER, User.class);
      } catch (Exception ex) {
         userLeader = createLeaderUser();
      }

      return User.builder()
            .name("Mr. " + userLeader.getName())
            .job("Intermediate " + userLeader.getJob())
            .build();
   }

   private static DataResponse extractFirstUserFromGetAllUsers(SuperQuest quest) {
      return quest.getStorage()
            .sub(StorageKeysApi.API)
            .get(GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersResponse.class)
            .getData()
            .get(0);
   }

}
