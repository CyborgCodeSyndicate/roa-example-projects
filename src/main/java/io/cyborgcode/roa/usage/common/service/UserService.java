package io.cyborgcode.roa.usage.common.service;

import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.GET_USER;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.*;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Custom service for complex, multi-step workflows.
 * <p>
 * Add methods here that combine UI, API, and DB operations.
 * Example: createUserWithOrder(), setupTestEnvironment(), etc.
 * </p>
 */
@Ring("User")
public class UserService extends FluentService {

    public UserService createUser(UserRequestDto userRequestDto) {
        quest.use(RING_OF_API)
                .request(POST_CREATE_USER, userRequestDto)
                .requestAndValidate(
                        GET_USER
                                .withPathParam("id",  quest.getStorage().sub(StorageKeysApi.API).get(POST_CREATE_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString(USER_ID_FROM_RESPONSE.getJsonPath())),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath()).type(IS).expected(userRequestDto.getName()).build()
                );
        return this;
    }
}
