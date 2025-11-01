package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.authentication.AdminAuth;
import io.cyborgcode.api.test.framework.api.authentication.AppAuth;
import io.cyborgcode.api.test.framework.api.dto.request.CreateUserRequest;
import io.cyborgcode.api.test.framework.api.dto.request.LoginUserRequest;
import io.cyborgcode.api.test.framework.api.dto.response.CreatedUserResponse;
import io.cyborgcode.api.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.api.test.framework.data.retriever.DataProperties;
import io.cyborgcode.api.test.framework.preconditions.Preconditions;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.PreQuest;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import java.time.Instant;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_EVOLUTION;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_AT_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_JOB_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_NAME_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_KEY;
import static io.cyborgcode.api.test.framework.data.constants.Headers.AUTHORIZATION_HEADER_VALUE;
import static io.cyborgcode.api.test.framework.data.constants.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.ID_THREE;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
class UserLifecycleEvolutionTest extends BaseQuest {

   @Test
   @Regression
   void testUserLifecycleBasic(Quest quest) {
      final DataProperties dataProperties = ConfigCache.getOrCreate(DataProperties.class);
      final String username = dataProperties.username();
      final String password = dataProperties.password();

      quest.use(RING_OF_API)
            .request(POST_LOGIN_USER, new LoginUserRequest(username, password));

      String token = retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
            .getBody().jsonPath().getString(TOKEN.getJsonPath());

      CreateUserRequest createLeaderUserRequest =
            CreateUserRequest.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      CreateUserRequest createIntermediateUserRequest =
            CreateUserRequest.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  createLeaderUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  createIntermediateUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE).withHeader(AUTHORIZATION_HEADER_KEY,
                        AUTHORIZATION_HEADER_VALUE + token),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @Regression
   void testUserLifecycleWithAuth(Quest quest) {
      CreateUserRequest createLeaderUserRequest =
            CreateUserRequest.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      CreateUserRequest createIntermediateUserRequest =
            CreateUserRequest.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createLeaderUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  createIntermediateUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @PreQuest({
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE_REQUEST)}, order = 2),
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_LEADER_REQUEST)}, order = 1)
   })
   @Regression
   void testUserLifecycleWithPreQuest(Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @PreQuest({
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE_REQUEST)}, order = 2),
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_LEADER_REQUEST)}, order = 1)
   })
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   @Regression
   void testUserLifecycleWithRipper(Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .complete();
   }

   @Test
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @PreQuest({
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE_REQUEST)}, order = 2),
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_LEADER_REQUEST)}, order = 1)
   })
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   @Regression
   void testUserLifecycleWithCustomService(Quest quest) {
      quest.use(RING_OF_EVOLUTION)
            .validateCreatedUser()
            .complete();
   }

}
