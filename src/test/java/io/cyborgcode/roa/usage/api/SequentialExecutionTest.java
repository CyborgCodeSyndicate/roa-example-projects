package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.log.LogApi;
import io.cyborgcode.roa.framework.base.BaseQuestSequential;
import io.cyborgcode.roa.framework.base.Services;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@API
class SequentialExecutionTest extends BaseQuestSequential {

   @Override
   protected void beforeAll(Services services) {
      LogApi.info("Starting test execution...");
   }

   /*@BeforeAll
   void setUp() {
      LogApi.info("Starting test execution...");
   }

   @AfterAll
   void tearDown() {
      LogApi.info("Test execution completed.");
   }*/

   @Test
   void getSingleUserTest(Quest quest) {
      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(Endpoints.GET_USER.withPathParam("id", 1),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

   @Test
   void getAllUsersTest(Quest quest) {
      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(Endpoints.GET_ALL_USERS,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

   @Test
   void createUserTest(Quest quest) {
      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(Endpoints.POST_CREATE_USER, new UserRequestDto("John", "Engineer"),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .complete();
   }
}
