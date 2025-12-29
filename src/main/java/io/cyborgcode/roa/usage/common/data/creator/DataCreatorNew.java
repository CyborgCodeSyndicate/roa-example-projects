package io.cyborgcode.roa.usage.common.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;

public enum DataCreatorNew implements DataForge<DataCreatorNew> {

    USER_MODEL_1(DataCreatorNew::userModel),
    USER_LATE_MODEL_1(DataCreatorNew::userLateModel);

    private final Late<Object> createDataFunction;

    DataCreatorNew(final Late<Object> createDataFunction) {
        this.createDataFunction = createDataFunction;
    }

    @Override
    public Late<Object> dataCreator() {
        return createDataFunction;
    }

    @Override
    public DataCreatorNew enumImpl() {
        return this;
    }

    private static Object userModel() {
        return UserRequestDto.builder()
                .name("John")
                .job("Engineer")
                .build();
    }

    private static UserRequestDto userLateModel() {
        SuperQuest quest = QuestHolder.get();
        UserRequestDto juniorUserDto = quest.getStorage()
                .sub(StorageKeysTest.ARGUMENTS).get(USER_MODEL_1, UserRequestDto.class);

        return UserRequestDto.builder()
                .name("Mr. " + juniorUserDto.getName())
                .job("Senior " + juniorUserDto.getJob())
                .build();
    }

}
