package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserRequest;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuestSequential;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_EVOLUTION;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_SENIOR_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_SENIOR_NAME;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
class CreateTwoUsersEvolutionTest extends BaseQuestSequential {

   @Test
   @Regression
   void testCreateTwoUsersBasic(Quest quest) {
      CreateUserRequest createLeaderUserRequest = CreateUserRequest.builder()
            .name(USER_LEADER_NAME)
            .job(USER_LEADER_JOB)
            .build();

      CreateUserRequest createSeniorUserRequest = CreateUserRequest.builder()
            .name(USER_SENIOR_NAME)
            .job(USER_SENIOR_JOB)
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createLeaderUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  createSeniorUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testCreateTwoUsersImproved(Quest quest, @Craft(model = DataCreator.Data.USER_LEADER_REQUEST) CreateUserRequest createLeaderUserRequest, @Craft(model = DataCreator.Data.USER_SENIOR_REQUEST) Late<CreateUserRequest> createSeniorUserRequest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  createLeaderUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  createSeniorUserRequest.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testCreateTwoUsersImprovedWithCustomService(Quest quest, @Craft(model = DataCreator.Data.USER_LEADER_REQUEST) CreateUserRequest createLeaderUserRequest, @Craft(model = DataCreator.Data.USER_SENIOR_REQUEST) Late<CreateUserRequest> createSeniorUserRequest) {
      quest.use(RING_OF_EVOLUTION)
            .createLeaderUserAndValidateResponse(createLeaderUserRequest)
            .createSeniorUserAndValidateResponse(createSeniorUserRequest.create())
            .complete();
   }

}
