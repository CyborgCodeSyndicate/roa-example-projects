package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuestSequential;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
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

/**
 * Demonstrates the evolution of creating multiple users:
 * from inline DTO construction, through {@code @Craft} and {@code Late},
 * to using a custom ROA ring that encapsulates reusable steps.
 * Requires {@link API} because it uses {@code RING_OF_API} and {@code RING_OF_EVOLUTION}.
 */
@API
class CreateTwoUsersEvolutionTest extends BaseQuestSequential {

   @Test
   @Regression
   @Description("Creates two users with inline request bodies and validates both responses.")
   void shouldCreateTwoUsersWithInlineData(Quest quest) {
      CreateUserDto leaderUser = CreateUserDto.builder()
            .name(USER_LEADER_NAME)
            .job(USER_LEADER_JOB)
            .build();

      CreateUserDto seniorUser = CreateUserDto.builder()
            .name(USER_SENIOR_NAME)
            .job(USER_SENIOR_JOB)
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  leaderUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  seniorUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Creates two users using @Craft and Late to externalize and reuse request body creation.")
   void shouldCreateTwoUsersUsingCraftAndLate(Quest quest,
                                              @Craft(model = DataCreator.Data.USER_LEADER) CreateUserDto leaderUser,
                                              @Craft(model = DataCreator.Data.USER_SENIOR) Late<CreateUserDto> seniorUser) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  leaderUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  seniorUser.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Creates two users using a custom ROA ring that encapsulates the user creation and validation flow.")
   void shouldCreateTwoUsersUsingCustomService(Quest quest,
                                               @Craft(model = DataCreator.Data.USER_LEADER) CreateUserDto leaderUser,
                                               @Craft(model = DataCreator.Data.USER_SENIOR) Late<CreateUserDto> seniorUser) {
      quest.use(RING_OF_EVOLUTION)
            .createLeaderUserAndValidateResponse(leaderUser)
            .createSeniorUserAndValidateResponse(seniorUser.create())
            .complete();
   }

}
