package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.api.dto.response.UserResponseDto;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.DELETE_USER;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.USER_ID_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@API
class StorageTest extends BaseQuest {

    @Test
    void storageTest(Quest quest) {

        quest
                .use(RING_OF_API)
                .request(POST_CREATE_USER, new UserRequestDto("John", "Engineer"))
                .validate(() -> {
                    UserResponseDto responseDto = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                            .getBody().as(UserResponseDto.class);

                    assertEquals("John", responseDto.getName(), "User name is incorrect");
                    assertEquals("Engineer", responseDto.getJob(), "User job is incorrect");
                })
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                .getBody()
                                .jsonPath()
                                .getString(USER_ID_FROM_RESPONSE.getJsonPath())),

                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build())
                .complete();
    }
}
