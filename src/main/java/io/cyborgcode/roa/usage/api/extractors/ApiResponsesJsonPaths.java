package io.cyborgcode.roa.usage.api.extractors;

/**
 * Central registry of JSON path expressions used across the tutorial tests.
 * <p>
 * Encapsulates commonly accessed response fields (users, resources, support block,
 * tokens, errors, etc.) so that JSON paths are:
 * <ul>
 *   <li>type-safe and discoverable via enum constants,</li>
 *   <li>reused consistently across tests,</li>
 *   <li>easy to update in one place if the API contract changes.</li>
 * </ul>
 * Supports indexed paths via {@link #getJsonPath(Object...)} for list-based responses.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum ApiResponsesJsonPaths {

   // --- General / Root Keys ---
   TOTAL("total"),
   TOTAL_PAGES("total_pages"),
   PER_PAGE("per_page"),

   // --- Support Information ---
   SUPPORT_URL("support.url"),
   SUPPORT_TEXT("support.text"),

   // --- Data Container ---
   DATA("data"),

   // --- User List (Indexed Fields) ---
   USER_ID("data[%d].id"),
   USER_FIRST_NAME("data[%d].first_name"),
   USER_AVATAR_BY_INDEX("data[%d].avatar"),

   // --- Single User (Non-Indexed Fields) ---
   SINGLE_USER_EMAIL_EXPLICIT("data.email"),

   // --- Create User Response Fields ---
   NAME_FROM_RESPONSE("name"),
   JOB_FROM_RESPONSE("job"),
   USER_ID_FROM_RESPONSE("id"),

   // --- Miscellaneous ---
   TOKEN("token"),
   ERROR("error");

   private final String jsonPath;

   ApiResponsesJsonPaths(String jsonPath) {
      this.jsonPath = jsonPath;
   }

   /**
    * Returns the JSON path.
    * If the path contains placeholders (e.g., %d, %s), supply the arguments to format the string.
    *
    * @param args Optional arguments for formatting the JSON path.
    * @return The formatted JSON path.
    */
   public String getJsonPath(Object... args) {
      if (args != null && args.length > 0) {
         return String.format(jsonPath, args);
      }
      return jsonPath;
   }

}
