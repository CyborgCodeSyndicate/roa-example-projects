package io.cyborgcode.api.test.framework.preconditions;

import io.cyborgcode.api.test.framework.rest.dto.request.User;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

public final class PreconditionFunctions {

   private PreconditionFunctions() {
   }

   public static void createNewUser(SuperQuest quest, Object... objects) {
      createNewUser(quest, (User) objects[0]);
   }

   public static void createNewUser(SuperQuest quest, User userObject) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userObject,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build());
   }

}
