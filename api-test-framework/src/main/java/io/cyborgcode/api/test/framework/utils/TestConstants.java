package io.cyborgcode.api.test.framework.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class TestConstants {

    public static class Pagination {
        public static final int PAGE_ONE = 1;
        public static final int PAGE_TWO = 2;
        public static final int PAGE_THREE = 3;
        public static final List<Integer> TOTAL_USERS_IN_PAGE_RANGE = List.of(5, 15);
    }

    public static class Support {
        public static final String SUPPORT_URL_REQRES_FRAGMENT = "reqres";
        public static final String SUPPORT_TEXT_PREFIX = "Tired of writing";
        public static final String SUPPORT_URL_REGEX = "https:\\/\\/contentcaddy\\.io\\?utm_source=reqres&utm_medium=json&utm_campaign=referral";
        public static final String SUPPORT_URL_VALUE = "https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral";
        public static final String SUPPORT_TEXT_FULL = "Tired of writing endless social media content?";
        public static final String SUPPORT_TEXT_BRAND = "Content Caddy";
        public static final String SUPPORT_TEXT_CADDY_FULL = "Tired of writing endless social media content? Let Content Caddy generate it for you.";
    }

    public static class FileConstants {
        public static final String AVATAR_FILE_EXTENSION = ".jpg";
    }

    public static class Users {
        public static final String USER_ONE_FIRST_NAME = "michael";
        public static final int ID_THREE = 3;
        public static final String USER_THREE_EMAIL = "emma.wong@reqres.in";

        public static final Map<String, Object> USER_SEVEN_EXPECTED = Map.of(
                "id", 7,
                "email", "michael.lawson@reqres.in",
                "first_name", "Michael",
                "last_name", "Lawson",
                "avatar", "https://reqres.in/img/faces/7-image.jpg"
        );
        public static final int USER_SEVENTH_FIRST_NAME_LENGTH = 7;

        public static final String USER_NINE_FIRST_NAME = "Tobias";
        public static final String USER_NINE_LAST_NAME = "Funke";
        public static final int USER_NINE_ID = 9;
        public static final String USER_NINE_EMAIL = "tobias.funke@reqres.in";

        public static final Map<String, Object> INVALID_USER = Map.of(
                "id", 22,
                "email", "invalid.user",
                "first_name", "Invalid",
                "last_name", "User",
                "avatar", "invalidUrls"
        );
        public static final int INVALID_USER_ID = 23;
        public static final String USER_TIMESTAMP_REGEX = "^\\d{4}-\\d{2}-\\d{2}T.*Z$";
        public static final String USER_EMAIL_DOMAIN = "@reqres.in";
        public static final String USER_THREE_FIRST_NAME = "Emma";
        public static final String USER_THREE_AVATAR = "https://reqres.in/img/faces/3-image.jpg";
    }

    public static class PageTwo {
        public static final List<Map<String, Object>> PAGE_TWO_EXPECTED_USERS = List.of(
                Map.of("id", 7, "email", "michael.lawson@reqres.in", "first_name", "Michael", "last_name", "Lawson", "avatar", "https://reqres.in/img/faces/7-image.jpg"),
                Map.of("id", 8, "email", "lindsay.ferguson@reqres.in", "first_name", "Lindsay", "last_name", "Ferguson", "avatar", "https://reqres.in/img/faces/8-image.jpg"),
                Map.of("id", 9, "email", "tobias.funke@reqres.in", "first_name", "Tobias", "last_name", "Funke", "avatar", "https://reqres.in/img/faces/9-image.jpg"),
                Map.of("id", 10, "email", "byron.fields@reqres.in", "first_name", "Byron", "last_name", "Fields", "avatar", "https://reqres.in/img/faces/10-image.jpg"),
                Map.of("id", 11, "email", "george.edwards@reqres.in", "first_name", "George", "last_name", "Edwards", "avatar", "https://reqres.in/img/faces/11-image.jpg"),
                Map.of("id", 12, "email", "rachel.howell@reqres.in", "first_name", "Rachel", "last_name", "Howell", "avatar", "https://reqres.in/img/faces/12-image.jpg")
        );

        public static final List<Map<String, Object>> PAGE_TWO_CONTAINS_ANY_USER = List.of(
                Users.USER_SEVEN_EXPECTED,
                Users.INVALID_USER
        );

        public static final int PAGE_TWO_DATA_SIZE = 6;
    }

    public static class Roles {
        public static final String USER_LEADER_NAME = "Morpheus";
        public static final String USER_LEADER_JOB = "Leader";
        public static final String USER_SENIOR_NAME = "Mr. Morpheus";
        public static final String USER_SENIOR_JOB = "Senior Leader";
        public static final String USER_INTERMEDIATE_NAME = "Mr. Morpheus";
        public static final String USER_INTERMEDIATE_JOB = "Intermediate Leader";
        public static final String USER_JUNIOR_NAME = "Michael suffix";
        public static final String USER_JUNIOR_JOB = "Junior Lawson worker";
    }

    public static class Login {
        public static final String TOKEN_REGEX = "[a-zA-Z0-9]+";
        public static final String MISSING_PASSWORD_ERROR = "Missing password";
        public static final String MISSING_EMAIL_ERROR = "Missing email or username";
        public static final String USER_NOT_FOUND_ERROR = "user not found";
        public static final String INVALID_EMAIL = "wrong.email@reqres.in";
    }

    public static class Resources {
        public static final int RESOURCE_ONE_ID = 1;
        public static final String RESOURCE_ONE_NAME = "cerulean";
        public static final int RESOURCE_TWO_ID = 2;
        public static final String RESOURCE_TWO_NAME = "fuchsia rose";
        public static final int RESOURCE_TWO_YEAR = 2001;
        public static final String RESOURCE_TWO_COLOR = "#C74375";
        public static final String RESOURCE_TWO_PANTONE = "17-2031";
        public static final int RESOURCE_INVALID_ID = 23;
        public static final String RESOURCE_SIX_NAME = "honeysuckle";
        public static final String RESOURCE_SIX_COLOR = "#D94F70";
    }

}
