package com.project.SpringCubeTimer.validate;

import com.project.SpringCubeTimer.validate.validConst.BodyValidationConst;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestBodyValidation {
    public static boolean isValidCube(String cube) {
        if (cube.equalsIgnoreCase("pyraminx"))
            return true;

        Pattern cubePatter = Pattern.compile(BodyValidationConst.CUBE_REGEX);
        Matcher cubeMatcher = cubePatter.matcher(cube);

        return cubeMatcher.find();
    }

    public static boolean isValidTime(String time) {
        Pattern secondsTimePattern = Pattern.compile(BodyValidationConst.SECONDS_TIME_REGEX);
        Pattern minutesTimePattern = Pattern.compile(BodyValidationConst.MINUTES_TIME_REGEX);
        Matcher secondsMatcher = secondsTimePattern.matcher(time);
        Matcher minutesMatcher = minutesTimePattern.matcher(time);

        return secondsMatcher.find() || minutesMatcher.find();
    }
}
