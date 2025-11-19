# roa-example-projects

End-to-end UI + API + DB test automation examples on top of ROA (Ring of Automation).

> Quick jump: if you already use ROA and want to run or write tests, go to  
> [7. Getting Started](#7-getting-started) → [7.4 Enable adapters on tests](#74-enable-adapters-on-tests) and [8. Writing Tests (step-by-step)](#8-writing-tests-step-by-step).

---

## Table of Contents

1. [Overview](#1-overview)  
2. [Core Concepts](#2-core-concepts)  
   - [Quest](#21-quest)  
   - [Rings](#22-rings)  
   - [Storage](#23-storage)  
   - [Annotations & Phases](#24-annotations--phases)  
3. [Module Metadata](#3-module-metadata)  
4. [Project Structure](#4-project-structure)  
5. [Features & Use Cases](#5-features--use-cases)  
6. [Architecture](#6-architecture)  
   - [Execution Model](#61-execution-model)  
   - [Test Flow](#62-test-flow)  
   - [Bootstrap & Runtime Behavior](#63-bootstrap--runtime-behavior)  
7. [Getting Started](#7-getting-started)  
8. [Writing Tests (step-by-step)](#8-writing-tests-step-by-step)  
9. [Storage Integration](#9-storage-integration)  
10. [UiElement Pattern & Component Services](#10-uielement-pattern--component-services)  
11. [Advanced Examples](#11-advanced-examples)  
12. [Adapter Configuration & Reporting](#12-adapter-configuration--reporting)  
13. [Troubleshooting](#13-troubleshooting)  
14. [Dependencies](#14-dependencies)  
15. [Author](#15-author)

---

## 1. Overview

This repository demonstrates how to build expressive, maintainable, and scalable test automation using the Ring of Automation (ROA) across multiple interfaces:

- UI (browser automation via a typed façade)
- API (fluent REST DSL)
- DB (fluent database queries and assertions)

You can use any single capability on its own, or combine UI + API + DB in the same test flow. The examples follow a unified, annotation-driven architecture that removes boilerplate, centralizes complex logic outside test bodies, and promotes reusable domain flows.

What you get:

- A typed, fluent UI façade (AppUiService) backed by Selenium + CDP.
- Fluent API & DB rings with assertion support.
- Custom domain rings (CustomService) to keep tests scenario-focused.
- Annotation-driven setup for capabilities, preconditions (journeys), data crafting, and cleanup (rippers).
- Per-test thread-local storage for safe data passing across phases and rings.
- Realistic examples you can copy, adapt, and extend for your own apps.

Who this is for:

- Test engineers who want readable tests without low-level WebDriver/REST/JDBC boilerplate, and need stable, fast, and reliable suites suitable for robust regression runs.
- Teams adopting ROA who want a practical multi-interface template.
- Users exploring storage, journeys, authentication helpers, retries, and cross-layer validation.

---

## 2. Core Concepts

This section is a conceptual map; later sections show concrete code.

### 2.1 Quest

`Quest` is the per-test execution context managed by ROA. It handles:

- registered rings (UI, API, DB, and Custom rings),
- per-test thread-local storage,
- soft/hard assertion aggregation,
- access to artifacts (e.g., WebDriver, HTTP client, DB connection).

How you get it (JUnit 5 + ROA):

- Extend `BaseQuest` (per-test `Quest`) or `BaseQuestSequential` (class-level `Quest`), or
- Use the ROA meta-annotation that enables Quest injection.

```java
class MyTests extends BaseQuest {

  @Test
  void sample(Quest quest) {
    // use quest here
  }
}
```

How you use it:

- `quest.use(RING_OF_UI | RING_OF_API | RING_OF_DB | RING_OF_CUSTOM)` – obtain a fluent service for a capability.
- `.drop()` – return from a fluent chain to `Quest` so you can switch rings.
- `.complete()` – assert collected soft assertions and finish the chain.

Validation patterns:

- `.validate(soft -> { ... })` — add soft assertions collected until `.complete()`.
- `.validate(() -> { ... })` — run immediate hard assertions.

Artifacts & storage helpers:

- `quest.artifact(RING, Type.class)` to access underlying tools.
- Helper functions like `retrieve(...)` provide type-safe reads from storage namespaces.

Lifecycle variants:

- `BaseQuest` – per-method `Quest` lifecycle (most common).
- `BaseQuestSequential` – class-level `Quest` shared by all tests in a class.

### 2.2 Rings

A Ring is a named capability (UI, API, DB, Custom…) that exposes a fluent DSL. Tests switch rings to access different capabilities while keeping code expressive and concerns separated.

Common rings used here:

- `RING_OF_UI` – AppUiService (browser UI)
- `RING_OF_API` – REST client fluent DSL
- `RING_OF_DB` – DB fluent DSL
- `RING_OF_CUSTOM` – CustomService (domain flows composed on top of other rings)
- `RING_OF_EVOLUTION` – encapsulated validations (example ring)

Example switching:

```java
quest
  .use(RING_OF_UI)
  .login(seller)
  .createOrder(order)
  .validateOrder(order)
  .drop()
  .use(RING_OF_API)
  .requestAndValidate(AppEndpoints.SOME_ENDPOINT, /* assertions */)
  .complete();
```

### 2.3 Storage

Each test has its own thread-local storage attached to `Quest`. Use it to pass data between:

- preconditions (journeys),
- fluent chains,
- hooks (DB hooks, rippers),
- interceptors, and
- the test body itself.

Typical namespaces:

- `StorageKeysUi.UI` UI namespace  — intercepted responses, values read from components.
- `StorageKeysDb.DB` DB namespace  — `QueryResponse` keyed by query enums.
- `StorageKeysApi.API` API namespace  — last responses keyed by endpoints; tokens and IDs between steps.
- `StorageKeysTest.PRE_ARGUMENTS` PRE_ARGUMENTS  — inputs/outputs of journeys and other pre-steps.

Reads (examples):

```java
// retrieving value from the API storage
GetUsersDto users = retrieve(StorageKeysApi.API, AppEndpoints.GET_ALL_USERS, Response.class)
    .getBody().as(GetUsersDto.class);

// retrieving value from the DB storage
QueryResponse resp = retrieve(StorageKeysDb.DB, AppQueries.QUERY_ORDER, QueryResponse.class);

// retrieving value from the PRE_ARGUMENTS storage
Order order = retrieve(StorageKeysTest.PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class);
```

### 2.4 Annotations & Phases

#### Class-level

| Annotation | Phase | Purpose |
|-----------|--------|---------|
| `@UI`     | Setup  | Enable UI ring, WebDriver & CDP integration. |
| `@API`    | Setup  | Enable API ring and REST client fluent DSL. |
| `@DB`     | Setup  | Enable DB ring and query/assertion support. |
| `@DbHook(when, type, arguments, order)` | Setup / Teardown | Run DB flows (e.g., initialize H2, seed data, cleanup). |

#### Method-level

| Annotation | Phase | Purpose |
|-----------|--------|---------|
| `@Craft(model)` | Data setup | Inject models produced by `DataCreator`. Supports `Late<T>` for runtime-dependent data. |
| `@Journey(value, journeyData, order)` | Preconditions | Reusable flows (login, order creation, DB checks) that run before the test. |
| `@JourneyData(DataCreator.Data)` | Preconditions | Bind specific data creators to a journey. |
| `@AuthenticateViaUi(credentials, type, cacheCredentials)` | Preconditions | Login via UI using `AppUiLogin` + credentials class. Optional session caching. |
| `@InterceptRequests(requestUrlSubStrings)` | Execution | Enable CDP interception for matching URLs; responses stored in UI storage. |
| `@StaticTestData(Provider)` | Data setup | Load static test data into storage from a provider class. |
| `@Ripper(targets)` | Teardown | Run cleanup flows from `DataCleaner` registry. |

You can read this as a mini lifecycle:

> **Hooks & capabilities** → **Preconditions** → **Data setup** → **Test body** → **Cleanup**

---

## 3. Module Metadata

This repo contains multiple example modules. For now, focus on these two:

- UI Complex Test Framework (ui + api + db example)
- API Test Framework (api-only example)

UI Complex Test Framework:

- name: `ui-complex-test-framework`
- groupId: `io.cyborgcode.roa.usage`
- artifactId: `ui-complex-test-framework`
- version: `1.0.0`
- parent: `io.cyborgcode.roa:roa-parent:1.1.4`

API Test Framework:

- name: `api-test-framework`
- groupId: `io.cyborgcode.roa.usage`
- artifactId: `api-test-framework`
- version: `1.0.0`
- parent: `io.cyborgcode.roa:roa-parent:1.1.1`

---

## 4. Project Structure

High level overview by module.

UI Complex Test Framework `ui-complex-test-framework`(conceptual structure):

- tests
  - `BasicToAdvancedFeaturesTest`
  - `AdvancedFeaturesTest`
  - `DataBaseTest`
- rings
  - `base/Rings` — maps logical rings to fluent implementations
  - `ui/AppUiService` — UI ring façade
  - `service/CustomService` — domain-level ring (login, order flows)
- ui
  - `ui/elements` — enums for fields/components (inputs, buttons, selects, links, tables)
  - `ui/model` — domain models (e.g., Seller, Order) with `@InsertionElement`
  - `ui/authentication` — credentials and login flows
  - `ui/interceptor` — requests interception registry
- data
  - `data/creator` — `DataCreator`, `DataCreatorFunctions`
  - `data/cleaner` — `DataCleaner`, `DataCleanerFunctions`
  - `data/extractor` — JSONPath extraction helpers
  - `data/test_data` — `Data`, `DataProperties`, `StaticData`
- db
  - `db/hooks` — `DbHookFlows`, `DbHookFunctions` (init, teardown)
  - `db/queries` — `AppQueries`
  - `db/extractors` — `DbResponsesJsonPaths`
- api
  - `api/AppEndpoints` — REST endpoints used in examples

API Test Framework `api-test-framework` (conceptual structure):

- tests
  - `GettingStartedTest`
  - `AdvancedExamplesTest`
  - `BasicToAdvancedEvolutionTest`
  - `RetryUntilExamplesTest`
- api
  - `api/AppEndpoints`
  - `api/authentication` — credentials and auth types
  - `dto/request`, `dto/response` — request/response DTOs
  - `extractors/ApiResponsesJsonPaths` — JSONPath registry
- service
  - `CustomService` — reusable flows on top of API ring
  - `EvolutionService` — encapsulated validations
- base
  - `Rings` — maps logical rings to fluent implementations
- data
    - `data/creator` — `DataCreator`, `DataCreatorFunctions`
    - `data/cleaner` — `DataCleaner`, `DataCleanerFunctions`
    - `data/extractor` — JSONPath extraction helpers
    - `data/test_data` — `Data`, `DataProperties`
- resources
  - `config-{dev,staging,prod}.properties`
  - `test_data-{dev,staging,prod}.properties`
  - `system.properties`

---

## 5. Features & Use Cases

High-level capabilities:

- Multi-interface testing — UI, REST API and DB within a single fluent chain.
- Annotation-driven configuration — class-level (& global) behavior defined declaratively.
- Thread-local storage — per-test data isolation, safe for parallel execution.
- Fluent, domain-centric DSL — tests read like scenarios, not scripts.
- Extensible design — plug in data creators, journeys, rippers, DB hooks, custom rings.

Concrete features (selected):

- UI: typed façade with `input()`, `button()`, `select()`, `table()`, `browser()`, `interceptor()`, `insertion()`, `validate()`.
- API: `request()`, `validate()`, `requestAndValidate(...)` with rich assertion types (status, headers, body); DTO mapping; JSONPath extractors.
- DB: query enums + JSONPath-based assertions; hooks for init/teardown.
- Data crafting – `DataCreator` factories produce strongly-typed models; `@InsertionElement` lets the framework auto-fill forms.
- Preconditions (Journeys) – small reusable flows that can be ordered, parameterized and share data via `PRE_ARGUMENTS`.
- Authentication helpers: `@AuthenticateViaUi`, `@AuthenticateViaApi` with session/token reuse.
- Late data and interception: build runtime-dependent data via intercepted responses.
- DB integration – DB hooks for H2 initialization, query enums + JSONPath-based assertions.
- Cleanup (Ripper) – `@Ripper` + `DataCleaner` ensure tests leave no residue.

Typical use cases:

- UI-first E2E with domain flows (login, create, validate, clean up).
- Cross-layer validation (UI creates, API verifies, DB asserts persisted state).
- Token-based API flows with chaining (login → reuse header → assert next call).
- Runtime-derived data - use intercepted responses to build `Late<T>` models (e.g., an order that uses values calculated by the backend).
- Safe, repeatable suites using hooks and rippers.

---

## 6. Architecture

### 6.1 Execution Model

On top of JUnit 5, the examples add:

1. Annotations — configure capabilities, preconditions, data, cleanup.
2. Test Phases — implicit flow from hooks → preconditions → data → execution → cleanup.
3. Storage — thread-local, per-test way to pass data between phases and rings.
4. Rings — service layers for UI, API, DB, and custom domain flows.

### 6.2 Test Flow

1. **Setup (class level)**
    - `@UI`, `@API`, `@DB` register adapters and create a `Quest`.
    - `@DbHook(when = BEFORE, ...)` executes DB-bootstrap flows (e.g., H2 init).
2. **Preconditions (method level)**
   - `@Journey` `@AuthenticateViaUi`, `@AuthenticateViaApi` run flows that might:
       - login,
       - create or prepare data,
       - execute DB queries or checks,
       - write results into `PRE_ARGUMENTS` storage.
3. **Data crafting**
   - `@Craft` injects strongly-typed data models produced by `DataCreator`.
   - `@StaticTestData` preloads constants.
4. **Execution**
    - The test body uses `quest.use(RING)` to interact with UI / API / DB / custom domain flows.
    - `.validate(...)` registers soft and hard assertions.
5. **Cleanup**
    - `@Ripper` uses `DataCleaner` to remove created entities.
    - `@DbHook(when = AFTER, ...)` can run DB teardown if configured.

### 6.37 Bootstrap & Runtime Behavior

#### Test Bootstrap & Extensions

- `@UI`, `@API`, `@DB` enable JUnit 5 extensions that:
    - create a `Quest` per test (with thread-local storage),
    - wire fluent services (UI, REST, DB),
    - integrate with Allure or other reporting,
    - process method-level annotations before and after tests.

#### Fluent Service Initialization

- `AppUiService` is a typed façade on top of core UI services and is constructed with a `SmartWebDriver` and `SuperQuest`.
- It offers short-hands:
    - `.input()`, `.button()`, `.select()`, `.table()`, `.browser()`, `.interceptor()`, `.insertion()`, `.validate()`.

- `CustomService` extends the base fluent chain and:
    - delegates UI operations to `RING_OF_UI`,
    - coordinates cross-ring logic (like using a session cookie in API calls),
    - writes / reads from storage as needed.

#### Authentication with Session Caching

- `@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)`:
    - runs the login flow via `AppUiLogin`,
    - can cache session state (cookies, local storage) to **reuse login** between tests using the same credentials.

#### Network Interception (CDP)

- `@InterceptRequests(requestUrlSubStrings = { RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH })`:
    - enables Chrome DevTools interception,
    - stores structured responses in the UI storage.
- `DataExtractorFunctions.responseBodyExtraction(...)`:
    - can strip prefixes (like `for(;;);`),
    - run JSONPath expressions,
    - return typed values that can feed `Late<T>` models or assertions.

#### Component Interaction Flow

For each interaction:

1. `before()` hook of the element enum runs (waits/synchronization).
2. The component-specific service performs the Selenium action.
3. `after()` hook runs (e.g., wait for loading, validations).
4. Optional data is stored in storage (e.g., dropdown options, table rows).

---

## 7. Getting Started

This section focuses on setting up the project for any app under test. Choose the adapters (UI, API, DB) you need — you can enable just one or all of them.

### 7.1 Prerequisites

- Java (as defined by the parent POM/toolchain)
- Maven
- Chrome/ChromeDriver if using UI interception (for UI flows)
- Application(s) under test reachable from your environment properties

### 7.2 Add dependencies (to your module)

Include the adapters you need:

```xml
<dependency>
  <groupId>io.cyborgcode.roa</groupId>
  <artifactId>ui-interactor-test-framework-adapter</artifactId>
</dependency>
<dependency>
  <groupId>io.cyborgcode.roa</groupId>
  <artifactId>api-interactor-test-framework-adapter</artifactId>
</dependency>
<dependency>
  <groupId>io.cyborgcode.roa</groupId>
  <artifactId>db-interactor-test-framework-adapter</artifactId>
</dependency>
```

### 7.3 Configure environment

Adapters use Owner configuration. A typical setup defines defaults via Maven profiles and lets you override via system properties.

- Profiles: `dev`, `staging`, `prod`
- Common properties:
  - UI: `ui.config.file=config-<env>`
  - API: `api.config.file=config-<env>`
  - DB: `db.config.file=config-<env>`
  - Framework: `framework.config.file=config-<env>`
  - Test data: `test.data.file=test_data-<env>`
- Defaults file per module: `src/main/resources/system.properties`

Naming convention for files in `src/main/resources`:

- `config-<env>.properties` and `test_data-<env>.properties`

Precedence of effective config values:

1. `-D` system properties  
2. Maven profile defaults  
3. `system.properties`  
4. Values inside the referenced property files

Examples (multi-module builds can add `-pl <module>`):

```bash
# Run tests with staging profile for a module
mvn -q -pl <your-module> -Pstaging test

# Run with dev profile
mvn -q -pl <your-module> -Pdev test

# Run a single test and override only test data to dev
mvn -q -pl <your-module> -Pprod \
 -Dtest=YourTestClass#yourTestMethod \
 -Dtest.data.file=test_data-dev test
```

Example API config (config-<env>.properties):

```properties
api.base.url=https://your-api.example.com
api.restassured.logging.enabled=true
api.restassured.logging.level=ALL
shorten.body=800
```

Example UI config (config-<env>.properties):

```properties
ui.base.url=https://your-ui.example.com/
browser.type=CHROME
headless=false
wait.duration.in.seconds=10
use.shadow.root=true
screenshot.on.passed.test=true
```

Example DB config (config-<env>.properties):

```properties
db.default.type=H2
db.full.connection.string=jdbc:h2:mem:AppDb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false

# For external DBs:
db.default.host=localhost
db.default.port=5432
db.default.name=appdb
db.default.username=app
db.default.password=secret
```

### 7.4 Enable adapters on tests

Annotate your test class and extend a Quest base class:

```java
@UI
@API
@DB
class MyTests extends BaseQuest { }
```

This initializes UI, API and DB rings. Add DB hooks as needed:

```java
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
class MyDbTests extends BaseQuest { }
```

### 7.5 Writing Simple Component Tests

The `GettingStartedTests` class demonstrates fundamental UI component interactions and serves as your starting point for writing tests. This section walks through a complete example that covers the most common UI operations you'll use in your tests.

#### Every UI test in this framework follows a consistent pattern:

1. **Test Setup**: Use `@Test()` annotation and inject `Quest quest` parameter
2. **Ring Activation**: Call `.use(RING_OF_UI)` to access UI component services
3. **Component Interactions**: Chain fluent method calls for UI operations
4. **Release Active Ring**: Call `.drop()` to release the current ring/service context
5. **Test Completion**: End with `.complete()`

```java
@Test()
void components_browserButtonInputLinkSelectAlert(Quest quest) {
  quest
        .use(RING_OF_UI)
        .browser().navigate(getUiConfig().baseUrl())
        .button().click(ButtonFields.SIGN_IN_BUTTON)
        .input().insert(InputFields.USERNAME_FIELD, "username")
        .input().insert(InputFields.PASSWORD_FIELD, "password")
        .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
        .browser().back()
        .link().click(LinkFields.TRANSFER_FUNDS_LINK)
        .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, LOAN_ACCOUNT)
        .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, CREDIT_CARD_ACCOUNT)
        .input().insert(InputFields.AMOUNT_FIELD, "100")
        .input().insert(InputFields.TF_DESCRIPTION_FIELD, TRANSFER_LOAN_TO_CREDIT_CARD)
        .button().click(ButtonFields.SUBMIT_BUTTON)
        .button().click(ButtonFields.SUBMIT_BUTTON)
        .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, SUCCESSFUL_TRANSFER_MESSAGE)
        .drop()
        .complete();
}
```

#### Each component service provides specialized methods for different UI element types:

| Service | Purpose | Common Methods | Example Usage |
|---------|---------|----------------|---------------|
| `browser()` | Page navigation and browser controls | `navigate()`, `back()`, `refresh()` | Navigate to URLs, browser history |
| `button()` | Button interactions | `click()`, `validateIsVisible()` | Click submit buttons, verify button states |
| `input()` | Text input operations | `insert()`, `clear()`, `getValue()` | Fill forms, clear fields, read values |
| `link()` | Hyperlink interactions | `click()`, `validateText()` | Navigate via links, verify link text |
| `select()` | Dropdown selections | `selectOption()`, `getSelectedOptions()` | Choose from dropdowns, verify selections |
| `alert()` | Message validation | `validateValue()`, `validateIsVisible()` | Verify success/error messages |

#### Instead of using raw CSS selectors or XPath expressions, the framework uses enum-based element definitions:

```java
// Instead of this (brittle):
driver.findElement(By.id("user_login")).sendKeys("username");

// Use this (maintainable):
.input().insert(InputFields.USERNAME_FIELD, "username")
```

#### **Benefits of Element Enums:**
- **Compile-time safety**: Typos in element names cause build failures, not runtime errors
- **IDE support**: Auto-completion and refactoring work seamlessly
- **Centralized maintenance**: Change a locator once in the enum, update all tests
- **Built-in synchronization**: Enums can include wait strategies for dynamic elements

#### The `Constants` class centralizes test data and expected values:

```java
// From Constants.java
public static final String LOAN_ACCOUNT = "Loan";
public static final String SUCCESSFUL_TRANSFER_MESSAGE = "You successfully submitted your transaction.";
```

**Why use Constants:**
- **Single source of truth**: Change expected text in one place
- **Readable tests**: `SUCCESSFUL_TRANSFER_MESSAGE` is clearer than a raw string
- **Refactoring support**: IDE can find all usages of a constant
- **Consistency**: Same values used across multiple tests

#### Quest Lifecycle Management

1. **`.use(RING_OF_UI)`**: Activates the UI service ring, making component services available
2. **Component operations**: Chain fluent method calls for your test scenario
3. **`.drop()`**: Releases the current ring (optional but recommended for clarity)
4. **`.complete()`**: Finalizes the test, triggers cleanup, and reports results

#### Getting Started Checklist

1. **Extend `BaseQuest`**: Your test class should extend this base class
2. **Add `@UI` annotation**: Enables UI testing capabilities at the class level
3. **Inject `Quest quest`**: Add this parameter to your test method
4. **Start with `.use(RING_OF_UI)`**: Activate UI component services
5. **Use element enums**: Reference `ButtonFields`, `InputFields`, etc. instead of raw locators
6. **Use constants**: Reference `Constants` class for test data and expected values
7. **End with `.complete()`**: Always finalize your test execution

### 7.6 Writing Simple API Tests

The `GettingStartedTest` class demonstrates fundamental API interactions and serves as your starting point for writing API tests.

#### Every API test in this framework follows a consistent pattern:

1. **Test Setup**: Use `@Test` and inject `Quest quest` parameter
2. **Ring Activation**: Call `.use(RING_OF_API)` to access the REST fluent DSL
3. **Request and Assertions**: Use `.requestAndValidate(endpoint[, body], assertions...)`
4. **Release Active Ring**: Optionally call `.drop()` if switching to another ring
5. **Test Completion**: End with `.complete()`

```java
@Test
@Regression
@Description("Shows GET with a query parameter via quest.use(RING_OF_API) + requestAndValidate; minimal status/header checks.")
void showsBasicGetWithQueryParamAndMinimalAssertions(Quest quest) {
  quest
        .use(RING_OF_API)
        .requestAndValidate(
              GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
              Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
              Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build()
        )
        .complete();
}
```

**Why this matters:**
- Uses `quest.use(RING_OF_API)` and a minimal `requestAndValidate(...)` call.
- Demonstrates query parameters and simple status/header assertions.
- Builds toward richer flows with DTO bodies and JSONPath body assertions.

**API Checklist:**
- **Extend `BaseQuest`** (per-method Quest; parallel at test method level) or **`BaseQuestSequential`** (class-level Quest; sequential across the class)
- **Annotate the class with `@API`** (enables the API ring and the REST client fluent DSL)
- **Inject `Quest quest`** as a parameter
- **Use `.use(RING_OF_API)`** to activate the API ring
- **Use `.requestAndValidate(...)`** with assertions on `STATUS`, `HEADER`, `BODY`
- **End with `.complete()`**

---

## 8. Writing Tests (step-by-step)

We follow the UI and API oriented progression.

### 8.1 Step 1 – First UI and API tests (manual steps)

```java
@Test
void manualLoginAndCreateOrder(Quest quest) {
  quest
      .use(RING_OF_UI)
      .browser().navigate(getUiConfig().baseUrl())
      .input().insert(InputFields.USERNAME_FIELD, "admin@example.com")
      .input().insert(InputFields.PASSWORD_FIELD, "admin")
      .button().click(ButtonFields.SIGN_IN_BUTTON)
      .button().click(ButtonFields.NEW_ORDER_BUTTON)
      .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
      .select().selectOption(SelectFields.LOCATION_DDL, "Store")
      .select().selectOption(SelectFields.PRODUCTS_DDL, "Strawberry Bun")
      .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
      .button().click(ButtonFields.PLACE_ORDER_BUTTON)
      .input().insert(InputFields.SEARCH_BAR_FIELD, "John Terry")
      .table().readTable(Tables.ORDERS)
      .table().validate(
            Tables.ORDERS,
            Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
      .complete();
}
```

**What this shows and why it matters:**

- You start a UI chain with `quest.use(RING_OF_UI)`, then perform readable, typed actions: navigate, type in inputs, click buttons, select dropdown options, read a table, and validate.
- All locators are centralized in enum registries like `InputFields`, `SelectFields`, and `ButtonFields`. This keeps tests stable when selectors change and documents the UI map in one place.
- Each interaction automatically applies element-specific waits via the enum’s `before/after` hooks, so you don’t litter tests with WebDriver boilerplate.
- Validations are gathered as soft assertions in the chain and executed on `.complete()`, making failures more informative.

**Variant: the same flow using externalized test-data properties (no hardcoded values):**

```java
@Test
void createOrderUsingTestDataProperties(Quest quest) {
  quest
      .use(RING_OF_UI)
      .browser().navigate(getUiConfig().baseUrl())
      .input().insert(InputFields.USERNAME_FIELD, Data.testData().sellerEmail())
      .input().insert(InputFields.PASSWORD_FIELD, Data.testData().sellerPassword())
      .button().click(ButtonFields.SIGN_IN_BUTTON)
      .button().click(ButtonFields.NEW_ORDER_BUTTON)
      .input().insert(InputFields.CUSTOMER_FIELD, Data.testData().customerName())
      .input().insert(InputFields.DETAILS_FIELD, Data.testData().customerDetails())
      .input().insert(InputFields.NUMBER_FIELD, Data.testData().phoneNumber())
      .select().selectOption(LOCATION_DDL, Data.testData().location())
      .select().selectOptions(PRODUCTS_DDL, Strategy.FIRST)
      .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
      .button().click(ButtonFields.PLACE_ORDER_BUTTON)
      .input().insert(InputFields.SEARCH_BAR_FIELD, Data.testData().customerName())
      .table().readTable(Tables.ORDERS)
      .table().validate(
          Tables.ORDERS,
          Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
      .complete();
}
```

#### API equivalent (a minimal GET):

```java
quest
      .use(RING_OF_API)
      .requestAndValidate(
        AppEndpoints.GET_ALL_USERS,
        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build())
      .complete();
```
**What this shows (basic scenario):**

- quest.use(RING_OF_API) activates the REST fluent DSL for making HTTP calls.
- requestAndValidate(AppEndpoints.GET_ALL_USERS, ...) sends a typed GET request to the endpoint.
- The assertion verifies STATUS == 200 — a quick smoke/heartbeat check that the API is reachable.
- .complete() finalizes the chain and flushes any pending soft assertions.

---

### 8.2 Step 2 – Move from script to domain flows (CustomService)

```java
@Test
void wrapLoginAndCreateOrderWithCustomService(Quest quest) {
  quest
    .use(RING_OF_CUSTOM)
    .login(seller)
    .createOrder(order)
    .validateOrder(order)
    .complete();
}
```

Under the hood, `CustomService` converts low‑level steps into business‑level verbs so tests read like scenarios. It delegates to UI/API rings, encapsulates waits/validations, and shares state via storage.

#### API example via CustomService:

```java
quest
     .use(RING_OF_CUSTOM)
     .loginUserAndAddSpecificHeader(login)
     .requestAndValidateGetAllUsers()
     .complete();
```
---

### 8.3 Step 3 – Centralize data with DataCreator and @Craft

Instead of building `Seller` and `Order` inline, we ask `DataCreator` to produce them.

```java
@Test
void createOrderUsingCraftAndCustomService(
  Quest quest,
  @Craft(model = DataCreator.Data.SELLER) Seller seller,
  @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      .use(RING_OF_CUSTOM)
      .login(seller)
      .createOrder(order)
      .validateOrder(order)
      .complete();
}
```

**How to add new creators**

To introduce a new crafted model, add an enum constant in `DataCreator.java` that maps to a factory method, and implement that method in `DataCreatorFunctions.java`. The enum names act as stable keys used by `@Craft(model = DataCreator.Data.YourEnum)`, while the functions centralize how objects are built (often using `Data.testData()` and sensible defaults). Keep factories small and deterministic; prefer composition over ad‑hoc randomization so tests remain reproducible. If your model is runtime‑dependent, expose a `Late<T>` creator alongside the regular one.

```java
// DataCreator.java
SPECIAL_ORDER(DataCreatorFunctions::createSpecialOrder);

// DataCreatorFunctions.java
public static Order createSpecialOrder() {
  return Order.builder()
      .customer(Data.testData().customerName())
      .location("Bakery")
      .product("Chocolate Cake")
      .build();
}
```

---
### 8.4 Step 4 – Journeys as reusable preconditions

Journeys encapsulate reusable flows that should run **before** the test body, such as default login or preparing orders.

```java
@Test
@Journey(value = Preconditions.Data.LOGIN_DEFAULT_PRECONDITION)
void preconditionNoData(
  Quest quest,
  @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      .use(RING_OF_CUSTOM)
      .createOrder(order)
      .validateOrder(order)
      .complete();
}
```

Journeys can also take data and be ordered:

```java
@Journey(
  value = Preconditions.Data.LOGIN_PRECONDITION,
  journeyData = {@JourneyData(DataCreator.Data.SELLER)},
  order = 1)
@Journey(
  value = Preconditions.Data.ORDER_PRECONDITION,
  journeyData = {@JourneyData(DataCreator.Data.ORDER)},
  order = 2)
@Test
void preconditionsWithDataOrdered(Quest quest) {

  quest
      .use(RING_OF_CUSTOM)
      .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class))
      .complete();
}
```

API journey example:

```java
@Journey(value = Preconditions.Data.CREATE_NEW_USER,
         journeyData = {@JourneyData(DataCreator.Data.USER_INTERMEDIATE)}, order = 1)
@Test
void apiPreconditionCreatesUser(Quest quest) {
  quest.use(RING_OF_API)
       .validate(() -> {
         CreatedUserDto dto = retrieve(StorageKeysApi.API, AppEndpoints.POST_CREATE_USER, Response.class)
             .getBody().as(CreatedUserDto.class);
         // assertions...
       })
       .complete();
}
```

**Adding a new journey**

Journeys live in `Preconditions.java` as enum constants that map to functions in `PreconditionFunctions.java`. Each journey is a `BiConsumer<SuperQuest, Object[]>` so it can both access rings (UI/API/DB) and receive optional input via `@JourneyData` (e.g., a `Seller` or `Order`). Add a new enum entry, point it to a function, and implement that function to perform setup steps (login, seed data, DB checks). If the journey produces outputs that the test should use later (like a created `Order`), save them into `StorageKeysTest.PRE_ARGUMENTS` so the test can `retrieve(...)` them. Use `order` to chain multiple journeys deterministically.

```java
// Preconditions.java
SPECIAL_LOGIN_PRECONDITION((SuperQuest quest, Object[] objects) -> loginUser(quest, (Seller) objects[0]));


// PreconditionFunctions.java
public static void loginUser(SuperQuest quest, Seller seller) {
   quest
       .use(RING_OF_CUSTOM)
       .login(seller);
}
```

---

### 8.5 Step 5 – Authentication helpers (UI and API)
Move login out of tests completely and treat it as a reusable “meta journey”.

```java
@Test
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
void authNoCache(
  Quest quest,
  @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {

  quest
      .use(RING_OF_CUSTOM)
      .createOrder(order)
      .validateOrder(order)
      .complete();
}
```

With session caching:

```java
@Test
@AuthenticateViaUi(
  credentials = AdminCredentials.class,
  type = AppUiLogin.class,
  cacheCredentials = true)
void authWithCache(
  Quest quest,
  @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      .use(RING_OF_CUSTOM)
      .createOrder(order)
      .validateOrder(order)
      .complete();
}
```

#### API authenticate example:

```java
@AuthenticateViaApi(credentials = AdminAuth.class, type = AppAuth.class)
@Test
void usesApiAuth(Quest quest) {
  quest.use(RING_OF_API)
       .requestAndValidate(AppEndpoints.POST_CREATE_USER, leaderUser,
            Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build())
       .complete();
}
```

This allows a suite to avoid repeated login for the same user, dramatically speeding up tests.
API authentication:

---

### 8.6 Step 6 – Intercept UI traffic & extract data

Enable interception with `@InterceptRequests` and then use `DataExtractorFunctions` + JSONPath to pull values out of responses:

```java
@Test
@InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
void extractFromTraffic(
            Quest quest,
            @Craft(model = DataCreator.Data.SELLER) Seller seller) {

   quest
         .use(RING_OF_CUSTOM)
         .login(seller)
         .drop()
         .use(RING_OF_UI)
         .validate(() -> Assertions.assertEquals(
               List.of("$197.54"),
               retrieve(
                     DataExtractorFunctions.responseBodyExtraction(
                           RequestsInterceptor.INTERCEPT_REQUEST_AUTH.getEndpointSubString(),
                           "$[0].changes[?(@.key=='totalPrice')].value",
                           "for(;;);"
                     ), List.class)))
         .complete();
}
```

Key points:

- Interceptor is configured by **URL substrings**.
- Responses are stored in UI storage.
- Prefixes like `for(;;);` can be stripped before parsing.
- Extracted data can drive further steps or be asserted directly.

---

### 8.7 Step 7 – DB validations with RING_OF_DB

Use the DB ring to execute parameterized queries (from `AppQueries`) and validate results with JSONPath‑based assertions. When a query runs, its last `QueryResponse` is stashed in the DB namespace of storage, keyed by the query enum. You can then compose assertions that target specific fields via `DbResponsesJsonPaths`, choose soft or hard checks, and keep DB verification in the same fluent chain. This makes it easy to confirm that UI/API actions truly persisted to the database.

```java
@Test
@Journey(
  value = Preconditions.Data.LOGIN_PRECONDITION,
  journeyData = {@JourneyData(DataCreator.Data.VALID_SELLER)})
void validateStoredOrderInDb(
  Quest quest,
  @Craft(model = DataCreator.Data.VALID_ORDER) Order order) {

  quest
      .use(RING_OF_CUSTOM)
      .validateOrder(order)
      .drop()
      .use(RING_OF_DB)
      .query(AppQueries.QUERY_ORDER.withParam("id", 1))
      .validate(
          retrieve(StorageKeysDb.DB, AppQueries.QUERY_ORDER, QueryResponse.class),
          Assertion.builder()
             .target(QUERY_RESULT)
             .key(DbResponsesJsonPaths.PRODUCT_BY_ID.getJsonPath(1))
             .type(CONTAINS_ALL)
             .expected(List.of(order.getProduct()))
             .soft(true)
             .build())
      .complete();
}
```

---

### 8.8 Step 8 – Cleanup with @Ripper and DataCleaner

Ensure created orders are cleaned up after tests finish:

```java
@Test
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class)
@Journey(value = Preconditions.Data.ORDER_PRECONDITION,
         journeyData = {@JourneyData(DataCreator.Data.ORDER)})
@Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
void cleanupCreatedOrders(Quest quest) {
  quest.use(RING_OF_CUSTOM)
      .validateOrder(retrieve(PRE_ARGUMENTS, DataCreator.ORDER, Order.class))
      .complete();
}
```

**Adding a new cleaner**

Add a new entry in `DataCleaner.java` that maps to a function in `DataCleanerFunctions.java`. A cleaner typically reads keys or models placed in `PRE_ARGUMENTS` during the test/journeys, then uses `RING_OF_DB` (or API/UI) to delete created records. Keep the logic idempotent and defensive so it’s safe to run even if partial data exists. Register the cleaner in tests with `@Ripper(targets = { ... })` to ensure suites remain isolated and repeatable.

```java
// DataCleaner.java
DELETE_SPECIAL_RECORDS(DataCleanerFunctions::deleteSpecialRecords);

// DataCleanerFunctions.java
public static void deleteSpecialRecords(SuperQuest quest) {
  // retrieve keys from PRE_ARGUMENTS or DB, then delete via RING_OF_DB
}
```

---

## 9. Storage Integration

Scope & thread-local design:

- Every test has its own storage instance tied to its executing thread; discarded on finish.
- Parallel execution is safe: data from test A cannot leak into test B.

Namespaces & usage (examples):

- UI: intercepted responses, values from components.
- DB: last `QueryResponse` per query enum.
- PRE_ARGUMENTS: input/output of journeys and preconditions.
- API: last `Response` per endpoint; tokens/IDs for chaining.

Read patterns:

```java
// Journey output
Order order = retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class);

// Static test data
String username = retrieve(staticTestData(StaticData.USERNAME), String.class);

// DB query result for the last run of a given query
QueryResponse resp = retrieve(StorageKeysDb.DB, AppQueries.QUERY_ORDER, QueryResponse.class);

// API response mapped to DTO
GetUsersDto users = retrieve(StorageKeysApi.API, AppEndpoints.GET_ALL_USERS, Response.class)
    .getBody().as(GetUsersDto.class);
```

Best practices:

- Use enums as keys for discoverability and type safety.
- Keep direct storage access inside rings/journeys/hooks.
- Avoid storing huge payloads unless required for assertions.
- Prefer helper methods (`retrieve`, `staticTestData`) over raw map access.

---

## 10. UiElement Pattern & Component Services

Element registries are enums implementing specific interfaces (e.g., `ButtonUiElement`) that encode the locator, component type, and synchronization strategy for each control. Using `ButtonFields` as an example, each constant defines a `By` locator, a `ButtonComponentType` via `ButtonFieldTypes`, and optional `before/after` hooks for robust timing. This keeps synchronization close to the element and avoids repeating waits across tests.

```java
public enum ButtonFields implements ButtonUiElement {
  SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE,
      SharedUi.WAIT_FOR_LOADING),
  NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE,
      SharedUi.WAIT_TO_BE_CLICKABLE, ButtonFields::waitForPresence),
  PLACE_ORDER_BUTTON(By.cssSelector("vaadin-button#save"), ButtonFieldTypes.VA_BUTTON_TYPE,
      SharedUi.WAIT_TO_BE_CLICKABLE, SharedUi.WAIT_TO_BE_REMOVED);
}

// Usage
quest.use(RING_OF_UI)
    .button().click(ButtonFields.SIGN_IN_BUTTON);
```

Why this pattern helps:

- Single source of truth for selectors and behavior (no magic strings in tests).
- `ButtonFieldTypes` binds each enum to the correct component implementation, enabling typed operations with adapter-specific behavior.
- Element‑level `before/after` hooks stabilize flows against async UI changes.
- The same approach applies to other element types (`InputFields`, `SelectFields`, `Tables`).

Mapping domain models to UI with `@InsertionElement`:

```java
@Data
public class Order {

  @InsertionElement(locatorClass = InputFields.class, elementEnum = "CUSTOMER_FIELD", order = 1)
  private String customer;

  @InsertionElement(locatorClass = SelectFields.class, elementEnum = "LOCATION_DDL", order = 2)
  private String location;

  @InsertionElement(locatorClass = SelectFields.class, elementEnum = "PRODUCTS_DDL", order = 3)
  private String product;
}

// Then
quest.use(RING_OF_UI)
    .insertion().insertData(order);
```

Component services via `AppUiService` (representative operations):

- input() — insert, clear, getValue, validateValue
- button() — click, validate visibility/enabled/hidden
- select() — selectOption(s), getSelectedOptions, getAvailableOptions, validateSelected
- table() — readTable, table-level assertions
- browser() — navigate, refresh, back, forward
- interceptor() — access intercepted requests/responses
- insertion() — insert annotated models via `@InsertionElement`
- validate() — run custom validation lambdas as part of the chain

---

## 11. Advanced Examples

### 11.1 Static test data preload

```java
@Test
@StaticTestData(StaticData.class)
void usesStaticData(Quest quest) {
  quest
      .use(RING_OF_CUSTOM)
      .validateOrder(retrieve(staticTestData(StaticData.ORDER), Order.class))
      .complete();
}
```

### 11.2 Late data creation based on intercepted responses

```java
@Test
@InterceptRequests(requestUrlSubStrings = {RequestsInterceptor.Data.INTERCEPT_REQUEST_AUTH})
void lateData(
  Quest quest,
  @Craft(model = DataCreator.Data.SELLER) Seller seller,
  @Craft(model = DataCreator.Data.ORDER) Order order,
  @Craft(model = DataCreator.Data.LATE_ORDER) Late<Order> lateOrder) {

  quest
      .use(RING_OF_CUSTOM)
      .loginUsingInsertion(seller)
      .createOrder(order).validateOrder(order)
      .createOrder(lateOrder.create()).validateOrder(lateOrder.create())
      .complete();
}
```

### 11.3 Validating tables with typed rows

```java
quest
    .use(RING_OF_UI)
    .input().insert(InputFields.SEARCH_BAR_FIELD, "John Terry")
    .table().readTable(Tables.ORDERS)
    .table().validate(
      Tables.ORDERS,
      Assertion.builder()
        .target(TABLE_VALUES)
        .type(TABLE_NOT_EMPTY)
        .expected(true)
        .soft(true)
        .build())
    .complete();
```

### 11.4 Full E2E: UI + API + DB + cleanup

```java
@UI
@API
@DB
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
@Test
@AuthenticateViaUi(credentials = AdminCredentials.class, type = AppUiLogin.class, cacheCredentials = true)
@Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
void fullE2E(Quest quest, @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      // Create via UI (Custom ring wraps UI flows)
      .use(RING_OF_CUSTOM)
      .createOrder(order)
      .validateOrder(order)
      .drop()

      // Call API with same logical session context if needed
      .use(RING_OF_API)
      .requestAndValidate(
        AppEndpoints.ENDPOINT_BAKERY,
        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build())
      .drop()

      // Validate persisted state in DB
      .use(RING_OF_DB)
      .query(AppQueries.QUERY_ORDER.withParam("id", 1))
      .validate(
        retrieve(StorageKeysDb.DB, AppQueries.QUERY_ORDER, QueryResponse.class),
        Assertion.builder()
          .target(QUERY_RESULT)
          .key(DbResponsesJsonPaths.PRODUCT_BY_ID.getJsonPath(1))
          .type(CONTAINS_ALL)
          .expected(List.of(order.getProduct()))
          .soft(true)
          .build())
      .complete();
}
```

---

## 12. Adapter Configuration & Reporting

Adapter configuration:

- UI, API and DB adapters provide Owner properties for base URLs, logging, retries, screenshots, and vendor-specific settings.
- See adapter READMEs for complete property lists.

Allure reporting (when on classpath):

- UI
  - step-level reporting for component operations,
  - optional screenshots on pass/fail,
  - attachments for intercepted traffic.
- API
  - request/response attachments (URL, method, headers, body),
  - status, duration,
  - validation target maps summarizing assertions.
- DB
  - executed SQL snapshots and timing,
  - row samples,
  - validation target maps for DB assertions.

---

## 13. Troubleshooting

UI

- Interception not working — ensure ChromeDriver matches Chrome; verify `@InterceptRequests` and URL substrings.
- Authentication flaky — ensure stable post-login locator; avoid transient elements; consider `cacheCredentials = true`.
- Elements not found — recheck locators in element enums; verify waits and `ui.base.url`.

API

- Base URL issues — check `api.base.url` and environment.
- JSONPath mismatches — verify paths in your JSONPath registry.
- 401/403 — confirm credentials and auth type when using `@AuthenticateViaApi`.
- Owner config not loaded — ensure property files exist on classpath; confirm profiles/system props.
- Excessive logs — tune `api.restassured.logging.enabled` and `api.restassured.logging.level`.
- Default headers — endpoints can set default headers; override/remove as needed for your API.

DB

- Hooks not running — confirm `@DbHook` configuration.
- Query mismatch — ensure query definitions match schema; verify JSONPath extractors.

---

## 14. Dependencies

Recommended adapters (include only what you use):

- `io.cyborgcode.roa:ui-interactor-test-framework-adapter`
- `io.cyborgcode.roa:api-interactor-test-framework-adapter`
- `io.cyborgcode.roa:db-interactor-test-framework-adapter`

Common extras used in some examples (optional, depending on your setup):

- `org.projectlombok:lombok`
- `com.fasterxml.jackson.core:jackson-databind`
- `com.h2database:h2`

---

## 15. Author

**Cyborg Code Syndicate 💍👨💻**
