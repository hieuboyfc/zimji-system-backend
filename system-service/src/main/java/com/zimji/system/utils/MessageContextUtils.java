package com.zimji.system.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageContextUtils {

    private static MessageContextUtils instance;
    private static final String BUNDLE_NAME = "language/message";
    private static final String LIBRARY_BUNDLE_NAME = "message";
    private final ResourceBundleMessageSource fileResourceSource = new ResourceBundleMessageSource();
    private final StaticMessageSource messageSource = new StaticMessageSource();
    private final Map<Locale, Set<String>> localeCompanies = new ConcurrentHashMap<>();

    private static final String COMPANY_KEY = "cid";
    private static final String SEPARATE_KEY = ".";

    public static MessageContextUtils getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            instance = new MessageContextUtils();
        }
        return instance;
    }

    public MessageContextUtils() {
        fileResourceSource.setBasenames(BUNDLE_NAME, LIBRARY_BUNDLE_NAME);
        fileResourceSource.setDefaultEncoding("UTF-8");
        messageSource.setParentMessageSource(fileResourceSource);
    }

    public Map<Locale, Set<String>> getLocaleCompanies() {
        return localeCompanies;
    }

    public static void register(String tag, Map<String, String> language) {
        Locale locale = Locale.forLanguageTag(tag);
        getInstance().localeCompanies.put(locale, language.keySet());
        getInstance().messageSource.addMessages(language, locale);
        getInstance().messageSource.setParentMessageSource(getInstance().fileResourceSource);
    }

    public static void register(Map<String, Map<String, String>> languages) {
        languages.forEach(MessageContextUtils::register);
    }

    public static String getMessage(String key) {
        return getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String key, Object[] args) {
        return getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String key, Locale locale) {
        return getMessage(key, null, locale);
    }

    public static String getMessage(String key, Object[] args, Locale locale) {
        HttpServletRequest request = getRequest();
        if (ObjectUtils.isNotEmpty(request)) {
            String cid = request.getHeader(COMPANY_KEY);
            if (StringUtils.isNotEmpty(cid)) {
                String code = String.format("%s%s%s", cid, SEPARATE_KEY, key);
                Set<String> keys = getInstance().getLocaleCompanies().getOrDefault(
                        locale == null ? LocaleContextHolder.getLocale() : locale, Collections.emptySet()
                );
                if (ObjectUtils.isNotEmpty(keys) && keys.contains(code)) {
                    assert locale != null;
                    return getInstance().getMessageSource().getMessage(code, args, locale);
                }
            }
        }

        if (ObjectUtils.isNotEmpty(getInstance().getMessageSource())) {
            assert locale != null;
            return getInstance().getMessageSource().getMessage(key, args, locale);
        } else {
            return key;
        }
    }

    public StaticMessageSource getMessageSource() {
        return messageSource;
    }

    private static HttpServletRequest getRequest() {
        try {
            return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                    .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes).getRequest())
                    .orElse(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}