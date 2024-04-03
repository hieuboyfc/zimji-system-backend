package com.zimji.system.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateTimeUtils {

    static Logger LOGGER = LoggerFactory.getLogger(DateTimeUtils.class);

    static String DEFAULT_ZONE = "Asia/Ho_Chi_Minh";
    static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static Date addMonth(Date date, int quantity) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, quantity);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date addYear(Date date, int quantity) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, quantity);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date truncDate(Calendar calendar, Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date startOfDay(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date endOfDay(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date startOfCycle(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date startOfYear(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, 1);
            return calendar.getTime();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Date toDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String format(Date date, String format, String timezone) {
        try {
            TimeZone tz = TimeZone.getTimeZone(timezone);
            DateFormat df = new SimpleDateFormat(format);
            df.setTimeZone(tz);
            return df.format(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String getTime(Date date) {
        try {
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            return localDateFormat.format(date.getTime());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String toString(Date date, String format, String timezone) {
        try {
            TimeZone tz = TimeZone.getTimeZone(timezone);
            DateFormat df = new SimpleDateFormat(format);
            df.setTimeZone(tz);
            return df.format(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String format(Date date) {
        try {
            return format(date, DEFAULT_DATE_FORMAT, DEFAULT_ZONE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String format(Date date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}