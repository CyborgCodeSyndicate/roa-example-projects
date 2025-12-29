package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

@API
class GettingStartedTest extends BaseQuest {

    @Test
    void basicTest(Quest quest) {
        quest
                .use(RING_OF_API)
                .requestAndValidate(Endpoints.GET_USER.withPathParam("id", 1),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
                )
                .complete();
    }
}
