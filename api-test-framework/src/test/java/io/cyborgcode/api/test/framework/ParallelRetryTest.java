package io.cyborgcode.api.test.framework;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.framework.retry.RetryCondition;
import io.cyborgcode.roa.framework.retry.RetryConditionImpl;
import io.cyborgcode.roa.validator.core.Assertion;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.rest.ReqresEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.utils.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

@API
public class ParallelRetryTest extends BaseQuest {

   private static final AtomicBoolean conditionMet = new AtomicBoolean(false);

   @Test
   public void testUpdateCondition(Quest quest) {
      try {
         Thread.sleep(3000);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
      conditionMet.set(true);
   }

   @Test
   public void testWaitForCondition(Quest quest) {
      quest.use(RING_OF_API)
            .retryUntil(
                  sharedFlagIsTrue(),
                  Duration.ofSeconds(20),
                  Duration.ofSeconds(1)
            )
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );
   }

   private RetryCondition<Boolean> sharedFlagIsTrue() {
      return new RetryConditionImpl<>(
            service -> ParallelRetryTest.conditionMet.get(),
            result -> result
      );
   }

}
