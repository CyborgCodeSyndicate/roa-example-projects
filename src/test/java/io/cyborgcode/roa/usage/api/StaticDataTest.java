package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.core.Endpoint;
import io.cyborgcode.roa.framework.annotation.StaticTestData;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.data.test_data.StaticData;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.storage.DataExtractorsTest.staticTestData;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.GET_USER;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.JOB_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.NAME_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@API
class StaticDataTest extends BaseQuest {

    @Test
    @StaticTestData(StaticData.class)
    void dataTest(Quest quest) {

        String name = retrieve(staticTestData(StaticData.NAME), String.class);
        String job = retrieve(staticTestData(StaticData.JOB), String.class);

        quest
                .use(RING_OF_API)
                .requestAndValidate(
                        POST_CREATE_USER,
                        new UserRequestDto(name, job),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(NAME_FROM_RESPONSE.getJsonPath()).type(IS).expected(name).soft(true).build(),
                        Assertion.builder().target(BODY).key(JOB_FROM_RESPONSE.getJsonPath()).type(IS).expected(job).soft(true).build())
                .complete();
    }


    @ParameterizedTest
    @MethodSource("scenarios")
    @StaticTestData(StaticData.class)
    void parameterizedTest(Endpoint<?> scenario, int status, Quest quest) {

        String name = retrieve(staticTestData(StaticData.NAME), String.class);
        String job = retrieve(staticTestData(StaticData.JOB), String.class);

        quest
                .use(RING_OF_API)
                .requestAndValidate(
                        scenario,
                        new UserRequestDto(name, job),
                        Assertion.builder().target(STATUS).type(IS).expected(status).build())
                .complete();
    }

    static Stream<Arguments> scenarios() {
        return Stream.of(
                Arguments.of(POST_CREATE_USER, SC_CREATED),
                Arguments.of(GET_USER, SC_OK)
        );
    }
}
