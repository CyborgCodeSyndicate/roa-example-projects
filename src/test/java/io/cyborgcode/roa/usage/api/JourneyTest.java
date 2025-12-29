package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.*;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.common.data.creator.DataCreator;
import io.cyborgcode.roa.usage.common.preconditions.Preconditions;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.*;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.*;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
class JourneyTest extends BaseQuest {

    @Test
    @Journey(
            value = Preconditions.Data.CREATE_USER_PRECONDITION,
            journeyData = {@JourneyData(DataCreator.Data.USER_MODEL)},
            order = 1
    )
    @Journey(
            value = Preconditions.Data.UPDATE_USER_PRECONDITION,
            order = 2
    )
    void journeyTest(Quest quest) {
        quest
                .use(RING_OF_API)
                .requestAndValidate(
                        GET_USER.withPathParam("id",
                                retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                        .getBody().jsonPath().getString(USER_ID_FROM_RESPONSE.getJsonPath())
                        ),

                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(NAME_FROM_RESPONSE.getJsonPath()).type(IS).expected("Mr. John").build(),
                        Assertion.builder().target(BODY).key(JOB_FROM_RESPONSE.getJsonPath()).type(IS).expected("Senior Engineer").build())
                .complete();
    }
}
