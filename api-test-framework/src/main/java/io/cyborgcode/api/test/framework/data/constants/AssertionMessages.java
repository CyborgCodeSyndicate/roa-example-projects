package io.cyborgcode.api.test.framework.data.constants;

import lombok.experimental.UtilityClass;

/**
 * Centralized assertion messages used across tests.
 * <p>
 * Keeps failure messages consistent and readable while avoiding duplication
 * in individual test classes.
 */
@UtilityClass
public final class AssertionMessages {

    public static final String USER_DATA_SIZE_INCORRECT = "User data size does not match the expected value!";
    public static final String FIRST_NAME_LENGTH_INCORRECT = "First name length does not match the expected value!";
    public static final String CREATED_USER_NAME_INCORRECT = "The created user's name does not match the expected value!";
    public static final String CREATED_USER_JOB_INCORRECT = "The created user's job title does not match the expected value!";
    public static final String CREATED_AT_INCORRECT = "The creation date of the user does not match the expected value!";

    public String userWithFirstNameNotFound(String firstName) {
        return String.format("User with first name '%s' not found", firstName);
    }

}