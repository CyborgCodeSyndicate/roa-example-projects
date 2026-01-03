package io.cyborgcode.roa.usage.common.data.creator;

import io.cyborgcode.roa.framework.parameters.DataForge;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.QuestHolder;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;

//4
public enum DataCreator implements DataForge<DataCreator> {

   //3
   USER_MODEL(DataCreator::userModel),
   //7
   USER_LATE_MODEL(DataCreator::userLateModel);

   //2
   private final Late<Object> createDataFunction;

   //2
   DataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }

   //5
   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }

   //5
   @Override
   public DataCreator enumImpl() {
      return this;
   }

   //1
   private static Object userModel() {
      return UserRequestDto.builder()
            .name("John")
            .job("Engineer")
            .build();
   }

   //6
   private static UserRequestDto userLateModel() {
      SuperQuest quest = QuestHolder.get();
      UserRequestDto juniorUserDto = quest.getStorage()
            .sub(StorageKeysTest.ARGUMENTS).get(USER_MODEL, UserRequestDto.class);

      return UserRequestDto.builder()
            .name("Mr. " + juniorUserDto.getName())
            .job("Senior " + juniorUserDto.getJob())
            .build();
   }

}
