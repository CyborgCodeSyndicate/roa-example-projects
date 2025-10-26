package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.rest.authentication.AdminAuth;
import io.cyborgcode.api.test.framework.rest.authentication.ReqResAuthentication;
import io.cyborgcode.api.test.framework.rest.dto.request.LoginUser;
import io.cyborgcode.api.test.framework.rest.dto.request.User;
import io.cyborgcode.api.test.framework.rest.dto.response.CreatedUserResponse;
import io.cyborgcode.api.test.framework.rest.dto.response.DataResponse;
import io.cyborgcode.api.test.framework.rest.dto.response.GetUsersResponse;
import io.cyborgcode.api.test.framework.rest.dto.response.UserResponse;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Journey;
import io.cyborgcode.roa.framework.annotation.JourneyData;
import io.cyborgcode.roa.framework.annotation.PreQuest;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_CUSTOM;
import static io.cyborgcode.api.test.framework.data.cleaner.TestDataCleaner.Data.DELETE_ADMIN_USER;
import static io.cyborgcode.api.test.framework.data.creator.TestDataCreator.Data.LOGIN_ADMIN_USER;
import static io.cyborgcode.api.test.framework.data.creator.TestDataCreator.Data.USER_INTERMEDIATE;
import static io.cyborgcode.api.test.framework.data.creator.TestDataCreator.Data.USER_JUNIOR;
import static io.cyborgcode.api.test.framework.data.creator.TestDataCreator.Data.USER_LEADER;
import static io.cyborgcode.api.test.framework.data.creator.TestDataCreator.Data.USER_SENIOR;
import static io.cyborgcode.api.test.framework.preconditions.QuestPreconditions.Data.CREATE_NEW_USER;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.DATA;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.PER_PAGE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SINGLE_USER_EMAIL_EXPLICIT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_URL;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_URL_EXPLICIT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.TOTAL;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.TOTAL_PAGES;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_AVATAR_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_FIRST_NAME;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.USER_ID;
import static io.cyborgcode.api.test.framework.rest.ReqresEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.rest.ReqresEndpoints.GET_USER;
import static io.cyborgcode.api.test.framework.rest.ReqresEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.rest.ReqresEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.CREATED_USER_JOB_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.CREATED_USER_NAME_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.FIRST_NAME_LENGTH_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.USER_DATA_SIZE_INCORRECT;
import static io.cyborgcode.api.test.framework.utils.AssertionMessages.userWithFirstNameNotFound;
import static io.cyborgcode.api.test.framework.utils.Headers.EXAMPLE_HEADER;
import static io.cyborgcode.api.test.framework.utils.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.utils.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static io.cyborgcode.api.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_CONTAINS_ANY_USER;
import static io.cyborgcode.api.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_DATA_SIZE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_EXPECTED_USERS;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.TOTAL_USERS_IN_PAGE_RANGE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_SENIOR_JOB;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Roles.USER_SENIOR_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_URL_REGEX;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_URL_REQRES_FRAGMENT;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_URL_VALUE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.ID_THREE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.INVALID_USER_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_EMAIL;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_FIRST_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_NINE_LAST_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_ONE_FIRST_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_SEVENTH_FIRST_NAME_LENGTH;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Users.USER_THREE_EMAIL;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.ALL_NOT_NULL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.BETWEEN;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS_ALL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS_ANY;
import static io.cyborgcode.roa.validator.core.AssertionTypes.ENDS_WITH;
import static io.cyborgcode.roa.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;
import static io.cyborgcode.roa.validator.core.AssertionTypes.GREATER_THAN;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.LENGTH;
import static io.cyborgcode.roa.validator.core.AssertionTypes.LESS_THAN;
import static io.cyborgcode.roa.validator.core.AssertionTypes.MATCHES_REGEX;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_EMPTY;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_NULL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.STARTS_WITH;
import static io.restassured.http.ContentType.JSON;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class ReqresApiTest extends BaseQuest {

   @Test
   @Regression
   public void testGetAllUsers(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(NOT).expected(1).build(),
                  Assertion.builder().target(BODY).key(TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
                  Assertion.builder().target(BODY).key(PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(CONTAINS).expected(SUPPORT_URL_REQRES_FRAGMENT).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected(SUPPORT_TEXT_PREFIX).build(),
                  Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                  Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(7).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(LENGTH).expected(6).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected(SUPPORT_URL_REGEX).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected(USER_ONE_FIRST_NAME).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(BETWEEN).expected(TOTAL_USERS_IN_PAGE_RANGE).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ALL).expected(PAGE_TWO_EXPECTED_USERS).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ANY).expected(PAGE_TWO_CONTAINS_ANY_USER).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testGetUser(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).soft(true).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL_EXPLICIT.getJsonPath()).type(IS).expected(USER_THREE_EMAIL).soft(true).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL_EXPLICIT.getJsonPath()).type(IS).expected(SUPPORT_URL_VALUE).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testUserNotFound(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testGetUsersJUnitAssertions(Quest quest) {
      quest.use(RING_OF_API)
            .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
            .validate(() -> {
               GetUsersResponse usersResponse =
                     retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class);
               assertEquals(PAGE_TWO_DATA_SIZE, usersResponse.getData().size(), USER_DATA_SIZE_INCORRECT);
               assertEquals(USER_SEVENTH_FIRST_NAME_LENGTH, usersResponse.getData().get(0).getFirstName().length(), FIRST_NAME_LENGTH_INCORRECT);
            });
   }

   @Test
   @Regression
   public void testGetUserFromListOfUsers(Quest quest) {
      quest.use(RING_OF_API)
            .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class).getData().get(0).getId()),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testGetUserFromListOfUsersByName(Quest quest) {
      quest.use(RING_OF_API)
            .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
            .request(GET_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                  .getBody()
                  .as(GetUsersResponse.class)
                  .getData()
                  .stream()
                  .filter(user -> USER_NINE_FIRST_NAME.equals(user.getFirstName()))
                  .map(DataResponse::getId)
                  .findFirst()
                  .orElseThrow(() -> new RuntimeException(userWithFirstNameNotFound(USER_NINE_FIRST_NAME))))
            )
            .validate(softAssertions -> {
               UserResponse userResponse =
                     retrieve(StorageKeysApi.API, GET_USER, Response.class).getBody().as(UserResponse.class);
               softAssertions.assertThat(userResponse.getData().getId()).isEqualTo(USER_NINE_ID);
               softAssertions.assertThat(userResponse.getData().getEmail()).isEqualTo(USER_NINE_EMAIL);
               softAssertions.assertThat(userResponse.getData().getFirstName()).isEqualTo(USER_NINE_FIRST_NAME);
               softAssertions.assertThat(userResponse.getData().getLastName()).isEqualTo(USER_NINE_LAST_NAME);
            })
            .complete();
   }

   @Test
   @Regression
   public void testCreateUser(Quest quest, @Craft(model = USER_LEADER) User user) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  user,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateJuniorUser(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
      quest.use(RING_OF_API)
            .requestAndValidate(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .requestAndValidate(POST_CREATE_USER,
                  user.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateTwoUsers(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
      quest.use(RING_OF_API)
            .requestAndValidate(POST_CREATE_USER, userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(POST_CREATE_USER, userSenior.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testLoginUserAndAddHeader(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
      quest.use(RING_OF_API)
            .request(POST_LOGIN_USER, loginUser)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, ID_THREE)
                        .withHeader(EXAMPLE_HEADER, retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
                              .getBody().jsonPath().getString(TOKEN.getJsonPath())),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApi(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @PreQuest({
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
   })
   @Ripper(targets = {DELETE_ADMIN_USER})
   @Regression
   public void testUserLifecycle(Quest quest) {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt().contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)));
            })
            .complete();
   }

   @Test
   @Regression
   public void testCustomService(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
      quest.use(RING_OF_CUSTOM)
            .loginUserAndAddSpecificHeader(loginUser)
            .drop()
            .use(RING_OF_API)
            .requestAndValidate(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testValidateAllUsers(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
      quest.use(RING_OF_CUSTOM)
            .loginUserAndAddSpecificHeader(loginUser)
            .requestAndValidateGetAllUsers()
            .complete();
   }

}
