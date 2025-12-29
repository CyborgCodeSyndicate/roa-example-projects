package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.data.test_data.Data;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.JOB_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.NAME_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
class DataConfigTest extends BaseQuest {

    @Test
    void dataConfigTest(Quest quest) {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(Data.testData().name())
                .job(Data.testData().job())
                .build();

        quest
                .use(RING_OF_API)
                .requestAndValidate(POST_CREATE_USER, userRequestDto,

                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(NAME_FROM_RESPONSE.getJsonPath()).type(IS).expected(userRequestDto.getName()).build(),
                        Assertion.builder().target(BODY).key(JOB_FROM_RESPONSE.getJsonPath()).type(IS).expected(userRequestDto.getJob()).build())
                .complete();
    }
}
