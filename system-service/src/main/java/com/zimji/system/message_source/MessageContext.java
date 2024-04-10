package com.zimji.system.message_source;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageContext {

    static Logger LOGGER = LoggerFactory.getLogger(MessageContext.class);

    static MessageContext instance = new MessageContext();
    static String BUNDLE_NAME = "language/message";
    static String LIBRARY_BUNDLE_NAME = "message";

    ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
    StaticMessageSource staticMessageSource = new StaticMessageSource();
    Map<Locale, Set<String>> localeCompanies = new ConcurrentHashMap<>();

    static String COMPANY_KEY = "cid";
    static String SEPARATE_KEY = ".";

    public static MessageContext getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            instance = new MessageContext();
        }
        return instance;
    }

    public MessageContext() {
        this.resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        this.resourceBundleMessageSource.setBasenames(BUNDLE_NAME, LIBRARY_BUNDLE_NAME);
        this.resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        this.resourceBundleMessageSource.setCacheSeconds(10);

        this.staticMessageSource.setParentMessageSource(resourceBundleMessageSource);
    }

    public Map<Locale, Set<String>> getLocaleCompanies() {
        return localeCompanies;
    }

    public static void register(String tag, Map<String, String> language) {
        Locale locale = Locale.forLanguageTag(tag);
        getInstance().localeCompanies.put(locale, language.keySet());
        getInstance().staticMessageSource.addMessages(language, locale);
        getInstance().staticMessageSource.setParentMessageSource(getInstance().resourceBundleMessageSource);
    }

    public static void register(Map<String, Map<String, String>> languages) {
        languages.forEach(MessageContext::register);
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
                    return getInstance().getStaticMessageSource().getMessage(code, args, locale);
                }
            }
        }

        if (ObjectUtils.isNotEmpty(getInstance().getStaticMessageSource())) {
            assert locale != null;
            return getInstance().getStaticMessageSource().getMessage(key, args, locale);
        } else {
            return key;
        }
    }

    public StaticMessageSource getStaticMessageSource() {
        return staticMessageSource;
    }

    private static HttpServletRequest getRequest() {
        try {
            return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                    .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes).getRequest())
                    .orElse(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

}