package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.api.test.framework.preconditions.Preconditions;
import io.cyborgcode.api.test.framework.rest.authentication.AdminAuth;
import io.cyborgcode.api.test.framework.rest.authentication.ReqResAuthentication;
import io.cyborgcode.api.test.framework.rest.dto.request.User;
import io.cyborgcode.api.test.framework.rest.dto.response.CreatedUserResponse;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.PreQuest;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.annotation.Smoke;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_EVOLUTION;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SINGLE_USER_AVATAR;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SINGLE_USER_FIRST_NAME;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_URL;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.GET_USER;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.CREATED_AT_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.CREATED_USER_JOB_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.CREATED_USER_NAME_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_CADDY_FULL;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_URL_VALUE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.ID_THREE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_THREE_AVATAR;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_THREE_FIRST_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_TIMESTAMP_REGEX;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.ENDS_WITH;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
class RoAGeneratedTest extends BaseQuest {

   @Test
   @Regression
   void shouldReturnUserThreeWithCorrectFieldsAndSupportInfo(final Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_FIRST_NAME.getJsonPath()).type(IS).expected(USER_THREE_FIRST_NAME).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_AVATAR.getJsonPath()).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_AVATAR.getJsonPath()).type(IS).expected(USER_THREE_AVATAR).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(IS).expected(SUPPORT_URL_VALUE).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(IS).expected(SUPPORT_TEXT_CADDY_FULL).build()
            )
            .complete();
   }

   @Test
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @PreQuest({
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_LEADER)}, order = 1),
         @Journey(value = Preconditions.Data.CREATE_NEW_USER, journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)}, order = 2)
   })
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   void shouldCreateIntermediateUserWithCorrectDetails(final Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdIntermediateUser =
                     retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                           .getBody()
                           .as(CreatedUserResponse.class);

               assertEquals(USER_INTERMEDIATE_NAME, createdIntermediateUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdIntermediateUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdIntermediateUser.getCreatedAt().matches(USER_TIMESTAMP_REGEX), CREATED_AT_INCORRECT);
            })
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   void shouldCreateAndDeleteLeaderUserSuccessfully(
         final Quest quest,
         final @Craft(model = DataCreator.Data.USER_LEADER) Late<User> userLeader) {

      quest.use(RING_OF_EVOLUTION)
            .createLeaderUserAndValidateResponse(userLeader.create())
            .drop()
            .use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdLeader = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody()
                     .as(CreatedUserResponse.class);

               assertEquals(USER_LEADER_NAME, createdLeader.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_LEADER_JOB, createdLeader.getJob(), CREATED_USER_JOB_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                        .getBody()
                        .as(CreatedUserResponse.class)
                        .getId()),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

}
