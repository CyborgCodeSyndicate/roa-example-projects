package io.cyborgcode.api.test.framework.data.cleaner;

import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.data.constants.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.ID_THREE;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

public final class DataCleanerFunctions {

   private DataCleanerFunctions() {
   }

   public static void deleteAdminUser(SuperQuest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            );
   }

}
