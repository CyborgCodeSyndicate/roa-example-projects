package io.cyborgcode.roa.usage.common.data.cleaner;

import io.cyborgcode.roa.api.storage.DataExtractorsApi;
import io.cyborgcode.roa.framework.parameters.DataRipper;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.usage.api.Endpoints;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.validator.core.Assertion;
import java.util.function.Consumer;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.framework.base.BaseQuest.DefaultStorage.retrieve;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

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
      quest
            .use(Rings.RING_OF_API)
            .requestAndValidate(
                  Endpoints.DELETE_USER.withPathParam("id",
                        retrieve(DataExtractorsApi.responseBodyExtraction(Endpoints.POST_CREATE_USER,
                              "id"), String.class)
                  ),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            );
   }
}
