package com.zimji.system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MapUtils {

    static Logger LOGGER = LoggerFactory.getLogger(MapUtils.class);

    public static Date getDate(Map<?, ?> map, Object key, String pattern, Date defaultValue) {
        Object value = map.get(key);
        if (!(value instanceof String strDate)) {
            return defaultValue;
        }
        try {
            return DateTimeUtils.toDate(strDate, pattern);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return defaultValue;
        }
    }

    public static Date getDate(Map<?, ?> map, Object key, Date defaultValue) {
        return getDate(map, key, DateTimeUtils.DEFAULT_DATE_FORMAT, defaultValue);
    }

    public static Date getDate(Map<?, ?> map, Object key) {
        return getDate(map, key, null);
    }

    private static Set<Number> getNumbersToSet(Map<?, ?> map, Object key, Set<Number> defaultValue) {
        Object value = map.get(key);
        if (!(value instanceof Set<?> rawSet)) {
            return defaultValue;
        }

        Set<Number> numbers = new HashSet<>();
        for (Object element : rawSet) {
            if (element instanceof Number) {
                numbers.add((Number) element);
            }
        }
        return numbers;
    }

    private static List<Number> getNumbersToList(Map<?, ?> map, Object key, List<Number> defaultValue) {
        Object value = map.get(key);
        if (!(value instanceof List<?> rawList)) {
            return defaultValue;
        }

        List<Number> numbers = new ArrayList<>();
        for (Object element : rawList) {
            if (element instanceof Number) {
                numbers.add((Number) element);
            }
        }
        return numbers;
    }

}