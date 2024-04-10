package com.zimji.system.message_source;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageTranslator {

    static Logger LOGGER = LoggerFactory.getLogger(MessageTranslator.class);

    static MessageTranslator instance = new MessageTranslator();
    static String BUNDLE_NAME = "language/message";
    static String LIBRARY_BUNDLE_NAME = "message";

    ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
    StaticMessageSource staticMessageSource = new StaticMessageSource();

    public static MessageTranslator getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            instance = new MessageTranslator();
        }
        return instance;
    }

    public MessageTranslator() {
        this.resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        this.resourceBundleMessageSource.setBasenames(BUNDLE_NAME, LIBRARY_BUNDLE_NAME);
        this.resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        this.resourceBundleMessageSource.setCacheSeconds(10);

        this.staticMessageSource.setParentMessageSource(resourceBundleMessageSource);
    }

    public static void register(String tag, Map<String, String> language) {
        Locale locale = Locale.forLanguageTag(tag);
        getInstance().staticMessageSource.addMessages(language, locale);
        getInstance().staticMessageSource.setParentMessageSource(getInstance().resourceBundleMessageSource);
    }

    public static void register(Map<String, Map<String, String>> languages) {
        languages.forEach(MessageTranslator::register);
    }

    public static String toLocale(String message) {
        return getInstance().getStaticMessageSource().getMessage(message, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String messageKey) {
        try {
            return ObjectUtils.isNotEmpty(getInstance().getStaticMessageSource())
                    ? getInstance().getStaticMessageSource().getMessage(messageKey, null, LocaleContextHolder.getLocale())
                    : messageKey;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return messageKey;
        }
    }

    public static String getMessage(String messageKey, Locale locale) {
        try {
            return ObjectUtils.isNotEmpty(getInstance().getStaticMessageSource())
                    ? getInstance().getStaticMessageSource().getMessage(messageKey, null, ObjectUtils.isEmpty(locale) ? LocaleContextHolder.getLocale() : locale)
                    : messageKey;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return messageKey;
        }
    }

    public static String getMessage(String messageKey, Object[] args) {
        try {
            return getInstance().getStaticMessageSource() != null
                    ? getInstance().getStaticMessageSource().getMessage(messageKey, args, LocaleContextHolder.getLocale())
                    : messageKey;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return messageKey;
        }
    }

    public StaticMessageSource getStaticMessageSource() {
        return staticMessageSource;
    }

}