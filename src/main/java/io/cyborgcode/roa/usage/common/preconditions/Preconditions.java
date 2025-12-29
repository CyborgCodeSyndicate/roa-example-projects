package io.cyborgcode.roa.usage.common.preconditions;

import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;

import java.util.function.BiConsumer;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.ExampleEndpoints.*;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.USER_ID_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.usage.common.data.creator.DataCreator.USER_MODEL;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Registry of pre-test journeys (Preconditions).
 * <p>
 * This enum implements {@link PreQuestJourney}. You can define setup steps here
 * that run before your test logic. Use the {@code @PreQuest} annotation to attach
 * them to your tests.
 * </p>
 */
public enum Preconditions implements PreQuestJourney<Preconditions> {


    CREATE_USER_PRECONDITION((quest, objects) -> createUser(quest, (UserRequestDto) objects[0])),
    UPDATE_USER_PRECONDITION((quest, objects) -> updateUser(quest));


    public static final class Data {
        public static final String CREATE_USER_PRECONDITION = "CREATE_USER_PRECONDITION";
        public static final String UPDATE_USER_PRECONDITION = "UPDATE_USER_PRECONDITION";

        private Data() {
        }
    }

    private final BiConsumer<SuperQuest, Object[]> function;

    Preconditions(BiConsumer<SuperQuest, Object[]> function) {
        this.function = function;
    }

    @Override
    public BiConsumer<SuperQuest, Object[]> journey() {
        return function;
    }

    @Override
    public Preconditions enumImpl() {
        return this;
    }

    private static void createUser(SuperQuest quest, UserRequestDto requestDto) {
        quest.use(RING_OF_API)
                .requestAndValidate(
                        POST_CREATE_USER,
                        requestDto,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build());
    }

    private static void updateUser(SuperQuest quest) {
        UserRequestDto juniorUserDto = quest.getStorage()
                .sub(StorageKeysTest.PRE_ARGUMENTS)
                .get(USER_MODEL, UserRequestDto.class);

        quest.use(RING_OF_API)
                .requestAndValidate(
                        UPDATE_USER.withPathParam("id", quest.getStorage()
                                .sub(StorageKeysApi.API)
                                .get(POST_CREATE_USER, Response.class)
                                .getBody()
                                .jsonPath()
                                .getString(USER_ID_FROM_RESPONSE.getJsonPath())),
                        UserRequestDto.builder()
                                .name("Mr. " + juniorUserDto.getName())
                                .job("Senior " + juniorUserDto.getJob())
                                .build(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build());
    }

}
