package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.authentication.AdminAuth;
import io.cyborgcode.api.test.framework.api.authentication.AppAuth;
import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.api.test.framework.api.dto.response.CreatedUserDto;
import io.cyborgcode.api.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.api.test.framework.data.test_data.Data;
import io.cyborgcode.api.test.framework.preconditions.Preconditions;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOKEN;
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

/**
 * Demonstrates the evolution of a full user lifecycle test:
 * - Manual login and token handling;
 * - API-level authentication via {@link AuthenticateViaApi};
 * - Pre-conditions with {@link Journey}, and {@link JourneyData};
 * - Cleanup with {@link Ripper};
 * - Encapsulated flows via {@code RING_OF_EVOLUTION}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@API
class UserLifecycleEvolutionTest extends BaseQuest {

   @Test
   @Regression
   @Description("Executes the user lifecycle manually using explicit login, token handling, create, validate, and delete.")
   void executesUserLifecycleManually(Quest quest) {
      final String username = Data.testData().username();
      final String password = Data.testData().password();

      quest.use(RING_OF_API)
            .request(POST_LOGIN_USER, new LoginDto(username, password));

      String token = retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
            .getBody().jsonPath().getString(TOKEN.getJsonPath());

      CreateUserDto leaderUser =
            CreateUserDto.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      CreateUserDto intermediateUser =
            CreateUserDto.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  leaderUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  intermediateUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserDto createdUser = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserDto.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUser.getCreatedAt()
                           .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)),
                     CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE)
                        .withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @Description("Executes the user lifecycle using @AuthenticateViaApi instead of manual token handling.")
   void executesUserLifecycleWithApiAuthentication(Quest quest) {
      CreateUserDto leaderUser =
            CreateUserDto.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      CreateUserDto intermediateUser =
            CreateUserDto.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  leaderUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  intermediateUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserDto createdUser = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserDto.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUser.getCreatedAt()
                           .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)),
                     CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)},
         order = 2
   )
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_LEADER)},
         order = 1
   )
   @Description("Executes the user lifecycle using @Journey to prepare users before the test body runs.")
   void executesUserLifecycleWithJourney(Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserDto createdUser = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserDto.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUser.getCreatedAt()
                           .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)),
                     CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)},
         order = 2
   )
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_LEADER)},
         order = 1
   )
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   @Description("Executes the user lifecycle with @Journey data setup and @Ripper cleanup after the test.")
   void executesUserLifecycleWithJourneyAndRipper(Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserDto createdUser = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserDto.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUser.getCreatedAt()
                           .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)),
                     CREATED_AT_INCORRECT);
            })
            .complete();
   }

   @Test
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)},
         order = 2
   )
   @Journey(
         value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_LEADER)},
         order = 1
   )
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   @Description("Executes the user lifecycle using a custom evolution ring that encapsulates validation logic.")
   void executesUserLifecycleUsingEvolutionRing(Quest quest) {
      quest.use(RING_OF_EVOLUTION)
            .validateCreatedUser()
            .complete();
   }

}
