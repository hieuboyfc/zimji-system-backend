package com.zimji.system.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.regex.Pattern;

public class Constants {

    public final static String PATTERN_URI_ITEM_KEY = "item";
    public final static String PATTERN_URI_NAME_KEY = "name";
    public final static String REGEX_PATTERN_URI = "(?<" + PATTERN_URI_ITEM_KEY + ">\\$\\{(?<" + PATTERN_URI_NAME_KEY + ">[^}]+)})";
    public final static Pattern PATTERN_URI = Pattern.compile(Constants.REGEX_PATTERN_URI);

}