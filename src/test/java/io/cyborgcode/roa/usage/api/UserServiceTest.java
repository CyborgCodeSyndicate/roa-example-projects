package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.storage.DataExtractorsApi;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

@API
class UserServiceTest extends BaseQuest {

    @Test
    void userServiceTest(Quest quest) {

        quest
                .use(Rings.RING_OF_USER)
                .createUser(new UserRequestDto("John", "Engineer"))
                .drop()
                .use(RING_OF_API)
                .requestAndValidate(
                        Endpoints.UPDATE_USER.withPathParam("id",
                                retrieve(DataExtractorsApi.responseBodyExtraction(Endpoints.POST_CREATE_USER,
                                        "id"), String.class)),
                        new UserRequestDto("Mr. John", "Senior Engineer"),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Mr. John").build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Senior Engineer").build()
                )
                .complete();
    }
}
