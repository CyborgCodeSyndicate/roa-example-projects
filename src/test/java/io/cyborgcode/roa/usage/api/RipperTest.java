package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Ripper;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
class RipperTest extends BaseQuest {

    @Test
    @Ripper(targets = {"CLEANUP_USER"})
    void craftTest(Quest quest,
                   @Craft(model = "USER_MODEL") UserRequestDto juniorUserDto) {
        quest
                .use(Rings.RING_OF_API)
                .requestAndValidate(Endpoints.POST_CREATE_USER, juniorUserDto,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("John").build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Engineer").build()
                )
                .complete();
    }
}
