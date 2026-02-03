package io.cyborgcode.api.test.framework.api.hooks;

import io.cyborgcode.roa.api.service.RestService;
import io.cyborgcode.roa.validator.core.Assertion;
import java.util.Map;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.DELETE_USER;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.data.constants.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Users.ID_THREE;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;

public final class ApiHookFunctions {

   private ApiHookFunctions() {
   }

   public static void pingReqres(RestService service,
                                 Map<Object, Object> storage,
                                 String[] arguments) {

      service.requestAndValidate(
            GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
            Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
      );
   }

   public static void deleteLeaderUser(RestService service,
                                       Map<Object, Object> storage,
                                       String[] arguments) {

      service.requestAndValidate(
            DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
            Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
      );
   }

}
