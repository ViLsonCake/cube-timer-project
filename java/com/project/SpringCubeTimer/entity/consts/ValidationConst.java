package com.project.SpringCubeTimer.entity.consts;

public class ValidationConst {
    // Pattern
    public static final String REGEX_PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!_^&+=])(?=\\S+$).{8,16}$";

    // Messages
    public static final String USERNAME_EMPTY_MESSAGE = "Username can't be empty!";
    public static final String EMAIL_EMPTY_MESSAGE = "Email can't be empty";
    public static final String PASSWORD_EMPTY_MESSAGE = "Password can't be empty";
    public static final String PASSWORD_NOT_VALID_MESSAGE = "Password is not valid";
    public static final String EMAIL_NOT_VALID_MESSAGE = "Email is not valid";
}
