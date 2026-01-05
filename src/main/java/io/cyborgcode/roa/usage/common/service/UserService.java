package io.cyborgcode.roa.usage.common.service;

import io.cyborgcode.roa.api.storage.DataExtractorsApi;
import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import io.cyborgcode.roa.usage.api.Endpoints;
import io.cyborgcode.roa.usage.api.dto.request.UserRequestDto;
import io.cyborgcode.roa.usage.common.base.Rings;
import io.cyborgcode.roa.validator.core.Assertion;

import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

@Ring("Custom User Service")
public class UserService extends FluentService {

   public UserService createUser(UserRequestDto userRequestDto) {
      quest
            .use(Rings.RING_OF_API)
            .request(Endpoints.POST_CREATE_USER, userRequestDto)
            .requestAndValidate(
                  Endpoints.GET_USER.withPathParam("id",
                        quest.getStorage().get(DataExtractorsApi.responseBodyExtraction(Endpoints.POST_CREATE_USER,
                              "id"), String.class)
                  ),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key("name").type(IS).expected(userRequestDto.getName()).build()
            );
      return this;
   }
}
