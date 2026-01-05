package io.cyborgcode.roa.usage.common.preconditions;

import io.cyborgcode.roa.api.storage.DataExtractorsApi;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.framework.storage.StorageKeysTest;
import io.cyborgcode.roa.usage.api.Endpoints;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.usage.common.data.creator.DataCreator;
import io.cyborgcode.roa.validator.core.Assertion;

import java.util.function.BiConsumer;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.base.BaseQuest.DefaultStorage.retrieve;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

public enum Preconditions implements PreQuestJourney<Preconditions> {

   CREATE_USER_PRECONDITION((quest, objects) -> createUser(quest, (UserRequestDto) objects[0])),
   UPDATE_USER_PRECONDITION((quest, objects) -> updateUser(quest));

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
      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(Endpoints.POST_CREATE_USER, requestDto,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            );
   }

   private static void updateUser(SuperQuest quest) {
      UserRequestDto juniorUserDto = quest.getStorage()
            .sub(StorageKeysTest.PRE_ARGUMENTS)
            .get(DataCreator.USER_MODEL, UserRequestDto.class);

      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(
                  Endpoints.UPDATE_USER.withPathParam("id",
                        retrieve(DataExtractorsApi.responseBodyExtraction(Endpoints.POST_CREATE_USER,
                              "id"), String.class)
                  ),
                  UserRequestDto.builder()
                        .name("Mr. " + juniorUserDto.getName())
                        .job("Senior " + juniorUserDto.getJob())
                        .build(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );
   }

}
