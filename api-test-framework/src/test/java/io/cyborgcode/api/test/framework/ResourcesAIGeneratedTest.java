package io.cyborgcode.api.test.framework;

import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.api.test.framework.base.Rings.RING_OF_API;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.DATA;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_COLOR;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_COLOR_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_ID;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_ID_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_NAME;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_NAME_BY_INDEX;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_PAGE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_PANTONE;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.RESOURCE_YEAR;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.ROOT;
import static io.cyborgcode.api.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.GET_ALL_RESOURCES;
import static io.cyborgcode.api.test.framework.rest.AppEndpoints.GET_RESOURCE;
import static io.cyborgcode.api.test.framework.utils.Helpers.EMPTY_JSON;
import static io.cyborgcode.api.test.framework.utils.PathVariables.ID_PARAM;
import static io.cyborgcode.api.test.framework.utils.QueryParams.PAGE_PARAM;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_ONE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_THREE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_INVALID_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_ONE_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_ONE_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_SIX_COLOR;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_SIX_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_TWO_COLOR;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_TWO_ID;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_TWO_NAME;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_TWO_PANTONE;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Resources.RESOURCE_TWO_YEAR;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_BRAND;
import static io.cyborgcode.api.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_FULL;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.BODY;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.HEADER;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.STATUS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.CONTAINS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.EMPTY;
import static io.cyborgcode.roa.validator.core.AssertionTypes.IS;
import static io.cyborgcode.roa.validator.core.AssertionTypes.NOT_EMPTY;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

@API
class ResourcesAIGeneratedTest extends BaseQuest {

   @Test
   void testGetAllResourcesPage1(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_ONE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_ID_BY_INDEX.getJsonPath(0)).type(IS).expected(RESOURCE_ONE_ID).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(0)).type(IS).expected(RESOURCE_ONE_NAME).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_FULL).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
            )
            .complete();
   }

   @Test
   void testGetAllResourcesPage2(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(PAGE_TWO).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(5)).type(IS).expected(RESOURCE_SIX_NAME).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_COLOR_BY_INDEX.getJsonPath(5)).type(IS).expected(RESOURCE_SIX_COLOR).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
            )
            .complete();
   }

   @Test
   void testGetAllResourcesPage3OrMoreReturnsEmptyData(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(PAGE_THREE).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(EMPTY).expected(true).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_BRAND).build()
            )
            .complete();
   }

   @Test
   void testGetResourceById2_Positive(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_RESOURCE.withPathParam(ID_PARAM, RESOURCE_TWO_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_ID.getJsonPath()).type(IS).expected(RESOURCE_TWO_ID).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_NAME.getJsonPath()).type(IS).expected(RESOURCE_TWO_NAME).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_YEAR.getJsonPath()).type(IS).expected(RESOURCE_TWO_YEAR).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_COLOR.getJsonPath()).type(IS).expected(RESOURCE_TWO_COLOR).build(),
                  Assertion.builder().target(BODY).key(RESOURCE_PANTONE.getJsonPath()).type(IS).expected(RESOURCE_TWO_PANTONE).build()
            )
            .complete();
   }

   @Test
   void testGetResourceById23_NotFound(Quest quest) {
      quest.use(RING_OF_API)
            .requestAndValidate(
                  GET_RESOURCE.withPathParam(ID_PARAM, RESOURCE_INVALID_ID),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                  Assertion.builder().target(BODY).key(ROOT.getJsonPath()).type(IS).expected(EMPTY_JSON).build()
            )
            .complete();
   }

}