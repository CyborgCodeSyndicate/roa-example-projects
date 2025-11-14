package io.cyborgcode.api.test.framework.data.creator;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.api.test.framework.api.dto.response.GetUsersDto;
import io.cyborgcode.api.test.framework.api.dto.response.UserData;
import io.cyborgcode.api.test.framework.data.constants.TestConstants;
import io.cyborgcode.api.test.framework.data.test_data.Data;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.restassured.response.Response;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.creator.DataCreator.USER_LEADER;

/**
 * Factory methods backing {@link DataCreator} entries.
 * <p>
 * Provides centralized, reusable builders for test data objects used across examples.
 * Some factories are context-aware: they can read from the active {@link SuperQuest}
 * storage or trigger prerequisite calls (e.g. fetching users) to derive dynamic input.
 * <p>
 * This keeps test classes focused on behavior while delegating all data construction
 * and lookup logic to a single, maintainable location.
 */
public final class DataCreatorFunctions {

   private DataCreatorFunctions() {
   }

   public static CreateUserDto leaderUser() {
      return CreateUserDto.builder()
            .name(TestConstants.Roles.USER_LEADER_NAME)
            .job(TestConstants.Roles.USER_LEADER_JOB)
            .build();
   }

   public static LoginDto loginAdminUser() {
      return LoginDto.builder()
            .email(Data.testData().username())
            .password(Data.testData().password())
            .build();
   }

   public static CreateUserDto juniorUser() {
      SuperQuest quest = QuestHolder.get();
      UserData firstUser = firstUserFromGetAllUsersOrFetch(quest);
      return CreateUserDto.builder()
            .name(firstUser.getFirstName() + " suffix")
            .job("Junior " + firstUser.getLastName() + " worker")
            .build();
   }

   public static CreateUserDto seniorUser() {
      SuperQuest quest = QuestHolder.get();
      CreateUserDto userLeader =
            leaderUserFromStorageOrDefault(quest, StorageKeysTest.ARGUMENTS); // encapsulated try/catch
      return CreateUserDto.builder()
            .name("Mr. " + userLeader.getName())
            .job("Senior " + userLeader.getJob())
            .build();
   }

   public static CreateUserDto intermediateUser() {
      SuperQuest quest = QuestHolder.get();
      CreateUserDto userLeader =
            leaderUserFromStorageOrDefault(quest, StorageKeysTest.PRE_ARGUMENTS); // encapsulated try/catch
      return CreateUserDto.builder()
            .name("Mr. " + userLeader.getName())
            .job("Intermediate " + userLeader.getJob())
            .build();
   }

   private static UserData firstUserFromGetAllUsersOrFetch(SuperQuest quest) {
      try {
         return extractFirstUserFromGetAllUsers(quest);
      } catch (Exception ignored) {
         quest.use(RING_OF_API)
               .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO));
         return extractFirstUserFromGetAllUsers(quest);
      }
   }

   private static CreateUserDto leaderUserFromStorageOrDefault(SuperQuest quest, StorageKeysTest storageArea) {
      try {
         return quest.getStorage()
               .sub(storageArea)
               .get(USER_LEADER, CreateUserDto.class);
      } catch (Exception ignored) {
         return leaderUser();
      }
   }

   private static UserData extractFirstUserFromGetAllUsers(SuperQuest quest) {
      return quest.getStorage()
            .sub(StorageKeysApi.API)
            .get(GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersDto.class)
            .getData()
            .get(0);
   }

}
