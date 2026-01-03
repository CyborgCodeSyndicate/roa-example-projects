package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.core.Endpoint;
import io.cyborgcode.roa.framework.annotation.StaticTestData;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.usage.common.data.test_data.StaticData;
import io.cyborgcode.roa.validator.core.Assertion;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.storage.DataExtractorsTest.staticTestData;
import static io.cyborgcode.roa.usage.api.Endpoints.GET_USER;
import static io.cyborgcode.roa.usage.api.Endpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@API
class StaticDataTest extends BaseQuest {

   @Test
   @StaticTestData(StaticData.class)
   void dataTest(Quest quest) {

      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(Endpoints.POST_CREATE_USER,
                  new UserRequestDto(
                        retrieve(staticTestData(StaticData.NAME), String.class),
                        retrieve(staticTestData(StaticData.JOB), String.class)),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key("name").type(IS).expected("John").build(),
                  Assertion.builder().target(BODY).key("job").type(IS).expected("Engineer").build()
            )
            .complete();
   }


   @ParameterizedTest
   @MethodSource("scenarios")
   @StaticTestData(StaticData.class)
   void parameterizedTest(Endpoint<?> endpoint, int status, Quest quest) {

      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(endpoint, new UserRequestDto("John", "Engineer"),
                  Assertion.builder().target(STATUS).type(IS).expected(status).build()
            )
            .complete();
   }

   static Stream<Arguments> scenarios() {
      return Stream.of(
            Arguments.of(POST_CREATE_USER, SC_CREATED),
            Arguments.of(GET_USER, SC_OK)
      );
   }
}
