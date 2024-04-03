package com.zimji.system.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class CompareUtils {

    public static boolean compare(String src, String dest) {
        return StringUtils.equals(src, dest);
    }

    public static boolean compare(Boolean src, Boolean dest) {
        return ObjectUtils.defaultIfNull(src, false).equals(ObjectUtils.defaultIfNull(dest, false));
    }

    public static boolean compare(Long src, Long dest) {
        return ObjectUtils.defaultIfNull(src, 0L).equals(ObjectUtils.defaultIfNull(dest, 0L));
    }

    public static boolean compare(Double src, Double dest) {
        return ObjectUtils.defaultIfNull(src, 0D).equals(ObjectUtils.defaultIfNull(dest, 0D));
    }

    public static boolean compare(Integer src, Integer dest) {
        return ObjectUtils.defaultIfNull(src, 0).equals(ObjectUtils.defaultIfNull(dest, 0));
    }

    public static boolean compare(Date src, Date dest) {
        return ObjectUtils.defaultIfNull(src, new Date(0)).getTime() == ObjectUtils.defaultIfNull(dest, new Date(0)).getTime();
    }

    public static boolean compare(Object src, Object dest) {
        return ObjectUtils.defaultIfNull(src, StringUtils.EMPTY).equals(ObjectUtils.defaultIfNull(dest, StringUtils.EMPTY));
    }

    public static boolean compare(Collection<?> src, Collection<?> dest) {
        if (src == dest) {
            return true;
        }

        if (ObjectUtils.isEmpty(src) || ObjectUtils.isEmpty(dest)) {
            return false;
        }

        if (src.size() != dest.size()) {
            return false;
        }

        Collection<Object> srcCopy = new HashSet<>(src);
        Collection<Object> destCopy = new HashSet<>(dest);

        return srcCopy.containsAll(destCopy);
    }

}