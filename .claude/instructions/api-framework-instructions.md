# **api-framework-instructions.md**

## API Testing with ROA Framework
The ROA framework provides a fluent API for REST API testing with declarative validation, automatic storage, 
and seamless integration with UI and database testing.

### Test Class Structure
**Class Annotations**
* Use `@API` annotation on each API test class
* Use `@DisplayName("...")` for descriptive test organization

**Class Inheritance**
* Extend `BaseQuest` for standard API test execution
* Extend `BaseQuestSequential` when tests must execute sequentially

### Test Method Structure
**Quest Object**
* Every API test method must accept a `Quest` parameter as the first argument
* The `Quest` object orchestrates all API operations via fluent API
* Never instantiate Quest manually; let the framework inject it

**Service Ring Management**
* Use `.use(RING_OF_API)` to activate the API service ring
* Use `.drop()` when switching to other rings if your project has multiple different services
* Always drop before switching contexts

**Test Completion**
* Always end test methods with `.complete()` to finalize quest execution

### API Endpoint Definition
**Endpoint Structure**
* Define all API endpoints in a dedicated enum class implementing `Endpoint<T>` interface
* Each endpoint constant must specify HTTP method (`Method.GET`, `Method.POST`, etc.) and relative URL path
* Endpoints integrate with the ROA fluent API for request execution and validation
* Use enum-based definitions to keep endpoints centralized, consistent, and discoverable

**Required Methods**
* Implement `method()` to return the HTTP method for the endpoint
* Implement `url()` to return the relative URL path
* Implement `enumImpl()` to return the enum constant itself
* Override `defaultConfiguration()` to apply shared request specifications (content type, headers, base URL)
* Override `headers()` to define default headers applied to all requests for that endpoint

**Default Configuration**
* Use `defaultConfiguration()` to set common request attributes (content type, authentication, base headers)
* Apply `ContentType.JSON` or other content types as needed
* Add common headers via spec.header("key", "value")
* Configuration applies to all requests using that endpoint unless overridden

**Header Management**
* Define default headers in headers() method returning Map<String, List<String>>
* Common headers include Content-Type, Accept, Authorization
* Headers are automatically applied during request execution
* Use `.withHeader()` in tests to add or override headers per request

**Endpoint Organization**
* Group endpoints by functional domain or API version
* Use descriptive enum constant names (e.g., `ENDPOINT_LOGIN`, `ENDPOINT_CREATE_USER`, `ENDPOINT_GET_ORDERS`)
* Maintain one endpoint enum per API service or microservice
* Keep endpoint definitions separate from test logic

**Endpoint Usage**
* Reference endpoints in tests via enum constants (e.g., `AppEndpoints.ENDPOINT_LOGIN`)
* Use `.withParam()`, `.withPathParam()`, `.withQueryParam()`, `.withHeader()` to customize requests
* Framework automatically applies default configuration and headers
* No manual URL construction or method specification in tests

### Request Execution
**Making Requests**
* Use `.request(ENDPOINT)` to execute a request without immediate validation
* Use `.request(ENDPOINT, requestBody)` to execute with a request body
* Use `.requestAndValidate(ENDPOINT, Assertion...)` for inline validation
* Responses are automatically stored in StorageKeysApi.API

**Request Customization**
* Use `.withQueryParam("key", value)` to add query parameters
* Use `.withPathParam("key", value)` to substitute path variables
* Use `.withHeader("key", value)` to add or override headers
* Chain multiple customizations fluently

### Response Validation
**Declarative Assertions**
* Use `Assertion.builder()` for declarative validation
* Target `STATUS` for status code validation
* Target `HEADER` for response header validation
* Target `BODY` for response body validation using JsonPath

**Assertion Types**
* Use `IS, NOT, CONTAINS, GREATER_THAN, LESS_THAN` for value comparisons
* Use `STARTS_WITH, ENDS_WITH, MATCHES_REGEX` for string operations
* Use `NOT_NULL, NOT_EMPTY, ALL_NOT_NULL` for null/empty checks
* Use `CONTAINS_ALL, CONTAINS_ANY, LENGTH` for collection operations

**Soft Assertions**
* Use `.soft(true)` in assertion builder for soft assertions
* Soft assertions collect all failures before reporting
* Useful for validating multiple related fields

### Storage and Retrieval
**Automatic Storage**
* All API responses are automatically stored in StorageKeysApi.API
* Storage key is the endpoint reference used in the request
* Responses persist across quest steps within the same test

**Retrieving Responses**
* Use `retrieve(StorageKeysApi.API, ENDPOINT, Response.class)` to get stored responses
* Map response to DTOs using `.getBody().as(DtoClass.class)`
* Extract values via `.getBody().jsonPath().getString(PATH)`
* Use stored responses in subsequent requests or validations

### Authentication
**@AuthenticateViaApi**
* Use `@AuthenticateViaApi(credentials = CredentialsClass.class, type = AuthClass.class)` for automatic API authentication
* Define credentials in dedicated classes (e.g., `AdminCredentials`, `UserCredentials`)
* Implement authentication logic in separate auth classes (e.g., `AppAuth`)
* Authentication executes before test method starts
* Auth tokens are automatically stored and available for use

### Test Data Management
**@Craft for Request Bodies**
* Use `@Craft(model = DataCreator.Data.MODEL_NAME)` to inject API request DTOs.
* Pass crafted models directly as request bodies in `.request()` or `.requestAndValidate()`.
* Use `Late<@Craft>` for lazy instantiation when the request body depends on data produced earlier in the quest.

### Preconditions
**Journey with API Preconditions**
* Use `@Journey` to perform API or mixed (API/UI/DB) precondition flows before the main test execution.
* Define API-related preconditions in the Preconditions enum.
* Add constants as strings in Preconditions enum Data class for usage in @Journey annotations.
* Validate required API state (e.g., existing user, token, configuration) before test execution.
* Combine API journeys with other module journeys using the order attribute if your project has multiple modules.

### API-Level Cleanup
**@Ripper for API/Data Cleanup**
* Use `@Ripper` to specify cleanup operations related to API-created data (entities, tokens, temp resources).
* Define cleanup targets in `DataCleaner` enum.
* Add constants as strings in DataCleaner enum Data class for usage in `@Ripper` annotations.
* Cleanup executes after test completion (success or failure).
* Ensures isolation of API test data and prevents pollution of the target system across runs.

**JsonPath Extractors**
* Define JsonPath constants in dedicated classes (e.g., `ApiResponsesJsonPaths`)
* Use enum-based JsonPath definitions for reusability
* Access nested JSON values via .getJsonPath() methods

**Configuration**
* Use `getApiConfig().baseUrl()` for environment-specific API URLs
* Store API credentials in test_data-{env}.properties

**Validation Requirements**
* Provide at least one validation in each API test
* Use framework assertion builders for declarative validation
* Use soft assertions (`.soft(true)`) when validating multiple related fields
* Use `.validate(() -> {})` for custom validation logic with JUnit assertions