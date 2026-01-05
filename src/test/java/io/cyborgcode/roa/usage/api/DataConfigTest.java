package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.usage.common.data.test_data.Data.testData;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
class DataConfigTest extends BaseQuest {

   @Test
   void usingDataAndConfigurationTest(Quest quest) {
      quest
            .use(RING_OF_API)
            .requestAndValidate(Endpoints.POST_CREATE_USER, UserRequestDto.builder()
                        .name(testData().name())
                        .job(testData().job())
                        .build(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key("name").type(IS).expected(testData().name()).build(),
                  Assertion.builder().target(BODY).key("job").type(IS).expected(testData().job()).build()
            )
            .complete();
   }
}

