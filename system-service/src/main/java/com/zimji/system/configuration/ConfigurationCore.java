package com.zimji.system.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.jackson.DatabindCodec;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy(
        proxyTargetClass = true
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationCore {

    static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    static String DEFAULT_TIMEZONE = "Asia/Ho_Chi_Minh";

    public ConfigurationCore() {
    }

    @PostConstruct
    public void init() {
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        DatabindCodec.mapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        DatabindCodec.mapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DatabindCodec.mapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DatabindCodec.mapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        DatabindCodec.mapper().disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        DatabindCodec.mapper().setDateFormat(dateFormat);
        DatabindCodec.mapper().registerModule(new JavaTimeModule());
        DatabindCodec.mapper().findAndRegisterModules();
        DatabindCodec.prettyMapper().registerModule(new JavaTimeModule());
        DatabindCodec.prettyMapper().findAndRegisterModules();
        DatabindCodec.prettyMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        DatabindCodec.prettyMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DatabindCodec.prettyMapper().setDateFormat(dateFormat);
        DatabindCodec.prettyMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}