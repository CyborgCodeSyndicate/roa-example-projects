package io.cyborgcode.api.test.framework.login;

import io.cyborgcode.api.test.framework.api.dto.request.LoginUserRequest;
import io.cyborgcode.api.test.framework.data.retriever.DataProperties;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.ERROR;
import static io.cyborgcode.api.test.framework.api.ApiResponsesJsonPaths.TOKEN;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_LOGIN_USER;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.INVALID_EMAIL;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.MISSING_EMAIL_ERROR;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.MISSING_PASSWORD_ERROR;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.TOKEN_REGEX;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Login.USER_NOT_FOUND_ERROR;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.MATCHES_REGEX;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_NULL;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

@API
class LoginFunctionalityAIGeneratedTest extends BaseQuest {

   private static final DataProperties DATA_PROPERTIES = ConfigCache.getOrCreate(DataProperties.class);

   @Test
   @Regression
   void testSuccessfulLogin(Quest quest) {
      LoginUserRequest validUserRequest = LoginUserRequest.builder()
            .email(DATA_PROPERTIES.username())
            .password(DATA_PROPERTIES.password())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  validUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(MATCHES_REGEX).expected(TOKEN_REGEX).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testLoginMissingPassword(Quest quest) {
      LoginUserRequest noPasswordUserRequest = LoginUserRequest.builder()
            .email(DATA_PROPERTIES.username())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  noPasswordUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(MISSING_PASSWORD_ERROR).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testLoginMissingEmail(Quest quest) {
      LoginUserRequest noEmailUserRequest = LoginUserRequest.builder()
            .password(DATA_PROPERTIES.password())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  noEmailUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(MISSING_EMAIL_ERROR).build()
            )
            .complete();
   }

   @Test
   @Regression
   void testLoginWithInvalidEmail(Quest quest) {
      LoginUserRequest invalidEmailUserRequest = LoginUserRequest.builder()
            .email(INVALID_EMAIL)
            .password(DATA_PROPERTIES.password())
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_LOGIN_USER,
                  invalidEmailUserRequest,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                  Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(USER_NOT_FOUND_ERROR).build()
            )
            .complete();
   }

}
