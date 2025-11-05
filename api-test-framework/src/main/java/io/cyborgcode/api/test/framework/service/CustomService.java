package io.cyborgcode.api.test.framework.service;

import io.cyborgcode.api.test.framework.api.dto.request.LoginRequest;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.DATA;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.PER_PAGE;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.SUPPORT_URL;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOTAL;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.TOTAL_PAGES;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.USER_AVATAR_BY_INDEX;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.USER_FIRST_NAME;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.USER_ID;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.data.constants.Headers.EXAMPLE_HEADER;
import static io.cyborgcode.api.test.framework.data.constants.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.PageTwo.PAGE_TWO_CONTAINS_ANY_USER;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.PageTwo.PAGE_TWO_DATA_SIZE;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.PageTwo.PAGE_TWO_EXPECTED_USERS;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.TOTAL_USERS_IN_PAGE_RANGE;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Support.SUPPORT_URL_REGEX;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Support.SUPPORT_URL_REQRES_FRAGMENT;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.ID_THREE;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.USER_ONE_FIRST_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.USER_SEVENTH_FIRST_NAME_LENGTH;
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
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

@Ring("Ring of Custom")
public class CustomService extends FluentService {

   public CustomService loginUserAndAddSpecificHeader(LoginRequest loginRequest) {
      quest.use(RING_OF_API)
            .request(POST_LOGIN_USER, loginRequest)
            .requestAndValidate(
                  GET_USER
                        .withPathParam(ID_PARAM, ID_THREE)
                        .withHeader(EXAMPLE_HEADER, quest.getStorage().sub(StorageKeysApi.API).get(POST_LOGIN_USER, Response.class)
                              .getBody()
                              .jsonPath()
                              .getString(TOKEN.getJsonPath())),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );
      return this;
   }

   public CustomService requestAndValidateGetAllUsers() {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam("page", PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(ContentType.JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(NOT).expected(1).build(),
                  Assertion.builder().target(BODY).key(TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
                  Assertion.builder().target(BODY).key(PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(CONTAINS).expected(SUPPORT_URL_REQRES_FRAGMENT).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected(SUPPORT_TEXT_PREFIX).build(),
                  Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                  Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(USER_SEVENTH_FIRST_NAME_LENGTH).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(LENGTH).expected(PAGE_TWO_DATA_SIZE).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected(SUPPORT_URL_REGEX).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected(USER_ONE_FIRST_NAME).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(BETWEEN).expected(TOTAL_USERS_IN_PAGE_RANGE).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ALL).expected(PAGE_TWO_EXPECTED_USERS).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ANY).expected(PAGE_TWO_CONTAINS_ANY_USER).build()
            );
      return this;
   }

}
