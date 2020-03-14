package com.example.visualmed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidator{

    private Pattern pattern;
    private Matcher matcher;

    private static final String timeFormat = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(a.m.|p.m.)";

    public TimeValidator(){
        pattern = Pattern.compile(timeFormat);
    }

    public boolean validate(final String time){
        matcher = pattern.matcher(time);
        return matcher.matches();
    }
}

//References:
//https://examples.javacodegeeks.com/core-java/util/regex/matcher/validate-time-in-12-hours-format-with-java-regular-expression-example/