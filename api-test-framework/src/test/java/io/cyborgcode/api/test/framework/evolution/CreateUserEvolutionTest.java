package io.cyborgcode.api.test.framework.evolution;

import io.cyborgcode.api.test.framework.api.dto.request.CreateUserDto;
import io.cyborgcode.api.test.framework.api.dto.response.GetUsersDto;
import io.cyborgcode.api.test.framework.api.dto.response.UserData;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.parameters.Late;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.api.AppEndpoints.GET_ALL_USERS;
import static io.cyborgcode.api.test.framework.api.AppEndpoints.POST_CREATE_USER;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_EVOLUTION;
import static io.cyborgcode.api.test.framework.data.constants.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_JUNIOR_JOB;
import static io.cyborgcode.api.test.framework.data.constants.TestConstants.Roles.USER_JUNIOR_NAME;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Demonstrates the evolution from manual data construction to fully reusable data creation
 * using storage, {@code @Craft}, {@code Late}, and a custom ROA ring for Reqres user creation.
 */
@API
class CreateUserEvolutionTest extends BaseQuest {

   @Test
   @Regression
   @Description("Creates a junior user using data derived from a previously fetched users list.")
   void createsJuniorUserUsingStoredUsersResponse(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );

      UserData firstUser = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersDto.class)
            .getData()
            .get(0);

      CreateUserDto juniorUser = CreateUserDto.builder()
            .name(firstUser.getFirstName() + " suffix")
            .job("Junior " + firstUser.getLastName() + " worker")
            .build();

      quest.use(RING_OF_API)
            .requestAndValidate(
                  POST_CREATE_USER,
                  juniorUser,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Creates a junior user using @Craft with Late to defer request body generation until after prerequisites run.")
   void createsJuniorUserUsingCraftAndLate(Quest quest,
                                                @Craft(model = DataCreator.Data.USER_JUNIOR) Late<CreateUserDto> juniorUser) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  juniorUser.create(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
            )
            .complete();
   }

   @Test
   @Regression
   @Description("Creates a junior user using a custom ROA ring to encapsulate reusable API steps.")
   void createsJuniorUserUsingCustomService(Quest quest,
                                                 @Craft(model = DataCreator.Data.USER_JUNIOR) Late<CreateUserDto> juniorUser) {
      quest.use(RING_OF_EVOLUTION)
            .getAllUsersAndValidateResponse()
            .createJuniorUserAndValidateResponse(juniorUser.create())
            .complete();
   }

}
