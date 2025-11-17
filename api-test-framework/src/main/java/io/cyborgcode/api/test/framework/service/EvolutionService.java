package io.cyborgcode.api.test.framework.service;

import io.cyborgcode.api.test.framework.api.dto.response.CreatedUserDto;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.restassured.response.Response;
import java.time.Instant;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_AT_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_JOB_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.AssertionMessages.CREATED_USER_NAME_INCORRECT;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ROA service ("Ring of Evolution") used in the evolution examples.
 * <p>
 * This class shows how you can move repeated test logic into reusable,
 * fluent methods instead of duplicating it in every test.
 * <ul>
 *   <li>Wraps common flows such as "get all users", "create user", "validate created user".</li>
 *   <li>Keeps tests focused on <i>what</i> they verify, not <i>how</i> they call the API.</li>
 *   <li>Used in the evolution tests via {@code quest.use(RING_OF_EVOLUTION)} to demonstrate
 *       how to gradually refactor scenarios into shared building blocks.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Ring("Ring of Evolution")
public class EvolutionService extends FluentService {

   public EvolutionService validateCreatedUser() {
      quest.use(RING_OF_API)
            .validate(() -> {
               CreatedUserDto createdUser = quest
                     .getStorage()
                     .sub(StorageKeysApi.API)
                     .get(POST_CREATE_USER, Response.class)
                     .getBody()
                     .as(CreatedUserDto.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
               assertTrue(createdUser
                     .getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            });
      return this;
   }

}
