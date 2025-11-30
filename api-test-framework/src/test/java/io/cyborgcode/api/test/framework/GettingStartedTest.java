package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.api.test.framework.data.test_data.Data;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.ERROR;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.SINGLE_USER_EMAIL_EXPLICIT;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.MISSING_PASSWORD_ERROR;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.ID_THREE;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.USER_THREE_EMAIL;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_NULL;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Minimal entry point for RoA-style API testing with Reqres.
 * <p>
 * IMPORTANT:
 * - This class is annotated with {@link API}, which is required when using RING_OF_API
 * and the RoA API DSL.
 * - Demonstrates:
 * - Using {@code quest.use(RING_OF_API)}
 * - Basic GET with query/path parameters
 * - Basic POST with a request DTO
 * - Simple assertions on status, headers, and body
 * For advanced features (preconditions, authentication, ripper, custom rings, evolutions),
 * see the dedicated example classes.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@API
class GettingStartedTest extends BaseQuest {

   @Test
   @Regression
   @Description("Shows GET with a query parameter via quest.use(RING_OF_API) + requestAndValidate; minimal status/header checks.")
   void showsBasicGetWithQueryParamAndMinimalAssertions(Quest quest) {
      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Shows GET with a path parameter using requestAndValidate and validating a field via JSON path.")
   void showsGetWithPathParamAndJsonPathValidation(Quest quest) {
      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  GET_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL_EXPLICIT.getJsonPath())
                        .type(IS).expected(USER_THREE_EMAIL).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Shows POST with a DTO request body using requestAndValidate and simple body assertions.")
   void showsPostWithDtoAndSimpleBodyAssertions(Quest quest) {
      CreateUserDto createUserRequest = CreateUserDto.builder()
            .name(USER_LEADER_NAME)
            .job(USER_LEADER_JOB)
            .build();

      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath())
                        .type(IS).expected(USER_LEADER_NAME).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Shows login POST with DTO credentials and asserting a non-null token via JSON path.")
   void showsLoginWithDtoAndTokenAssertion(Quest quest) {
      LoginDto loginRequest = LoginDto.builder()
            .email(Data.testData().username())
            .password(Data.testData().password())
            .build();

      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  loginRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(TOKEN.getJsonPath())
                        .type(NOT_NULL).expected(true).build()
            )
            .complete();
   }

   @Test
   @Description("Shows negative testing with requestAndValidate: build a DTO missing password, call POST /login, assert 400 and error via JSON path.")
   void showsNegativeLoginMissingPasswordWithErrorAssertion(Quest quest) {
      LoginDto login = LoginDto.builder()
            .email(Data.testData().username())
            .build();

      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  login,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath())
                        .type(IS).expected(MISSING_PASSWORD_ERROR).build()
            )
            .complete();
   }

}
