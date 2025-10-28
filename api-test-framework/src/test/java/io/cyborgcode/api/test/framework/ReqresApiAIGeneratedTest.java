package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.api.test.framework.rest.authentication.AdminAuth;
import io.cyborgcode.api.test.framework.rest.authentication.ReqResAuthentication;
import io.cyborgcode.api.test.framework.rest.dto.request.LoginUser;
import io.cyborgcode.api.test.framework.rest.dto.request.User;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.annotation.Smoke;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.creator.DataCreator.Data.LOGIN_ADMIN_USER;
import static io.cyborgcode.api.test.framework.data.creator.DataCreator.Data.USER_JUNIOR;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATED_USER_ID;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATED_USER_TIMESTAMP;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.DATA;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.PAGE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.ROOT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SINGLE_USER_EMAIL;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SINGLE_USER_FIRST_NAME;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_AVATAR_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_EMAIL_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_ID;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.GET_USER;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.EMAIL_FOUND_IN_LIST_UNEXPECTED;
import static io.cyborgcode.api.test.framework.utils.Helpers.EMPTY_JSON;
import static io.cyborgcode.api.test.framework.utils.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.utils.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Login.TOKEN_REGEX;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_ONE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_JUNIOR_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_JUNIOR_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_SENIOR_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.INVALID_USER_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_EMAIL_DOMAIN;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_FIRST_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_TIMESTAMP_REGEX;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.ENDS_WITH;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.MATCHES_REGEX;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_EMPTY;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_NULL;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
class ReqresApiAIGeneratedTest extends BaseQuest {

   @Test
   @Regression
   void testGetAllUsersPage1AndPage2(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_ONE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_PREFIX).build(),
                  Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                  Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build()
            )
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(PAGE.getJsonPath()).type(IS).expected(PAGE_TWO).build(),
                  Assertion.builder().target(BODY).key(USER_EMAIL_BY_INDEX.getJsonPath(5)).type(CONTAINS).expected(USER_EMAIL_DOMAIN).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testGetUserByIdValidAndInvalid(Quest quest) {
      quest.use(RING_OF_API)
            .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, USER_NINE_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_FIRST_NAME.getJsonPath()).type(IS).expected(USER_NINE_FIRST_NAME).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL.getJsonPath()).type(CONTAINS).expected(USER_EMAIL_DOMAIN).build()
            )
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testCreateUserWithValidPayload(Quest quest, @Craft(model = DataCreator.Data.USER_LEADER) User user) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  user,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(user.getName()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(user.getJob()).build(),
                  Assertion.builder().target(BODY).key(CREATED_USER_ID.getJsonPath()).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(CREATED_USER_TIMESTAMP.getJsonPath()).type(MATCHES_REGEX).expected(USER_TIMESTAMP_REGEX).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testLoginUserWithValidCredentials(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  loginUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(MATCHES_REGEX).expected(TOKEN_REGEX).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testGetNonExistentUserReturns404AndEmptyBody(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                  Assertion.builder().target(BODY).key(ROOT.getJsonPath()).type(IS).expected(EMPTY_JSON).build()
            )
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   void testCreateAndDeleteLeaderUser(Quest quest, @Craft(model = DataCreator.Data.USER_LEADER) User userLeader) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .request(
                  DELETE_USER.withPathParam(
                        ID_PARAM,
                        retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                              .getBody()
                              .jsonPath()
                              .getString(CREATED_USER_ID.getJsonPath())
                  )
            )
            .complete();
   }

   @Test
   @Smoke
   @Regression
   @AuthenticateViaApi(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
   void testFullUserLifecycleAndCrossValidation(
         Quest quest,
         @Craft(model = DataCreator.Data.USER_LEADER) User userLeader,
         @Craft(model = DataCreator.Data.USER_JUNIOR) Late<User> userJunior
   ) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  userJunior.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).soft(true).build()
            )

            .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))

            .validate(() -> {
               List<String> emails = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                     .getBody()
                     .jsonPath()
                     .getList(USER_EMAIL_BY_INDEX.getJsonPath().replace("[%d]", ""), String.class);

               assertTrue(
                     emails.stream().noneMatch(email -> email.contains(USER_SENIOR_NAME.toLowerCase())),
                     EMAIL_FOUND_IN_LIST_UNEXPECTED
               );
            })

            .request(
                  DELETE_USER.withPathParam(
                        ID_PARAM,
                        retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                              .getBody().jsonPath().getString(CREATED_USER_ID.getJsonPath())
                  )
            )
            .complete();
   }

}
