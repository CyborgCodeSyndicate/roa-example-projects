package io.cyborgcode.api.test.framework.api.hooks;

import io.cyborgcode.roa.api.hooks.ApiHookFlow;
import io.cyborgcode.roa.api.service.RestService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

public enum ApiHookFlows implements ApiHookFlow<ApiHookFlows> {

   PING_REQRES(ApiHookFunctions::pingReqres),
   DELETE_LEADER_USER(ApiHookFunctions::deleteLeaderUser);

   public static final class Data {
      private Data() {
      }

      public static final String PING_REQRES = "PING_REQRES";
      public static final String DELETE_LEADER_USER = "DELETE_LEADER_USER";
   }

   private final TriConsumer<RestService, Map<Object, Object>, String[]> flow;

   ApiHookFlows(final TriConsumer<RestService, Map<Object, Object>, String[]> flow) {
      this.flow = flow;
   }

   @Override
   public TriConsumer<RestService, Map<Object, Object>, String[]> flow() {
      return flow;
   }

   @Override
   public ApiHookFlows enumImpl() {
      return this;
   }
}
