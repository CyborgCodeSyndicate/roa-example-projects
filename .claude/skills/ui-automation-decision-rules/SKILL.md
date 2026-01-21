---
name: ui-automation-decision-rules
description: |
  Guides UI automation decisions using Chrome DevTools MCP.
  Ensures stable locator selection, proper UI component modeling,
  correct service layering, and non-flaky end-to-end UI automation.
  Use whenever generating, reviewing, or refactoring UI automation code.
---

When working on **UI automation**, always follow these rules:

---

## 1. Always Explore the Application First (Mandatory)

Before writing or modifying any UI automation code:

- Use **Chrome DevTools MCP** to explore the application
- Inspect the DOM to understand structure and hierarchy
- Identify stable attributes suitable for automation
- Observe dynamic behavior (SPA rendering, modals, lazy loading)

**Never guess locators.**  
If MCP exploration is not possible:
- Inform the user explicitly
- Ask for confirmation or additional context
- Do not generate brittle selectors

---

## 2. Locator Strategy Rules

When defining UI locators, follow this strict priority order:

1. `By.id` (only if stable and unique)
2. Data attributes (`data-test-id`, `data-testid`, `data-cy`)
3. Accessible attributes (`aria-label`, `role`, `name`)
4. Semantic CSS selectors
5. XPath (ONLY when text matching or parent traversal is unavoidable)

### Stability Rules
- Avoid dynamic or framework-generated classes (e.g., Tailwind, hashed CSS)
- Avoid absolute DOM paths
- Prefer locators that survive UI layout changes

All locators must be:
- Verified via MCP
- Unique at runtime
- Documented implicitly through meaningful enum names

---

## 3. UI Component Modeling Rules

Model UI elements as **logical components**, not raw selectors.

### Granularity
- Group related elements by page area or responsibility
  - Examples: `LoginForm`, `HeaderNav`, `ProductTable`

### Element Type Mapping
Map HTML elements to framework field types consistently:

- `<button>`, `input[type='submit']` → `ButtonFields`
- `input[type='text']`, `input[type='password']` → `InputFields`
- `<select>` → `SelectFields`
- `<a>` → `LinkFields`

### Definition Rules
- Define locators in **enums**, never inline in tests
- Reuse existing elements before creating new ones
- Add lifecycle hooks only when explicitly required

---

## 4. Interaction & Validation Patterns

### Wait Strategy
- Always consider asynchronous behavior
- Use `SharedUi.WAIT_FOR_PRESENCE` or approved custom hooks
- Never rely on hard sleeps

### Validation Strategy
- Use **soft assertions** for non-critical checks
  - Example: `validate().validateTextInField(..., true)`
- Use **hard assertions** for flow-critical gates
  - Example: `validate().validateTextInField(..., false)`

Every UI test must include **at least one meaningful validation**.

---

## 5. Service Layering Rules

Separate concerns clearly using service layers:

### Layer Responsibilities
- **Low-Level Interactions**
  - Use `RING_OF_UI`
  - Raw element actions only (click, type, select)
- **Business Workflows**
  - Use custom service rings (e.g., `PurchaseService`)
  - Encapsulate business meaning, not UI mechanics

### Encapsulation Rule
- If a sequence contains **more than 3 UI interactions**,
  it must be extracted into a custom service ring method.

Tests should describe **what happens**, not **how it happens**.

---

## 6. End-to-End Flow Validation

For end-to-end UI tests:

- Correlate UI actions with **network activity** when applicable
- Validate that user actions trigger expected backend requests
- Align test flows with documented business behavior

UI automation must reflect **real user journeys**, not artificial paths.

---

## 7. Non-Negotiable Constraints

- No flaky tests
- No inline locators
- No hard-coded waits
- No duplicated UI logic
- No business logic inside test methods

Favor:
- Reusability
- Stability
- Readability
- Long-term maintainability
