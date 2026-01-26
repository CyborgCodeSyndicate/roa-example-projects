# **api-test-examples.md**

## **Overview**
This document provides comprehensive examples of API testing using the ROA (Ring of Abstraction) test framework. 
The examples demonstrate core patterns, assertions, data flow, authentication mechanisms, and service orchestration 
that form the foundation of API test development in this framework.

## **Reference Test Class Structure**
``
Every API test file should follow this template to ensure the AI has context for imports and annotations.
``

```java
package io.cyborgcode.api.test.framework;

// Core Framework Imports
import io.cyborgcode.roa.api.annotations.API;
import io.cyborgcode.roa.api.annotations.AuthenticateViaApi;
import io.cyborgcode.roa.api.storage.StorageKeysApi;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.roa.framework.annotation.*; // Journey, Ripper, Craft, Regression, etc.
import io.cyborgcode.roa.framework.parameters.Late;

// Data & DTO Imports
import io.cyborgcode.api.test.framework.api.dto.request.*;
import io.cyborgcode.api.test.framework.api.dto.response.*;
import io.cyborgcode.api.test.framework.data.creator.DataCreator;
import io.cyborgcode.api.test.framework.data.cleaner.DataCleaner;
import io.cyborgcode.api.test.framework.preconditions.Preconditions;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.time.Instant;

// Static Constants (Essential for readability)
import static io.cyborgcode.api.test.framework.api.AppEndpoints.*;
import static io.cyborgcode.api.test.framework.api.extractors.ApiResponsesJsonPaths.*;
import static io.cyborgcode.api.test.framework.base.Rings.*;
import static io.cyborgcode.roa.api.validator.RestAssertionTarget.*;
import static io.cyborgcode.roa.validator.core.AssertionTypes.*;
import static org.apache.http.HttpStatus.*;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@API
class ExampleApiTest extends BaseQuest {
    // Tests go here...
}
```
## **Test Examples**
``Each numbered example section contains:``
**Example title:** One line summarizing the scenario (e.g. “Comprehensive Assertions – Multiple Validation Types”).
**Short description:** 1–2 sentences explaining what the test demonstrates (assertions, chaining, storage, Craft/Late,
auth, rings, journeys, ripper, etc.).
**Code snippet:** Full Java test method with annotations and DSL usage.
**Key Patterns:** Bullet list of what the example teaches (e.g. when to use requestAndValidate, how to use retrieve,
how to chain rings, how Craft/Late works, how tokens/journeys/rippers integrate).

### **1. Comprehensive Assertions - Multiple Validation Types**
**Description:** Demonstrates the full range of assertion capabilities in a single GET request, including status code validation, header checks, numeric comparisons, string operations, regex matching, null checks, and collection assertions.

```java
@Test
@Smoke
@Regression
@Description("Showcases many assertion types in one GET: status/headers, numeric comparisons, regex, contains(all/any), length, null checks...")
void showsComprehensiveAssertionsOnUsersList(Quest quest) {
   quest
         .use(RING_OF_API)
         .requestAndValidate(
               GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
               Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
               Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
               Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(NOT).expected(1).build(),
               Assertion.builder().target(BODY).key(TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
               Assertion.builder().target(BODY).key(PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
               Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(CONTAINS).expected(SUPPORT_URL_REQRES_FRAGMENT).build(),
               Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected(SUPPORT_TEXT_PREFIX).build(),
               Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
               Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
               Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
               Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
               Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(7).build(),
               Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(LENGTH).expected(6).build(),
               Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected(SUPPORT_URL_REGEX).build(),
               Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected(USER_ONE_FIRST_NAME).build(),
               Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(BETWEEN).expected(TOTAL_USERS_IN_PAGE_RANGE).build(),
               Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ALL).expected(PAGE_TWO_EXPECTED_USERS).build(),
               Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ANY).expected(PAGE_TWO_CONTAINS_ANY_USER).build()
         )
         .complete();
}
```
### Key Patterns:
``
Use requestAndValidate() for inline assertion validation
Chain multiple Assertion.builder() calls for comprehensive validation
Assertions support STATUS, HEADER, and BODY targets
JsonPath extraction via .getJsonPath() methods for body assertions
``

### **2. Separated Request and Validation - Plain JUnit Assertions**
**Description:** Shows how to separate request execution from validation, retrieve stored responses, 
and use standard JUnit assertions instead of the framework's DSL.

```java
java
@Test
@Smoke
@Regression
@Description("Uses request() + validate(): make a request, retrieve the stored response, and assert with plain JUnit (no DSL).")
void showsRequestThenValidateWithPlainJUnitAssertions(Quest quest) {
   quest
         .use(RING_OF_API)
         // Make the request only; response is stored under StorageKeysApi.API
         .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
         .validate(() -> {
            // Retrieve stored response and map to DTO
            GetUsersDto users =
                  retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                        .getBody().as(GetUsersDto.class);

            // Plain JUnit assertions
            assertEquals(PAGE_TWO_DATA_SIZE, users.getData().size(), USER_DATA_SIZE_INCORRECT);
            assertEquals(USER_SEVENTH_FIRST_NAME_LENGTH,
                  users.getData().get(0).getFirstName().length(), FIRST_NAME_LENGTH_INCORRECT);
         });
}
```
### Key Patterns:
``
Use request() to execute without immediate validation
Responses are automatically stored in StorageKeysApi.API
Retrieve via retrieve(StorageKeysApi.API, ENDPOINT, Response.class)
Map response to DTOs using .getBody().as(DtoClass.class)
``

### **3. Chained Requests with Storage Reuse**
**Description:** Demonstrates how to chain multiple API calls where data from the first response is used as input for 
subsequent requests.

```java
java
@Test
@Smoke
@Regression
@Description("Chains requests: read an id from the stored list response and reuse it as a path param in the next call.")
void showsChainedRequestsAndUsingStorage(Quest quest) {
   quest
         .use(RING_OF_API)
         .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
         .requestAndValidate(
               GET_USER.withPathParam(
                     ID_PARAM,
                     retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                           .getBody().as(GetUsersDto.class)
                           .getData().get(0).getId()
               ),
               Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
         )
         .complete();
}
```
### Key Patterns:
``
Chain requests using fluent API
Extract dynamic values from previous responses via retrieve()
Use .withPathParam() or .withQueryParam() to add dynamic parameters
Storage is request-specific: use endpoint reference to retrieve correct response
``

### **4. Complex Data Filtering with Soft Assertions**
**Description:** Shows how to retrieve stored responses, filter collections, find specific entities, and validate using AssertJ soft assertions.

```java
@Test
@Smoke
@Regression
@Description("Reads the list from storage, finds a user by first name, and validates the full user with soft assertions.")
void showsSoftAssertionsAfterFindingUserByFirstNameFromStorage(Quest quest) {
   quest
         .use(RING_OF_API)
         .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
         .request(
               GET_USER.withPathParam(
                     ID_PARAM,
                     retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                           .getBody()
                           .as(GetUsersDto.class)
                           .getData()
                           .stream()
                           .filter(user -> USER_NINE_FIRST_NAME.equals(user.getFirstName()))
                           .map(UserData::getId)
                           .findFirst()
                           .orElseThrow(() -> new RuntimeException(userWithFirstNameNotFound(USER_NINE_FIRST_NAME))))
         )
         .validate(softAssertions -> {
            UserDto user =
                  retrieve(StorageKeysApi.API, GET_USER, Response.class)
                        .getBody().as(UserDto.class);
            softAssertions.assertThat(user.getData().getId()).isEqualTo(USER_NINE_ID);
            softAssertions.assertThat(user.getData().getEmail()).isEqualTo(USER_NINE_EMAIL);
            softAssertions.assertThat(user.getData().getFirstName()).isEqualTo(USER_NINE_FIRST_NAME);
            softAssertions.assertThat(user.getData().getLastName()).isEqualTo(USER_NINE_LAST_NAME);
         })
         .complete();
}
```
### Key Patterns:
``
Use Java Streams to filter and find specific entities from collections
Throw descriptive exceptions when required data is not found
Use validate(softAssertions -> {}) for multiple related assertions
Soft assertions collect all failures before reporting
``

### **5. Eager @Craft Model with Soft Assertion**
**Description:** Demonstrates using the @Craft annotation to inject pre-configured test data models that are materialized eagerly (at test initialization).

```java
@Test
@Smoke
@Regression
@Description("Creates a user from an eager @Craft model and validates with a soft assertion.")
void showsCraftModelAsRequestWithSoftAssertion(Quest quest,
                                               @Craft(model = DataCreator.Data.USER_LEADER) CreateUserDto leaderUser) {
   quest
         .use(RING_OF_API)
         .requestAndValidate(
               POST_CREATE_USER,
               leaderUser,
               Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
               Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath())
                     .type(IS).expected(USER_LEADER_NAME).soft(true).build()
         )
         .complete();
}
```
### Key Patterns:
``
Use @Craft(model = DataCreator.Data.MODEL_NAME) to inject test data
Craft models are created before test execution (eager)
Pass the model directly as request body
Use .soft(true) in assertions to enable soft assertion mode
``

### **6. Lazy Late<@Craft> Model Materialization**
**Description:** Shows the use of Late<@Craft> for lazy initialization where 
the model is created only when create() is explicitly called.

```java
@Test
@Smoke
@Regression
@Description("Combines a GET and POST where the POST body is built lazily via Late<@Craft> (materialized only when create() is called).")
void showsLateCraftModelMaterializedOnDemand(Quest quest,
                                             @Craft(model = DataCreator.Data.USER_JUNIOR)
                                             Late<CreateUserDto> juniorUser) {
   quest
         .use(RING_OF_API)
         .requestAndValidate(
               GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
               Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
         )
         .requestAndValidate(
               POST_CREATE_USER,
               juniorUser.create(), // Late<@Craft> is instantiated here on demand
               Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
         )
         .complete();
}
```
### Key Patterns:
``
Wrap @Craft models with Late<> for lazy initialization
Call .create() to materialize the model at the point of use
Useful when model creation depends on runtime data or execution order
``

### **7. Mixed Eager and Lazy Craft Models**
**Description:** Demonstrates combining both eager and lazy model creation in a 
single test to handle different initialization timing needs.

```java
@Test
@Smoke
@Regression
@Description("Mixes eager @Craft and lazy Late<@Craft>: eager model is ready up-front; the Late model is instantiated via create() at use time.")
void showsMixOfCraftAndLateCraftInChainedFlow(Quest quest,
                                              @Craft(model = DataCreator.Data.USER_LEADER)
                                              CreateUserDto leaderUser,
                                              @Craft(model = DataCreator.Data.USER_SENIOR)
                                              Late<CreateUserDto> seniorUser) {
   quest
         .use(RING_OF_API)
         // @Craft model (leaderUser) is already materialized
         .requestAndValidate(
               POST_CREATE_USER,
               leaderUser,
               Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
               Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath())
                     .type(IS).expected(USER_LEADER_NAME).soft(true).build(),
               Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath())
                     .type(IS).expected(USER_LEADER_JOB).soft(true).build()
         )
         // Late<@Craft> is created on-demand at this point
         .requestAndValidate(
               POST_CREATE_USER,
               seniorUser.create(),
               Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
               Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath())
                     .type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
               Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath())
                     .type(IS).expected(USER_SENIOR_JOB).soft(true).build()
         )
         .complete();
}
```
### Key Patterns:
``
Declare both eager and Late<> models in test parameters
Use eager models when data is static or doesn't depend on test execution
Use Late<> when models need runtime data or ordered execution
``

### **8. Token Storage and Header Reuse**
**Description:** Shows authentication flow where a token is retrieved from a login response and reused as a header in subsequent requests.

```java
@Test
@Smoke
@Regression
@Description("Logs in using @Craft credentials, stores the token, and reuses it as a header in the next GET call.")
void showsTokenReuseFromStorageAcrossRequests(Quest quest,
                                              @Craft(model = DataCreator.Data.LOGIN_ADMIN_USER)
                                              LoginDto loginAdminUser) {
   quest
         .use(RING_OF_API)
         .request(POST_LOGIN_USER, loginAdminUser)
         .requestAndValidate(
               GET_USER.withPathParam(ID_PARAM, ID_THREE)
                     .withHeader(
                           EXAMPLE_HEADER,
                           retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
                                 .getBody().jsonPath().getString(TOKEN.getJsonPath())
                     ),
               Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
         )
         .complete();
}
```
### Key Patterns:
``
Extract values using .jsonPath().getString(PATH) for simple extractions
Use .withHeader(HEADER_NAME, value) to add dynamic headers
Store authentication tokens from login responses for reuse
``

### **9. Full Lifecycle with Auth, Journey, and Ripper**
**Description:** Demonstrates the complete test lifecycle with automatic authentication, 
precondition setup, test execution, and cleanup.

```java
@Test
@Smoke
@Regression
@AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
@Journey(
      value = Preconditions.Data.CREATE_NEW_USER,
      journeyData = {@JourneyData(DataCreator.Data.USER_LEADER)},
      order = 1
)
@Journey(
      value = Preconditions.Data.CREATE_NEW_USER,
      journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)},
      order = 2
)
@Ripper(targets = {DataCleaner.Data.DELETE_ADMIN_USER})
@Description("Executes a full user lifecycle using AuthenticateViaApi for auth, Journey preconditions for setup, and Ripper for cleanup.")
void showsAuthenticateViaApiWithJourneySetupAndRipperCleanup(Quest quest) {
   quest
         .use(RING_OF_API)
         .validate(() -> {
            CreatedUserDto createdUser =
                  retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                        .getBody().as(CreatedUserDto.class);
            assertEquals(USER_INTERMEDIATE_NAME, createdUser.getName(), CREATED_USER_NAME_INCORRECT);
            assertEquals(USER_INTERMEDIATE_JOB, createdUser.getJob(), CREATED_USER_JOB_INCORRECT);
            assertTrue(createdUser.getCreatedAt()
                  .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)));
         })
         .complete();
}
```
### Key Patterns:
``
Use @AuthenticateViaApi to handle authentication before test execution
Use @Journey with order attribute for sequential precondition execution
Use @Ripper to specify cleanup operations that run after test completion
Access Journey-created data from storage using standard retrieve() methods
``

### **10. Switching Between Service Rings**
**Description:** Shows how to switch between different service layers (rings) for 
specialized operations and then return to standard API operations.

```java
@Test
@Smoke
@Regression
@Description("Demonstrates switching rings (services): use a custom ring to log in + header wiring, then return to RING_OF_API for the GET.")
void showsSwitchingRingsForLoginThenApiCall(Quest quest,
                                            @Craft(model = DataCreator.Data.LOGIN_ADMIN_USER)
                                            LoginDto loginAdminUser) {
   quest
         .use(RING_OF_CUSTOM)
         .loginUserAndAddSpecificHeader(loginAdminUser)
         .drop()
         .use(RING_OF_API)
         .requestAndValidate(
               GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
               Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
         )
         .complete();
}
```
### Key Patterns:
``
Use .use(RING_NAME) to activate a specific service ring
Use .drop() to release the current ring
Switch between rings to access specialized functionality
Custom rings encapsulate complex multi-step operations
``

### **11. Simplified Tests with Custom Service Rings**
**Description:** Demonstrates how custom service rings can encapsulate complex validation 
logic to create cleaner, more maintainable tests.

```java
@Test
@Smoke
@Regression
@Description("Shows how a custom service ring can slim down tests by encapsulating the full validation flow.")
void showsSlimmerTestsViaCustomServiceRing(Quest quest,
                                           @Craft(model = DataCreator.Data.LOGIN_ADMIN_USER)
                                           LoginDto loginAdminUser) {
   quest
         .use(RING_OF_CUSTOM)
         .loginUserAndAddSpecificHeader(loginAdminUser)
         .requestAndValidateGetAllUsers()
         .complete();
}
```
### Key Patterns:
``
Create custom ring methods that encapsulate common workflows
Combine authentication, request, and validation in single methods
Improve test readability by hiding implementation details

### Core Framework Patterns - DO's
**Request Execution**
✅ DO use requestAndValidate() for simple requests with immediate validation
✅ DO use request() followed by validate() when you need custom validation logic
✅ DO chain multiple requests in a fluent manner
✅ DO use retrieve() to access stored responses from previous requests
✅ DO always call .complete() at the end of the quest chain

**Assertions**
✅ DO use the Assertion DSL for declarative validation
✅ DO leverage soft assertions when validating multiple related fields
✅ DO use appropriate assertion types (IS, CONTAINS, GREATER_THAN, etc.)
✅ DO validate STATUS, HEADER, and BODY targets as needed
✅ DO use JsonPath extractors for nested JSON validation

**Test Data Management**
✅ DO use @Craft annotations for test data injection
✅ DO use Late<@Craft> for models that need lazy initialization
✅ DO define test data models in DataCreator.Data enum
✅ DO store commonly used test constants in dedicated constant classes
✅ DO use descriptive model names (USER_LEADER, USER_INTERMEDIATE, etc.)

**Authentication & Authorization**
✅ DO use @AuthenticateViaApi for automatic authentication
✅ DO store tokens/credentials for reuse across requests
✅ DO extract auth tokens from login responses and pass them as headers
✅ DO define authentication classes (AdminAuth, AppAuth) in the api/authentication package

**Test Lifecycle Management**
✅ DO use @Journey annotations for precondition setup
✅ DO use the order attribute to control Journey execution sequence
✅ DO use @Ripper annotations for automatic test data cleanup
✅ DO retrieve Journey-created resources from storage for validation

**Service Rings**
✅ DO use RING_OF_API for standard REST API operations
✅ DO create custom rings (RING_OF_CUSTOM) for specialized workflows
✅ DO use .drop() to release a ring before switching
✅ DO encapsulate complex multi-step operations in custom ring methods

**Error Handling**
✅ DO throw descriptive RuntimeExceptions when data is not found
✅ DO use helper methods for error message generation
✅ DO provide context in exception messages (e.g., which entity, what field)

**Test Organization**
✅ DO use @Description annotations to explain test purpose
✅ DO apply appropriate test tags (@Smoke, @Regression, @Craft)
✅ DO follow naming convention: showsXxxWhenYyy or validatesXxxForYyy
✅ DO extend BaseQuest for all API test classes
✅ DO mark test classes with @API annotation

### Core Framework Patterns - DON'Ts
**Request Execution**
❌ DON'T make API calls outside the quest chain
❌ DON'T use RestAssured directly; always use the quest DSL
❌ DON'T forget to call .complete() at the end of the quest
❌ DON'T store responses in local variables; use the framework storage
❌ DON'T mix synchronous and asynchronous request patterns

**Assertions**
❌ DON'T hardcode expected values; use constants from constant classes
❌ DON'T mix JUnit assertions with framework assertions in the same validation block
❌ DON'T create overly complex JsonPath expressions; extract to constants
❌ DON'T ignore assertion failures; ensure all validations are meaningful
❌ DON'T use string concatenation for JsonPath; use the JsonPath enum methods

**Test Data Management**
❌ DON'T create test data inline in test methods
❌ DON'T use Late<> for models that don't need lazy initialization
❌ DON'T create duplicate Craft models; reuse existing ones
❌ DON'T hardcode magic numbers or strings; define them as constants
❌ DON'T modify Craft models after injection; they should be immutable

**Authentication & Authorization**
❌ DON'T hardcode credentials in test methods
❌ DON'T share authentication state across unrelated tests
❌ DON'T skip authentication when testing secured endpoints
❌ DON'T use invalid auth tokens for positive test scenarios

**Test Lifecycle Management**
❌ DON'T perform manual setup/teardown when Journey/Ripper can handle it
❌ DON'T create data dependencies between unrelated tests
❌ DON'T skip cleanup; always use @Ripper for created resources
❌ DON'T use shared test data without proper isolation

**Service Rings**
❌ DON'T activate multiple rings simultaneously without dropping
❌ DON'T create custom rings for single-use operations
❌ DON'T bypass the ring system with direct API calls
❌ DON'T create circular dependencies between ring methods

**Error Handling**
❌ DON'T use generic exception messages
❌ DON'T swallow exceptions without proper logging
❌ DON'T use empty catch blocks
❌ DON'T catch exceptions unless you can handle them meaningfully

**Test Organization**
❌ DON'T create overly long test methods (keep under 30 lines)
❌ DON'T test multiple unrelated scenarios in one test
❌ DON'T skip test documentation (@Description)
❌ DON'T use unclear test method names
❌ DON'T mix API and UI testing logic in the same test class

| Assertion Type | Description | Example Usage |
|----------------|-------------|---------------|
| `IS` | Exact equality check | `type(IS).expected(SC_OK)` |
| `NOT` | Negation check | `type(NOT).expected(1)` |
| `CONTAINS` | String/collection contains | `type(CONTAINS).expected("reqres")` |
| `CONTAINS_ALL` | Collection contains all elements | `type(CONTAINS_ALL).expected(expectedList)` |
| `CONTAINS_ANY` | Collection contains any element | `type(CONTAINS_ANY).expected(candidateList)` |
| `STARTS_WITH` | String starts with prefix | `type(STARTS_WITH).expected("To support")` |
| `ENDS_WITH` | String ends with suffix | `type(ENDS_WITH).expected(".jpg")` |
| `MATCHES_REGEX` | Regex pattern match | `type(MATCHES_REGEX).expected("https?://.*")` |
| `EQUALS_IGNORE_CASE` | Case-insensitive equality | `type(EQUALS_IGNORE_CASE).expected("Michael")` |
| `GREATER_THAN` | Numeric comparison | `type(GREATER_THAN).expected(1)` |
| `LESS_THAN` | Numeric comparison | `type(LESS_THAN).expected(10)` |
| `BETWEEN` | Range validation | `type(BETWEEN).expected(rangeArray)` |
| `LENGTH` | String/collection size | `type(LENGTH).expected(7)` |
| `NOT_NULL` | Null check | `type(NOT_NULL).expected(true)` |
| `ALL_NOT_NULL` | All collection items not null | `type(ALL_NOT_NULL).expected(true)` |
| `NOT_EMPTY` | Empty check | `type(NOT_EMPTY).expected(true)` |