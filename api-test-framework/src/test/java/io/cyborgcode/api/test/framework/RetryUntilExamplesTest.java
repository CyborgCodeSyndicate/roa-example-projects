package io.cyborgcode.api.test.framework;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.framework.retry.RetryCondition;
import io.cyborgcode.roa.framework.retry.RetryConditionImpl;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Demonstrates usage of {@code retryUntil} in the RoA DSL.
 *
 * <p>The idea:
 * <ul>
 *   <li>Define a {@link RetryCondition} that is periodically evaluated.</li>
 *   <li>RoA will poll this condition until it becomes {@code true} or a timeout is reached.</li>
 *   <li>Once satisfied, continue the fluent chain with a normal API call.</li>
 * </ul>
 *
 * <p>In real projects this can be used for:
 * <ul>
 *   <li>Waiting for asynchronous processing to complete (e.g. job finished, record exists).</li>
 *   <li>Waiting for an environment/service to become ready (health check, feature flag, etc.).</li>
 *   <li>Synchronizing test flows that depend on external state.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@API
class RetryUntilExamplesTest extends BaseQuest {

   @Test
   @Regression
   @Description("Feature demo: retryUntil — poll a custom condition every 1s (max 10s) and, once satisfied, proceed with a normal GET and assert 200.")
   void showsRetryUntilPollingThenGetsUsers(Quest quest) {
      AtomicInteger probeCounter = new AtomicInteger(0);

      // Define a simple condition that becomes true on the 3rd probe
      RetryCondition<Boolean> condition = new RetryConditionImpl<>(
            service -> {
               int attempt = probeCounter.incrementAndGet();
               return attempt >= 3;
            },
            result -> result
      );

      quest
            .use(RING_OF_API)
            // Wait up to 10s, polling every 1s, until the condition is true
            .retryUntil(
                  condition,
                  Duration.ofSeconds(10),
                  Duration.ofSeconds(1)
            )
            // After the condition is met, run a normal API request and validate
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .complete();
   }

}
