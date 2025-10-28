package io.cyborgcode.ui.complex.test.framework.db;

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
