package io.cyborgcode.ui.complex.test.framework.db.extractors;

/**
 * Registry of JsonPath expressions for extracting data from database query responses.
 *
 * <p>Each enum constant represents a JsonPath expression used to extract specific fields from
 * database query results stored as JSON. The expressions support both direct indexed access (e.g.,
 * {@code $[0].EMAIL}) and filtered access by ID (e.g., {@code $[?(@.ID == 1)].EMAIL}).
 *
 * <p>The {@link #getJsonPath(Object...)} method enables dynamic path construction by formatting
 * placeholders with runtime arguments, allowing tests to query specific rows or filter by criteria.
 *
 * <p>These paths integrate with ROA database validation framework to assert on query results in a
 * readable, maintainable way:
 *
 * <pre>{@code
 * Assertion.builder()
 *     .target(QUERY_RESULT)
 *     .key(DbResponsesJsonPaths.EMAIL.getJsonPath(0))
 *     .type(EQUALS)
 *     .expected("admin@vaadin.com")
 *     .build()
 * }</pre>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DbResponsesJsonPaths {
   DELETED("$[%d].updatedRows"),
   EMAIL("$[%d].EMAIL"),
   EMAIL_BY_ID("$[?(@.ID == %d)].EMAIL"),
   PASSWORD("$[%d].PASSWORD"),
   PASSWORD_BY_ID("$[?(@.ID == %d)].PASSWORD"),
   PRODUCT("$[%d].PRODUCT"),
   PRODUCT_BY_ID("$[?(@.ID == %d)].PRODUCT"),
   LOCATION("$[%d].LOCATION"),
   LOCATION_BY_ID("$[?(@.ID == %d)].LOCATION");

   private final String jsonPath;

   DbResponsesJsonPaths(String jsonPath) {
      this.jsonPath = jsonPath;
   }

   /**
    * Returns the JSON path.
    *
    * <p>If the path contains a placeholder (like %d), you can supply the arguments
    * (e.g., an index) to format the path correctly.
    *
    * @param args optional arguments to format the JSON path
    * @return the formatted JSON path as a String
    */
   public String getJsonPath(Object... args) {
      if (args != null && args.length > 0) {
         return String.format(jsonPath, args);
      }
      return jsonPath;
   }
}
