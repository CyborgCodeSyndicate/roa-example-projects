package io.cyborgcode.api.test.framework.preconditions;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import io.cyborgcode.roa.validator.core.Assertion;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

/**
 * Implementation of reusable precondition routines referenced by {@link Preconditions}.
 * <p>
 * Each method encapsulates a concrete setup step (for example, creating a user) that can be
 * triggered via {@code @PreQuest} journeys before a test executes, keeping test methods
 * focused on verification rather than state preparation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class PreconditionFunctions {

   private PreconditionFunctions() {
   }

   public static void createNewUser(SuperQuest quest, Object... objects) {
      createNewUser(quest, (CreateUserDto) objects[0]);
   }

   public static void createNewUser(SuperQuest quest, CreateUserDto userObjectRequest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userObjectRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build());
   }

}
