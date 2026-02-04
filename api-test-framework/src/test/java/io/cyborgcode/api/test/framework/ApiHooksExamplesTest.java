package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.api.hooks.ApiHookFlows;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.ApiHook;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Smoke;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.hooks.HookExecution;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_LEADER_NAME;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
@ApiHook(when = HookExecution.BEFORE, type = ApiHookFlows.Data.PING_REQRES)
@ApiHook(when = HookExecution.AFTER, type = ApiHookFlows.Data.DELETE_LEADER_USER)
class ApiHooksExamplesTest extends BaseQuest {

   @Test
   @Smoke
   void showsCraftModelAsRequestWithSoftAssertion(Quest quest,
                                                  @Craft(model = DataCreator.Data.USER_LEADER) CreateUserDto leaderUser) {
      quest
            .use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  leaderUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath())
                        .type(IS).expected(USER_LEADER_NAME).soft(true).build()
            )
            .complete();
   }
}
