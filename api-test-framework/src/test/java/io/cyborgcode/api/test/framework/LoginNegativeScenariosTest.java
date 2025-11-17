package io.cyborgcode.api.test.framework;

import io.cyborgcode.api.test.framework.api.dto.request.LoginDto;
import io.cyborgcode.api.test.framework.data.test_data.Data;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.ERROR;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.INVALID_EMAIL;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.MISSING_EMAIL_ERROR;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.MISSING_PASSWORD_ERROR;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.USER_NOT_FOUND_ERROR;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * Demonstrates how to validate negative login scenarios with RoA against Reqres:
 * missing password, missing email, and invalid email, all returning 400 with proper error messages.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@API
class LoginNegativeScenariosTest extends BaseQuest {

   @Test
   @Description("Verifies that a login request without password returns 400 and MISSING_PASSWORD_ERROR.")
   void returns400AndErrorWhenPasswordIsMissing(Quest quest) {
      LoginDto login = LoginDto.builder()
            .email(Data.testData().username())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  login,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath())
                        .type(IS).expected(MISSING_PASSWORD_ERROR).build()
            )
            .complete();
   }

   @Test
   @Description("Verifies that a login request without email returns 400 and MISSING_EMAIL_ERROR.")
   void returns400AndErrorWhenEmailIsMissing(Quest quest) {
      LoginDto login = LoginDto.builder()
            .password(Data.testData().password())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  login,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath())
                        .type(IS).expected(MISSING_EMAIL_ERROR).build()
            )
            .complete();
   }

   @Test
   @Description("Verifies that a login request with INVALID_EMAIL returns 400 and USER_NOT_FOUND_ERROR.")
   void returns400AndErrorWhenEmailIsInvalid(Quest quest) {
      LoginDto login = LoginDto.builder()
            .email(INVALID_EMAIL)
            .password(Data.testData().password())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  login,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath())
                        .type(IS).expected(USER_NOT_FOUND_ERROR).build()
            )
            .complete();
   }

}
