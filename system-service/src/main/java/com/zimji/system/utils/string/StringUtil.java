package com.zimji.system.utils.string;

import com.zimji.system.utils.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String unAccent(String src) {
        return Normalizer.normalize(src, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        temp = temp.replaceAll("Đ", "D");
        temp = temp.replaceAll("đ", "d");
        return temp;
    }

    public static String replace(Map<String, Object> mapper, String temp) {
        if (ObjectUtils.isEmpty(temp)) {
            return temp;
        }

        StringBuilder buffer = new StringBuilder();
        Matcher matcher = Constants.PATTERN_URI.matcher(temp);

        while (matcher.find()) {
            String name = matcher.group(Constants.PATTERN_URI_NAME_KEY);
            if (ObjectUtils.isNotEmpty(mapper) && mapper.containsKey(name)) {
                Object value = mapper.get(name);
                matcher.appendReplacement(buffer, ObjectUtils.isEmpty(value) ? StringUtils.EMPTY : Matcher.quoteReplacement(value.toString()));
            }
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    public static String substring(String content, String startMark, String endMark) {
        if (ObjectUtils.isEmpty(content)) {
            return null;
        }

        int startIndex = content.indexOf(startMark);
        if (startIndex == -1) {
            return null;
        }

        startIndex += startMark.length();

        int endIndex = content.indexOf(endMark, startIndex);
        if (endIndex == -1 || endIndex <= startIndex) {
            return null;
        }

        return content.substring(startIndex, endIndex);
    }


}