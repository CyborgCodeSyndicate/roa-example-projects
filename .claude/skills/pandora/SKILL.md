---
name: Pandora
description: |
  This skill defines how the AI must discover and interpret the ROA framework surface via Pandora metadata.
Generate and use **ROA framework usage metadata** to:
- discover available ROA types/methods/fields
- understand intended usage via linked example snippets
- avoid guessing options, patterns, and contracts
---

## Inputs & Outputs

### Input (generator)
A Maven plugin goal that scans the ROA codebase and discovers available types, methods, and fields, exporting them as Pandora JSON metadata.

### Outputs (files)
1) **Pandora metadata JSON** (main index)
- project identifiers + a list of `types` describing ROA contracts and usage surface

Pandora entries can point to the relevant examples file via `exampleFilesPath`.

---

## How to run the Maven plugin

### Where to run
Run from the **project root**.

### Output location
The generated Pandora JSON is written under:
- `target/pandora/` (in the module where you run the command)

### Command
```bash
  mvn pandora:open -U "-DincludeTags=<TAG_LIST> -DexcludeTags=<TAG_LIST>"
```

### Tag filtering
- `-DincludeTags` (optional): comma-separated list of tags to **include** in the exported metadata.
  - Example: `-DincludeTags=framework,storage`

- `-DexcludeTags` (optional): comma-separated list of tags to **exclude** from the exported metadata.
  - Use this when you want “everything except X”.
  - Example: generate everything except UI:
    - `-DexcludeTags=ui`

### Notes
- If `includeTags` is omitted, the plugin exports all available tags (except those in `excludeTags`).
- If both are provided, the result is: (included) minus (excluded).

---

## Tag guidance (choose includeTags before generating pandora.json)

Tags are **capability switches**. You must choose them **before** you can see any types/methods in `pandora.json`.

### Primary tags (default for API tests)

* **`framework`** – Enables the ROA test runtime surface: **Quest/SuperQuest**, base test classes (BaseQuest/BaseQuestSequential), ring chaining/lifecycle, and the core annotations that drive setup/teardown and test execution flow. Use this whenever you write tests or extend ROA-style test execution patterns.

* **`assertion`** – Enables the ROA validation DSL: **Assertion**, **AssertionTypes**, **AssertionTarget**, and **AssertionResult**. Use this when tests validate status/body/header fields through the assertion builder patterns (instead of inventing custom validation logic).

* **`api`** – Enables everything required for API testing: endpoint contracts (Endpoint + endpoint enums), API execution (RestServiceFluent/RestService), API annotations (e.g., @API), and API-related helpers for storing/retrieving responses, authentication flows, hooks, and retries. Use this for any test that calls endpoints and validates API behavior.

**Preset: write API tests**

```bash
  mvn pandora:open -U -DincludeTags=api,framework,assertion
```

After generating: explore `types[]`, then follow `exampleFilesPath` and see the documented usage patterns.

---

### Secondary tags (add only when implementing/adjusting features)

These are category tags. Add them only when the task explicitly needs that feature area (or if the minimal preset is missing something).

* **`test-data`** – Test data injection and model registries (e.g., craftable models, static providers).
* **`storage`** – Storage namespaces/keys and extraction patterns (DataExtractor*, DataExtractors*).
* **`auth`** – Authentication contracts/clients and auth annotations/flows.
* **`hook`** – Class-level hooks (before/after) and hook flow contracts.
* **`precondition`** – Pre-test setup flows (journeys/pre-quest patterns).
* **`cleanup`** – Post-test cleanup flows (rippers/teardown contracts).
* **`retry`** – Polling/eventual consistency utilities.
* **`ring`** – Authoring/extending custom rings (custom fluent services).

**Preset: implement/extend framework/project features**

```bash
  mvn pandora:open -U -DincludeTags=api,framework,assertion,test-data,storage,auth,hook,precondition,cleanup,retry,ring
```

After generating: explore `types[]`, then follow `exampleFilesPath` and see the documented usage patterns.

---

### Missing metadata rule

Prefer a **small tag set** per run; if something is missing, re-run by adding **one** secondary tag, and if it’s still missing run once with **no `includeTags`** to discover the needed tag(s) and then re-run minimal.

---
### Exclude rule (optional):

Use `-DexcludeTags=<TAG_LIST>` to remove noisy/unneeded areas and keep `pandora.json` small (e.g., exclude `logging`, `ui`, or any tag you don’t want the AI to see); if both `includeTags` and `excludeTags` are provided, the output is **(included) minus (excluded)**.

---

## Pandora JSON: what it represents

### Top-level fields
- `groupId`, `artifactId`, `version`, `projectName`: identify which artifact the metadata describes
- `generatedAt`: generation timestamp
- `types[]`: the main list of described types/contracts

### `types[]` entries
Each entry represents a type/contract (class/interface/annotation/etc.) and can include:

- `id`: unique identifier (often fully-qualified; methods may include signatures)
- `description`: what it is / how to use it
- `tags`: tag labels exported from @Pandora(tags=...) and used by the generator for filtering via -DincludeTags / -DexcludeTags (same tag vocabulary).
- `creation`: how the type is intended to be obtained/constructed (e.g., PROVIDED, CONSTRUCTOR, ENUM_CONSTANT, BUILDER)
- `extra`: additional metadata as key/value pairs (structured hints exported by the generator, e.g. { "key": "...", "value": "..." }).
- `exampleFilesPath`: path to a usage examples JSON file relevant to this type
- `fields[]`: field-level metadata (name/type/description/options)
- `methods[]`: method-level metadata (signature, parameters, return type, varargs)

---

## `fields[]`: how to interpret fields (when present)
A field entry typically contains:
- `name`, `type`, `description`, `primitive`, `tags`
- `availableOptions` (when the field expects a known set of constants/enums)
- optional `extra`: additional metadata as key/value pairs (structured hints exported by the generator).

Rule:
- If `availableOptions` exists, prefer those exact constants instead of inventing values.

---

## `methods[]`: how to interpret methods (when present)
A method entry typically contains:
- `id` (often full signature), `description`, `tags`, `returnType`, `varArgs`
- `parameters[]` where each param has `index`, `name`, `type`, `description`, `primitive`

---

## Usage Examples JSON: structure & linking

### Structure
- top-level: `items[]`
- each item:
  - `id` (should match a Pandora `type.id` or method id/signature)
  - `summary`
  - `usages[]`

### `usages[]` entries
Each usage includes:
- `code`: copy-pasteable snippet
- `description`: what it demonstrates
- `level`: e.g. CORE / ADVANCED
- `contextHint`: when to use / when not to use

### Link between Pandora → usage examples
If a Pandora type includes `exampleFilesPath`, open that file and locate the matching `id` under `items[]`.

---

## Practical usage rules (to keep generated code correct)
1) Use Pandora for discovery (types/methods/fields).
2) Use `availableOptions` instead of guessing.
3) Prefer linked usage examples when available (`exampleFilesPath`).
4) If metadata is missing for a needed contract, re-run `mvn pandora-open -Dtags=...` with broader tags.