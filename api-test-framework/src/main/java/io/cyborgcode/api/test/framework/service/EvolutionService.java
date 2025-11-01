package io.cyborgcode.api.test.framework.service;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserRequest;
import io.cyborgcode.api.test.framework.api.dto.response.CreatedUserResponse;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import java.time.Instant;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.CREATE_USER_JOB;
import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.CREATE_USER_NAME;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_AT_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_JOB_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_NAME_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_JUNIOR_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_JUNIOR_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_SENIOR_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_SENIOR_NAME;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.restassured.http.ContentType.JSON;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Ring("Ring of Evolution")
public class EvolutionService extends FluentService {

   public EvolutionService getAllUsersAndValidateResponse() {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );
      return this;
   }

   public EvolutionService createJuniorUserAndValidateResponse(CreateUserRequest createJuniorUserRequest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createJuniorUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
            );
      return this;
   }

   public EvolutionService createLeaderUserAndValidateResponse(CreateUserRequest createLeaderUserRequest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createLeaderUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            );
      return this;
   }

   public EvolutionService createSeniorUserAndValidateResponse(CreateUserRequest createSeniorUserRequest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createSeniorUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            );
      return this;
   }

   public EvolutionService validateCreatedUser() {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = quest
                     .getStorage()
                     .sub(StorageKeysApi.API)
                     .get(POST_CREATE_USER, Response.class)
                     .getBody()
                     .as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUserResponse
                     .getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            });
      return this;
   }

}
