package io.cyborgcode.api.test.framework.data.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AssertionMessages {

    public static final String USER_DATA_SIZE_INCORRECT = "User data size does not match the expected value!";
    public static final String FIRST_NAME_LENGTH_INCORRECT = "First name length does not match the expected value!";
    public static final String CREATED_USER_NAME_INCORRECT = "The created user's name does not match the expected value!";
    public static final String CREATED_USER_JOB_INCORRECT = "The created user's job title does not match the expected value!";
    public static final String CREATED_AT_INCORRECT = "The creation date of the user does not match the expected value!";
    public static final String SENIOR_USER_EMAIL_FOUND_IN_PAGINATED_LIST = "Unexpected: Senior user email found in paginated list!";

    public String userWithFirstNameNotFound(String firstName) {
        return String.format("User with first name '%s' not found", firstName);
    }

}