package io.cyborgcode.api.test.framework.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AssertionMessages {

    public static final String USER_DATA_SIZE_INCORRECT = "User data size not correct!";
    public static final String FIRST_NAME_LENGTH_INCORRECT = "Name length incorrect!";
    public static final String NAME_INCORRECT = "The created user's name does not match the expected value!";
    public static final String JOB_INCORRECT = "The created user's job title does not match the expected value!";
    public static final String CREATED_AT_INCORRECT = "The creation date of the user is not today or formatted incorrectly!";
    public static final String EMAIL_FOUND_IN_LIST_UNEXPECTED = "Unexpected: Senior user email found in paginated list!";

    public String userWithFirstNameNotFound(String firstName) {
        return String.format("User with first name '%s' not found", firstName);
    }

}