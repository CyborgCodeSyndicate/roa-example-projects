package io.cyborgcode.roa.usage.common.data.cleaner;

import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.restassured.response.Response;

import java.util.function.Consumer;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.usage.api.Endpoints.DELETE_USER;
import static io.cyborgcode.roa.usage.api.Endpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.usage.api.extractors.ApiResponsesJsonPaths.USER_ID_FROM_RESPONSE;
import static io.cyborgcode.roa.usage.common.base.Rings.RING_OF_API;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

/**
 * Defines reusable cleanup (data ripping) operations for your test suite.
 * <p>
 * This enum implements {@link DataRipper}, allowing you to define cleanup logic
 * that can be attached to tests using the {@code @Ripper} annotation.
 * </p>
 */
public enum DataCleaner implements DataRipper<DataCleaner> {

    CLEANUP_USER(DataCleaner::deleteUser);

    private final Consumer<SuperQuest> cleanUpFunction;

    DataCleaner(Consumer<SuperQuest> cleanUpFunction) {
        this.cleanUpFunction = cleanUpFunction;
    }

    @Override
    public Consumer<SuperQuest> eliminate() {
        return cleanUpFunction;
    }

    @Override
    public DataCleaner enumImpl() {
        return this;
    }

    private static void deleteUser(SuperQuest quest) {
        quest.use(RING_OF_API)
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", quest.getStorage()
                                .sub(StorageKeysApi.API)
                                .get(POST_CREATE_USER, Response.class)
                                .getBody()
                                .jsonPath()
                                .getString(USER_ID_FROM_RESPONSE.getJsonPath())
                        ),

                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                );
    }
}
