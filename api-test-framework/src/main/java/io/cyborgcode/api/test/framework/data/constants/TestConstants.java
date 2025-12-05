package io.cyborgcode.api.test.framework.data.constants;

import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * Canonical expected values used across the Reqres-based examples.
 * <p>
 * Encapsulates:
 * <ul>
 *    <li>pagination settings and ranges,</li>
 *    <li>support metadata and URLs,</li>
 *    <li>user identities and roles used in assertions,</li>
 *    <li>resource metadata (colors, years, ids),</li>
 *    <li>login error messages and token patterns.</li>
 * </ul>
 * These constants document the assumptions about the public Reqres API
 * that the tutorial tests assert against.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UtilityClass
public class TestConstants {

   private static final class Keys {
      private Keys() {
      }

      private static final String ID = "id";
      private static final String EMAIL = "email";
      private static final String FIRST_NAME = "first_name";
      private static final String LAST_NAME = "last_name";
      private static final String AVATAR = "avatar";
   }

   private static final class Urls {
      private Urls() {
      }

      private static final String AVATAR_BASE = "https://reqres.in/img/faces/";
      private static final String AVATAR_SUFFIX = "-image" + FileConstants.AVATAR_FILE_EXTENSION;

      private static String avatar(int id) {
         return AVATAR_BASE + id + AVATAR_SUFFIX;
      }
   }

   public static class Pagination {
      private Pagination() {
      }

      public static final int PAGE_TWO = 2;
      public static final List<Integer> TOTAL_USERS_IN_PAGE_RANGE = List.of(5, 15);
   }

   public static class Support {
      private Support() {
      }

      public static final String SUPPORT_URL_REQRES_FRAGMENT = "reqres";
      public static final String SUPPORT_TEXT_PREFIX = "Tired of writing";
      public static final String SUPPORT_URL_REGEX =
            "https:\\/\\/contentcaddy\\.io\\?utm_source=reqres&utm_medium=json&utm_campaign=referral";
   }

   public static class FileConstants {
      private FileConstants() {
      }

      public static final String AVATAR_FILE_EXTENSION = ".jpg";
   }

   public static class Users {
      private Users() {
      }

      public static final String USER_ONE_FIRST_NAME = "michael";
      public static final int ID_THREE = 3;
      public static final String USER_THREE_EMAIL = "emma.wong@reqres.in";

      public static final Map<String, Object> USER_SEVEN_EXPECTED = Map.of(
            Keys.ID, 7,
            Keys.EMAIL, "michael.lawson@reqres.in",
            Keys.FIRST_NAME, "Michael",
            Keys.LAST_NAME, "Lawson",
            Keys.AVATAR, Urls.avatar(7)
      );
      public static final int USER_SEVENTH_FIRST_NAME_LENGTH = 7;

      public static final String USER_NINE_FIRST_NAME = "Tobias";
      public static final String USER_NINE_LAST_NAME = "Funke";
      public static final int USER_NINE_ID = 9;
      public static final String USER_NINE_EMAIL = "tobias.funke@reqres.in";

      public static final Map<String, Object> INVALID_USER = Map.of(
            Keys.ID, 22,
            Keys.EMAIL, "invalid.user",
            Keys.FIRST_NAME, "Invalid",
            Keys.LAST_NAME, "User",
            Keys.AVATAR, "invalidUrls"
      );
   }

   public static class PageTwo {
      private PageTwo() {
      }

      public static final List<Map<String, Object>> PAGE_TWO_EXPECTED_USERS = List.of(
            Map.of(Keys.ID, 7, Keys.EMAIL, "michael.lawson@reqres.in", Keys.FIRST_NAME, "Michael", Keys.LAST_NAME, "Lawson", Keys.AVATAR, Urls.avatar(7)),
            Map.of(Keys.ID, 8, Keys.EMAIL, "lindsay.ferguson@reqres.in", Keys.FIRST_NAME, "Lindsay", Keys.LAST_NAME, "Ferguson", Keys.AVATAR, Urls.avatar(8)),
            Map.of(Keys.ID, 9, Keys.EMAIL, "tobias.funke@reqres.in", Keys.FIRST_NAME, "Tobias", Keys.LAST_NAME, "Funke", Keys.AVATAR, Urls.avatar(9)),
            Map.of(Keys.ID, 10, Keys.EMAIL, "byron.fields@reqres.in", Keys.FIRST_NAME, "Byron", Keys.LAST_NAME, "Fields", Keys.AVATAR, Urls.avatar(10)),
            Map.of(Keys.ID, 11, Keys.EMAIL, "george.edwards@reqres.in", Keys.FIRST_NAME, "George", Keys.LAST_NAME, "Edwards", Keys.AVATAR, Urls.avatar(11)),
            Map.of(Keys.ID, 12, Keys.EMAIL, "rachel.howell@reqres.in", Keys.FIRST_NAME, "Rachel", Keys.LAST_NAME, "Howell", Keys.AVATAR, Urls.avatar(12))
      );

      public static final List<Map<String, Object>> PAGE_TWO_CONTAINS_ANY_USER = List.of(
            Users.USER_SEVEN_EXPECTED,
            Users.INVALID_USER
      );

      public static final int PAGE_TWO_DATA_SIZE = 6;
   }

   public static class Roles {
      private Roles() {
      }

      public static final String USER_LEADER_NAME = "Morpheus";
      public static final String USER_LEADER_JOB = "Leader";
      public static final String USER_SENIOR_NAME = "Mr. Morpheus";
      public static final String USER_SENIOR_JOB = "Senior Leader";
      public static final String USER_INTERMEDIATE_NAME = "Mr. Morpheus";
      public static final String USER_INTERMEDIATE_JOB = "Intermediate Leader";
   }

   public static class Login {
      private Login() {
      }

      public static final String MISSING_PASSWORD_ERROR = "Missing password";
   }

}
