package io.cyborgcode.roa.usage.common.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;

/**
 * Registry of reusable test data factories.
 * <p>
 * This enum implements {@link DataForge}, allowing you to define "Late" objects -
 * data that is instantiated just before the test runs. Use the {@code @Craft} annotation
 * to inject these into your tests.
 * </p>
 */
public enum DataCreator implements DataForge<DataCreator> {


    USER_MODEL(() -> UserRequestDto.builder()
            .name("John")
            .job("Engineer")
            .build()),
    USER_LATE_MODEL(DataCreator::userLateModel);


    public static final class Data {

        public static final String USER_MODEL = "USER_MODEL";
        public static final String USER_LATE_MODEL = "USER_LATE_MODEL";

        private Data() {
        }
    }

    private final Late<Object> createDataFunction;

    DataCreator(final Late<Object> createDataFunction) {
        this.createDataFunction = createDataFunction;
    }

    @Override
    public Late<Object> dataCreator() {
        return createDataFunction;
    }

    @Override
    public DataCreator enumImpl() {
        return this;
    }

    private static UserRequestDto userLateModel() {
        SuperQuest quest = QuestHolder.get();
        UserRequestDto juniorUserDto = quest.getStorage()
                .sub(StorageKeysTest.ARGUMENTS)
                .get(USER_MODEL, UserRequestDto.class);
        return UserRequestDto.builder()
                .name("Mr. " + juniorUserDto.getName())
                .job("Senior " + juniorUserDto.getJob())
                .build();
    }
}
