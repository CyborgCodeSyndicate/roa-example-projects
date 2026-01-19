# roa-example-projects

End-to-end UI + API + DB test automation examples on top of ROA (Ring of Automation).

> Quick jump: if you are already familiar with ROA and just want to run the sample tests, go to:
> [Writing Simple UI Component Tests](#75-writing-simple-ui-component-tests) or [Writing Simple API Tests](#76-writing-simple-api-tests).

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
   - [High-level capabilities](#51-high-level-capabilities)
   - [Concrete features](#52-concrete-features)
   - [Typical use cases](#53-typical-use-cases)
6. [Architecture](#6-architecture)  
   - [Execution Model](#61-execution-model)  
   - [Test Flow](#62-test-flow)  
   - [Bootstrap & Runtime Behavior](#63-bootstrap--runtime-behavior)  
7. [Getting Started](#7-getting-started)
    - [Prerequisites](#71-prerequisites)
    - [Add dependencies (to your module)](#72-add-dependencies-to-your-module)
    - [Configure environment](#73-configure-environment)
    - [Enable adapters on tests](#74-enable-adapters-on-tests)
    - [Writing Simple UI Component Tests](#75-writing-simple-ui-component-tests)
    - [Writing Simple API Tests](#76-writing-simple-api-tests)
8. [Writing Tests (feature-by-feature)](#8-writing-tests-feature-by-feature)  
9. [Storage Integration](#9-storage-integration)
    - [Scope & thread-local design](#91-scope--thread-local-design)
    - [Namespaces & what goes where](#92-namespaces--what-goes-where)
    - [Write patterns](#93-write-patterns)
    - [Read patterns](#94-read-patterns)
    - [Best practices](#95-best-practices)  
10. [UiElement Pattern & Component Services](#10-uielement-pattern--component-services)  
11. [Table Testing Guide](#11-table-testing-guide)  
12. [Advanced Examples](#12-advanced-examples)  
13. [Adapter Configuration & Reporting](#13-adapter-configuration--reporting)  
14. [Troubleshooting](#14-troubleshooting)  
15. [Dependencies](#15-dependencies)  
16. [Author](#16-author)

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
- Users exploring storage, preconditions, authentication helpers, retries, and cross-layer validation.

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

**For a detailed explanation of Quest, see the relevant section in the ROA Libraries documentation: [Quest - The Big Idea](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#the-big-idea)**

### 2.2 Rings

A Ring is a named capability (UI, API, DB, Custom…) that exposes a fluent DSL. Tests switch rings to access different capabilities while keeping code expressive and concerns separated.

It represents the concrete fluent service implementation that backs Quest.use(Class). Tests switch between rings to access different testing capabilities. Rings keeps test code expressive while cleanly separating concerns between low-level HTTP, database interactions, UI operations and shared domain-specific actions.

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
   .input().insert(InputFields.ORDER_NUMBER, ID_VALUE)
   .button().click(ButtonFields.FIND_ORDER)
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

A dedicated section later [Storage Integration](#9-storage-integration) dives into details and best practices.

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
- UI Simple Test Framework (ui-only example)
- API Test Framework (api-only example)

UI Complex Test Framework:

- name: `ui-complex-test-framework`
- groupId: `io.cyborgcode.roa.usage`
- artifactId: `ui-complex-test-framework`
- version: `1.0.0`
- parent: `io.cyborgcode.roa:roa-parent:1.1.4`

UI Complex Test Framework:

- name: `ui-simple-test-framework`
- groupId: `io.cyborgcode.roa.usage`
- artifactId: `ui-simple-test-framework`
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

### UI Complex Test Framework `ui-complex-test-framework`(conceptual structure):

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

### UI Simple Test Framework `ui-simple-test-framework`(conceptual structure):

- tests
    - `GettingStartedTests`
    - `BasicToAdvancedFeatureTests`
    - `TableComponentExampleTests`
    - `AuthenticationViaUITests `
- rings
    - `base/Rings` — maps logical rings to fluent implementations
    - `ui/AppUiService` — UI ring façade
    - `service/PurchaseService` — domain-level ring (purchase flows)
- ui
    - `ui/elements` — enums for fields/components (inputs, buttons, selects, links, tables)
    - `ui/model` — domain models such as PurchaseForeignCurrency (annotated with `@InsertionElement`)
    - `ui/authentication` — credentials and login flows
    - `ui/components` — UI component type definitions
    - `ui/functions` — utility functions for UI operations
- data
    - `data/creator` — `DataCreator`, `DataCreatorFunctions`
    - `data/test_data` — `Data`, `DataProperties`, `Constants`

### API Test Framework `api-test-framework` (conceptual structure):

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

### 5.1 High-level capabilities:

- Multi-interface testing — UI, REST API and DB within a single fluent chain.
- Annotation-driven configuration — class-level (& global) behavior defined declaratively.
- Thread-local storage — per-test data isolation, safe for parallel execution.
- Fluent, domain-centric DSL — tests read like scenarios, not scripts.
- Extensible design — plug in data creators, journeys, rippers, DB hooks, custom rings.

### 5.2 Concrete features:

- UI: typed façade with `input()`, `button()`, `select()`, `table()`, `list()`, `link()`, `alert()`, `radio()`, `browser()`, `interceptor()`, `insertion()`, `validate()`.
- API: `request()`, `validate()`, `requestAndValidate(...)` with rich assertion types (status, headers, body); DTO mapping; JSONPath extractors.
- DB: query enums + JSONPath-based assertions; hooks for init/teardown.
- Data crafting – `DataCreator` factories produce strongly-typed models; `@InsertionElement` lets the framework auto-fill forms.
- Preconditions (Journeys) – small reusable flows that can be ordered, parameterized and share data via `PRE_ARGUMENTS`.
- Authentication helpers: `@AuthenticateViaUi`, `@AuthenticateViaApi` with session/token reuse.
- Late data and interception: build runtime-dependent data via intercepted responses.
- DB integration – DB hooks for H2 initialization, query enums + JSONPath-based assertions.
- Cleanup (Ripper) – `@Ripper` + `DataCleaner` ensure tests leave no residue.

### 5.3 Typical use cases:

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

### 6.3 Bootstrap & Runtime Behavior

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

**For a detailed explanation of each adapter (dependency) visit the relevant section in the ROA Libraries documentation: [ROA Libraries](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/README.md#modules-overview)**

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

### 7.5 Writing Simple UI Component Tests

The `GettingStartedTests` class demonstrates fundamental UI component interactions and serves as your starting point for writing tests. This section walks through a complete example that covers the most common UI operations you'll use in your tests.

#### Every UI test in this framework follows a consistent pattern:

1. **Test Setup**: Use `@Test()` annotation and inject `Quest quest` parameter
2. **Ring Activation**: Call `.use(RING_OF_UI)` to access UI component services
3. **Component Interactions**: Chain fluent method calls for UI operations
4. **Release Active Ring**: Optionally call `.drop()` to release the current ring/service context
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

#### Getting Started Checklist

1. **Extend `BaseQuest`** (per-method Quest; parallel at test method level) or **`BaseQuestSequential`** (class-level Quest; sequential across the class)
2. **Annotate the class with `@UI`** (enables UI testing capabilities at the class level)
3. **Inject `Quest quest`** as a parameter to your test method
4. **Start with `.use(RING_OF_UI)`** to activate the UI component services
5. **Use element enums** (reference `ButtonFields`, `InputFields`, etc. instead of raw locators)
6. **Use constants** (reference `Constants` class for test data and expected values)
7. **End with `.complete()`** (always finalize your test execution)

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
- **End with `.complete()`** (always finalize your test execution)

---

## 8. Writing Tests (feature-by-feature)

We follow the UI and API oriented progression.

### 8.1 Step 1 – First UI and API tests

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

Rather than keep login, order creation and validation logic in every test, we move it into CustomService and expose domain methods:

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

Under the hood, `CustomService` turns low‑level UI steps into business‑level verbs so your tests read like scenarios, not scripts. It:

- Delegates to `RING_OF_UI` for the granular work (navigate, input, click), but hides selectors and timing concerns.
- Encapsulates synchronization and validations so retries, waits, and checks live with the flow, not in every test.
- Shares and reuses state via `Quest` storage (e.g., saving the created order or session cookie for later API/DB steps).
- Promotes reuse: one place to adjust when the UI changes; all tests benefit instantly.
- Keeps the test body focused on intent: “login, create order, validate order.”

#### API example via CustomService:

```java
quest
     .use(RING_OF_CUSTOM)
     .loginUserAndAddSpecificHeader(login)
     .requestAndValidateGetAllUsers()
     .complete();
```
---

### 8.3 Step 3 – Centralize data with DataCreator and Craft

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
**For a more detailed explanation of `DataCreator` and `Craft`, visit the relevant section in the ROA Libraries documentation: [DataForge, Craft, Late & Dynamic Data Creation](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#dataforge-craft-late--dynamic-data-creation)**

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
**For a more detailed explanation of `Journeys``, visit the relevant section in the ROA Libraries documentation: [PreQuest, Journey & JourneyData System](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#prequest-journey--journeydata-system)**

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

### 8.8 Step 8 – Cleanup with DataCleaner and Ripper

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
**For a more detailed explanation of `DataCleaner` and `Ripper`, visit the relevant section in the ROA Libraries documentation: [Ripper, RipperMan & Data Cleanup](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#ripper-ripperman--data-cleanup)**

---

## 9. Storage Integration

This section complements [2.3 Storage](#23-storage) with more detail.

### 9.1 Scope & thread-local design

- Every test has its **own storage instance** tied to its executing thread.
- When the test finishes, its storage is discarded.
- Parallel execution is safe: data from test A cannot leak into test B.

Conceptually:

```text
Test Thread 1 → Storage #1
Test Thread 2 → Storage #2
...
```

### 9.2 Namespaces & what goes where

Recommended grouping:

- **UI namespace (`StorageKeysUi.UI`)**
    - intercepted responses,
    - values read from components (e.g., dropdown options).
- **API namespace (`StorageKeysApi.API`)**
    - api responses
- **DB namespace (`StorageKeysDb.DB`)**
    - `QueryResponse` objects keyed by `AppQueries` values.
- **PRE_ARGUMENTS (`StorageKeysTest.PRE_ARGUMENTS`)**
    - input/output of journeys and preconditions (e.g., created order IDs, pre-created orders).

### 9.3 Write patterns

Many writes happen automatically (e.g., some UI services stash options, interceptors stash responses).

Manual writes:

```java
quest.getStorage().put(MyKeys.USER_ID, "user123");
```

Journeys and DB hooks often write results into `PRE_ARGUMENTS` or `DB` namespaces.

### 9.4 Read patterns

Common examples:

```java
// Journey output retrieved from PRE_ARGUMENTS
Order order = retrieve(PRE_ARGUMENTS, DataCreator.VALID_ORDER, Order.class);

// Static test data retrieved from the configured static data store
String username = retrieve(staticTestData(StaticData.USERNAME), String.class);

// Last DB query execution result retrieved from the database storage
QueryResponse resp = retrieve(StorageKeysDb.DB, AppQueries.QUERY_ORDER, QueryResponse.class);

// UI input field editability state retrieved from the UI storage
Boolean enabled = retrieve(StorageKeysUi.UI, InputFields.USERNAME_FIELD, Boolean.class);

// API response mapped to DTO retrieved from the API storage
GetUsersDto users = retrieve(StorageKeysApi.API, AppEndpoints.GET_ALL_USERS, Response.class)
    .getBody().as(GetUsersDto.class);
```

### 9.5 Best practices

- Use **enums** as keys wherever possible for discoverability and type safety.
- Keep direct storage access inside **rings, journeys, hooks** – not scattered through tests.
- Avoid storing huge payloads unless you really need them for assertions.
- Prefer **helper methods** (`retrieve`, `staticTestData`) over raw map access to avoid casting errors.

**For a deeper explanation of Storage, visit the relevant section in the ROA Libraries documentation: [Storage & Data Extractors](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#storage--data-extractors)**

---

## 10. UiElement Pattern & Component Services

### 10.1 UiElement enums

Element registries are enums implementing specific interfaces (e.g., `ButtonUiElement`) that encode the locator, component type, and synchronization strategy for each control. Using `ButtonFields` as an example (see `ButtonFields.java`), each constant defines a `By` locator, a `ButtonComponentType` via `ButtonFieldTypes`, and optional `before/after` hooks for robust timing. This keeps synchronization close to the element and avoids repeating waits across tests.

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
- `ButtonFieldTypes` binds each enum to the correct component implementation, enabling typed operations (`click`, visibility checks) with adapter-specific behavior.
- Element‑level `before/after` hooks (e.g., wait to be clickable, wait for overlay to disappear) stabilize flows against async UI changes.
- The same approach applies to other element types (`InputFields`, `SelectFields`, `Tables`), producing a consistent, maintainable UI map.

### 10.2 Mapping domain models to UI with `@InsertionElement`

Annotate model fields with `@InsertionElement` to declare how each property maps to the UI (which enum registry and the order of operations). With this mapping in place, `insertion().insertData(model)` walks the fields and performs the right UI actions automatically (type, select, etc.). This keeps tests at the domain level and removes repetitive glue code. See `Order.java` for a complete example of mapping inputs and selects with execution order.

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
```

Then use:

```java
quest.use(RING_OF_UI)
    .insertion().insertData(order);
```
Full flow example using **insertion**:

```java
@Test
void createOrderUsingCraftAndInsertionFeatures(Quest quest,
     @Craft(model = DataCreator.Data.SELLER) Seller seller,
     @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      .use(RING_OF_UI)
      .browser().navigate(getUiConfig().baseUrl())
      .insertion().insertData(seller) // insertion: maps model fields to corresponding UI controls in one operation
      .button().click(ButtonFields.SIGN_IN_BUTTON)
      .button().click(ButtonFields.NEW_ORDER_BUTTON)
      .insertion().insertData(order)  // insertion: maps model fields to corresponding UI controls in one operation
      .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
      .button().click(ButtonFields.PLACE_ORDER_BUTTON)
      .input().insert(InputFields.SEARCH_BAR_FIELD, order.getCustomerName())
      .table().readTable(Tables.ORDERS)
      .table().validate(
          Tables.ORDERS,
          Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build())
      .complete();
}
```

### 10.3 Component services via `AppUiService`

`AppUiService` exposes typed component services so tests don’t need to think about low-level Selenium calls.

| Service        | Representative operations                                           |
|----------------|---------------------------------------------------------------------|
| `input()`      | `insert`, `clear`, `getValue`, `validateValue`                      |
| `button()`     | `click`, `validateIsVisible`, `validateIsEnabled/Hidden`           |
| `select()`     | `selectOption(s)`, `getSelectedOptions`, `getAvailableOptions`, `validateSelected` |
| `table()`      | `readTable`, `readRow` (if supported), table-level assertions       |
| `browser()`    | `navigate`, `refresh`, `back`, `forward`                            |
| `interceptor()`| access intercepted requests/responses                               |
| `insertion()`  | insert annotated models via `@InsertionElement`                     |
| `validate()`   | run custom validation lambdas as part of the chain                  |

---

## 11. Table Testing Guide

The `TableComponentExampleTests` class demonstrates comprehensive table testing capabilities in the ROA framework. This section provides practical examples for reading, validating, and interacting with tabular data in your UI tests.

### 11.1 Table Testing Overview

The framework provides powerful table operations through the `.table()` service, supporting:

- **Full table reading** - Load entire tables into typed objects
- **Selective column reading** - Read only specific columns you need
- **Row range reading** - Read subsets of rows by index range
- **Search-based row reading** - Find and read rows by criteria
- **Rich validation** - Multiple assertion types for comprehensive table validation
- **Cell-level interactions** - Insertion data or click elements within table cells

### 11.2 Complete Table Reading and Validation

The most comprehensive example shows reading an entire table and applying multiple validation types:

```java
@Test
@Description("Read entire table and validate using table assertion types")
@Regression
void readEntireTable_validateWithAssertionTypes(Quest quest) {
  quest
        .use(RING_OF_UI)
        // table(): entry point for table component interactions (read/validate/click)
        // readTable(table): reads the entire table into the framework's storage for later assertions
        .table().readTable(Tables.FILTERED_TRANSACTIONS)
        // table().validate(): fluent assertions targeting table values/elements using TableAssertionTypes
        .table().validate(
              Tables.FILTERED_TRANSACTIONS,
              Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(2).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(EVERY_ROW_CONTAINS_VALUES).expected(List.of(ONLINE_TRANSFER_REFERENCE)).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(TABLE_DOES_NOT_CONTAIN_ROW).expected(ROW_VALUES_NOT_CONTAINED).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(ALL_ROWS_ARE_UNIQUE).expected(true).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(NO_EMPTY_CELLS).expected(false).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(COLUMN_VALUES_ARE_UNIQUE).expected(1).soft(true).build(),
              Assertion.builder().target(TABLE_VALUES).type(TABLE_DATA_MATCHES_EXPECTED).expected(ONLINE_TRANSFERS_EXPECTED_TABLE).soft(true).build(),
              Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_ENABLED).expected(true).soft(true).build(),
              Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_CLICKABLE).expected(true).soft(true).build())
        // readRow(): narrows context to a single row by index for row-level assertions
        .table().readRow(Tables.FILTERED_TRANSACTIONS, 1)
        .table().validate(
              Tables.FILTERED_TRANSACTIONS,
              Assertion.builder().target(ROW_VALUES).type(ROW_NOT_EMPTY).expected(true).soft(true).build(),
              Assertion.builder().target(ROW_VALUES).type(ROW_CONTAINS_VALUES).expected(List.of(TRANSFER_DATE_1, ONLINE_TRANSFER_REFERENCE)).soft(true).build())
        .complete();
}
```

**Key Learning Points:**

- **Table Reading**: `.table().readTable(Tables.FILTERED_TRANSACTIONS)` loads the entire table
- **Multiple Validations**: Apply various assertion types in a single validation call
- **Row-Level Focus**: `.table().readRow(table, index)` for specific row validations
- **Soft Assertions**: All assertions use `.soft(true)` to collect all failures before reporting

### 11.3 Table Assertion Types

The framework provides comprehensive assertion types for different validation scenarios:

#### Content Validation
- `TABLE_NOT_EMPTY` - Ensures table has data
- `TABLE_ROW_COUNT` - Validates exact number of rows
- `NO_EMPTY_CELLS` - Checks for empty cells (expected false means empty cells are allowed)
- `TABLE_DATA_MATCHES_EXPECTED` - Compares entire table against expected data structure

#### Row and Column Validation
- `EVERY_ROW_CONTAINS_VALUES` - Ensures all rows contain specified values
- `ROW_CONTAINS_VALUES` - Validates specific row contains expected values
- `TABLE_DOES_NOT_CONTAIN_ROW` - Ensures table doesn't contain unwanted data
- `ALL_ROWS_ARE_UNIQUE` - Validates no duplicate rows
- `COLUMN_VALUES_ARE_UNIQUE` - Checks uniqueness within specific column

#### Element State Validation
- `ALL_CELLS_ENABLED` - Validates all table cells are enabled
- `ALL_CELLS_CLICKABLE` - Ensures all cells are clickable
- `ROW_NOT_EMPTY` - Validates specific row is not empty

### 11.4 Selective Column Reading

Read only the columns you need for more efficient testing:

```java
@Test
@Description("Read table with specific columns and validate target cell value")
@Regression
void readTableWithSpecifiedColumns_validateCell(Quest quest) {
  quest
        .use(RING_OF_UI)
        .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
        // readTable(table, columns...): reads specific columns from the table into the framework's storage
        .table().readTable(Tables.FILTERED_TRANSACTIONS, 
              TableField.of(FilteredTransactionEntry::setDescription),
              TableField.of(FilteredTransactionEntry::setWithdrawal))
        // validate(): uses a lambda for arbitrary assertions when a built-in assertion type isn't suitable
        .validate(() -> Assertions.assertEquals(
              "50",
              retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, TRANSACTION_DESCRIPTION_OFFICE_SUPPLY),
                    FilteredTransactionEntry.class).getWithdrawal().getText(),
              "Wrong deposit value")
        )
        .complete();
}
```

**Benefits of Selective Reading:**
- **Performance** - Only read data you need to validate
- **Memory efficiency** - Reduce storage footprint for large tables
- **Focused testing** - Clear intent about what data matters for the test
- **Type safety** - `TableField.of(FilteredTransactionEntry::setDescription)` provides compile-time checking

### 11.5 Row Range Reading

Read specific subsets of table rows by index range:

```java
@Test
@Description("Read table with start/end row range and validate target cell value")
@Regression
void readTableWithRowRange_validateCell(Quest quest) {
  quest
        .use(RING_OF_UI)
        .link().click(LinkFields.MY_MONEY_MAP_LINK)
        // readTable(table, start, end): reads a subset of rows (inclusive indices)
        .table().readTable(Tables.OUTFLOW, 3, 5)
        .validate(() -> Assertions.assertEquals(
              "$375.55",
              retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                    OutFlow.class).getAmount().getText(),
              "Wrong Amount")
        )
        .complete();
}
```

**Use Cases for Row Range Reading:**
- **Large tables** - Focus on specific sections without loading everything
- **Pagination testing** - Validate specific pages of data
- **Performance testing** - Test with controlled data sets
- **Boundary testing** - Validate first/last rows or middle sections

### 11.6 Combined: Specific Columns with Row Range

The most targeted approach combines row range with column selection:

```java
@Test
@Description("Read specific columns within row range and validate target cell value")
@Regression
void readTableSpecificColumnsWithRowRange_validateCell(Quest quest) {
  quest
        .use(RING_OF_UI)
        .link().click(LinkFields.MY_MONEY_MAP_LINK)
        // readTable(table, start, end, columns...): subset of rows with specific mapped columns
        .table().readTable(Tables.OUTFLOW, 3, 5, 
              TableField.of(OutFlow::setCategory),
              TableField.of(OutFlow::setAmount))
        .validate(() -> Assertions.assertEquals(
              "$375.55",
              retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                    OutFlow.class).getAmount().getText(),
              "Wrong Amount")
        )
        .complete();
}
```

**Maximum Efficiency**: This approach provides the most targeted data reading - only specific rows and columns.

### 11.7 Search-Based Row Reading

Find and read rows based on content criteria:

```java
@Test
@Description("Read a table row by search criteria and validate target cell value")
@Regression
void readTableRowBySearchCriteria_validateCell(Quest quest) {
  quest
        .use(RING_OF_UI)
        .link().click(LinkFields.MY_MONEY_MAP_LINK)
        // readRow(table, criteria): reads a row matching the provided search values
        .table().readRow(Tables.OUTFLOW, List.of(RETAIL))
        .validate(() -> Assertions.assertEquals(
              "$375.55",
              retrieve(tableRowExtractor(Tables.OUTFLOW),
                    OutFlow.class).getAmount().getText(),
              "Wrong Amount")
        )
        .complete();
}
```

**Search-Based Benefits:**
- **Dynamic data** - Find rows regardless of position changes
- **Content-driven** - Search by actual data values, not positions
- **Robust tests** - Tests survive table reordering or data changes
- **Business logic focus** - Find data by meaningful criteria

### 11.8 Data Retrieval Patterns

The framework provides consistent patterns for accessing table data after reading:

#### Basic Table Data Retrieval
```java
// Get all table data
List<FilteredTransactionEntry> allRows = 
  retrieve(StorageKeysUi.UI, Tables.FILTERED_TRANSACTIONS, List.class);
```

#### Search-Based Row Retrieval
```java
// Get specific row by search criteria
FilteredTransactionEntry specificRow = 
  retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, TRANSACTION_DESCRIPTION_OFFICE_SUPPLY),
    FilteredTransactionEntry.class);
```

#### Direct Row Access
```java
// Get row by index (after readRow operation)
OutFlow rowData = 
  retrieve(tableRowExtractor(Tables.OUTFLOW), OutFlow.class);
```

### 11.9 Table Testing Best Practices

Based on the framework's patterns:

1. **Setup First** - Always navigate and filter before reading tables
2. **Read Then Validate** - Separate data reading from validation for clarity
3. **Use Soft Assertions** - Collect multiple failures with `.soft(true)`
4. **Choose Right Reading Method** - Match reading approach to test needs:
    - Full table: Comprehensive validation
    - Column selection: Performance optimization
    - Row range: Large table handling
    - Search criteria: Dynamic data finding
5. **Leverage Constants** - Use `Constants` class values for expected data
6. **Type Safety** - Use `TableField.of()` with method references for compile-time safety
7. **Meaningful Assertions** - Choose assertion types that match your validation intent

### 11.10 Common Table Testing Scenarios

The framework supports these typical scenarios:

- **Data Verification**: Ensure search results match expected criteria
- **Content Validation**: Verify table contains/doesn't contain specific data
- **Structure Validation**: Check row counts, uniqueness, empty cells
- **Element State Testing**: Validate clickability and enabled states
- **Performance Testing**: Use selective reading for large datasets
- **Dynamic Content**: Use search-based reading for changing data positions

---

## 12. Advanced Examples

This section shows focused scenarios that combine the pieces introduced above.

### 12.1 Static test data preload

Use `@StaticTestData` to load shared constants for the whole test class:

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

Ideal for demo data or constants you don’t want to encode in property files.

**For a deeper explanation of `StaticTestData`, visit the relevant section in the ROA Libraries documentation: [StaticDataProvider - Preloading Test Data](https://github.com/CyborgCodeSyndicate/roa-libraries/blob/main/test-framework/README.md#staticdataprovider---preloading-test-data)**

---

### 12.2 Late data creation based on intercepted responses

`Late<T>` lets you build data after some runtime information is known.

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

The `LATE_ORDER` creator can use values extracted from intercepted responses to build a second order on the fly.

---

### 12.3 Validating tables with typed rows

Map a table to a typed row class, then use fluent table operations and storage:

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

Access the typed rows:

```java
quest
    .use(RING_OF_UI)
    .table().readTable(Tables.ORDERS)
    .drop()
    .use(RING_OF_UI)
    .validate(() -> {
         List<TableEntry> rows =
         (List<TableEntry>) DefaultStorage.retrieve(Tables.ORDERS, List.class);
       Assertions.assertFalse(rows.isEmpty());
       })
    .complete();
```

If your adapter version supports `readRow`:

```java
TableEntry first = quest.use(RING_OF_UI).table().readRow(Tables.ORDERS, 0);
```

---

### 12.4 Full E2E: UI + API + DB + cleanup

Combine everything into a single scenario:

```java
@UI
@API
@DB
@DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
// Adapters and hooks registered on class

@Test
@AuthenticateViaUi(
  credentials = AdminCredentials.class,
  type = AppUiLogin.class,
  cacheCredentials = true)
@Ripper(targets = {DataCleaner.Data.DELETE_CREATED_ORDERS})
void fullE2E(
  Quest quest,
  @Craft(model = DataCreator.Data.ORDER) Order order) {

  quest
      // Create via UI (Custom ring wraps UI flows)
      .use(RING_OF_CUSTOM)
      .createOrder(order)
      .validateOrder(order)
      .drop()

      // Reuse session cookie for API validation
      .use(RING_OF_API)
      .requestAndValidate(
        AppEndpoints.ENDPOINT_BAKERY.withHeader("Cookie", CustomService.getJsessionCookie()),
        Assertion.builder()
          .target(STATUS)
          .type(IS)
          .expected(HttpStatus.SC_OK)
          .build())
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

This recipe showcases:

- multi-ring composition,
- session reuse between UI and API,
- DB verification,
- full cleanup via `@Ripper`.

---

## 13. Adapter Configuration & Reporting

### 13.1 Adapter configuration

This module does not define new Owner keys; it **reuses** configuration from:

- `ui-interactor-test-framework-adapter`,
- `api-interactor-test-framework-adapter`,
- `db-interactor-test-framework-adapter`.

### 13.2 Allure reporting

When Allure is on the classpath, ROA adapters typically provide:

- **UI**
    - step-level reporting for each component operation,
    - optional screenshots on pass/fail,
    - attachments for intercepted traffic when enabled.

- **API**
    - request/response attachments:
    - URL, method, headers, body, status, duration,
    - validation target maps summarizing assertions.

- **DB**
    - executed SQL snapshots and timing,
    - row samples,
    - validation target maps for DB assertions.
---

## 14. Troubleshooting

**Interception not working**

- Ensure your ChromeDriver version matches installed Chrome.
- Check that `RequestsInterceptor` URL substrings match actual network calls.
- Verify that `@InterceptRequests` is present on the test (or class).

**Authentication is flaky**

- Confirm `AppUiLogin.successfulLoginElementLocator()` points to a stable element available after login.
- Avoid depending on transient UI elements (like toasts) for login success.
- Use `cacheCredentials = true` for long-running suites.

**Elements not found**

- Double-check locators in `InputFields`, `ButtonFields`, etc.
- Make sure `SharedUi` `before`/`after` waits are suitable for your app.
- Verify that `ui.base.url` is correct and your app is reachable.

**DB assertions fail unexpectedly**

- Validate that `DbHookFlows.Data.INITIALIZE_H2` (or your hook) is actually running.
- Check that the query definition in `AppQueries` matches your schema.
- Inspect the JSONPath in `DbResponsesJsonPaths` and compare with the actual query result.

**Owner config not loaded**

- Confirm that property files exist on the test classpath.
- Check system property overrides (`ui.config.file`, `api.config.file`, `db.config.file`).
- Run with `-X` or additional logging if necessary to verify effective config values.

---

## 15. Dependencies

From this module’s POM:

- `io.cyborgcode.roa:ui-interactor-test-framework-adapter`
- `io.cyborgcode.roa:api-interactor-test-framework-adapter`
- `io.cyborgcode.roa:db-interactor-test-framework-adapter`
- `org.projectlombok:lombok`
- `com.fasterxml.jackson.core:jackson-databind`
- `com.h2database:h2`

---

## 16. Author

**Cyborg Code Syndicate 💍👨💻**
