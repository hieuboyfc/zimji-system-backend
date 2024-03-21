package com.zimji.system.configuration.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class LogbackConfig {

    public LogbackConfig() {
        configure();
    }

    private void configure() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Console Appender for common log
        ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(loggerContext);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);

        // File Appender for common log
        RollingFileAppender<ILoggingEvent> fileAppender = createRollingFileAppender(loggerContext, "COMMON");
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(fileAppender);

        // Root Logger
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ERROR);
        rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(fileAppender);

        // File Appenders for individual classes
        configureClassLoggers(loggerContext);
    }

    private ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext loggerContext) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName("CONSOLE");
        PatternLayoutEncoder consoleEncoder = createPatternLayoutEncoder(loggerContext);
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.start();
        return consoleAppender;
    }

    private RollingFileAppender<ILoggingEvent> createRollingFileAppender(LoggerContext loggerContext, String name) {
        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setName(name + "_FILE");
        fileAppender.setRollingPolicy(createTimeBasedRollingPolicy(loggerContext, fileAppender, null));
        PatternLayoutEncoder fileEncoder = createPatternLayoutEncoder(loggerContext);
        fileAppender.setEncoder(fileEncoder);
        fileAppender.start();
        return fileAppender;
    }

    private PatternLayoutEncoder createPatternLayoutEncoder(LoggerContext loggerContext) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy/MM/dd - HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        // encoder.setPattern("%d{yyyy/MM/dd - HH:mm:ss.SSS} [%thread] %highlight(%-5level) %cyan(%logger{36}) - %msg%n");
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.start();
        return encoder;
    }

    private void configureClassLoggers(LoggerContext loggerContext) {
        // Configure loggers for individual classes
        // configureClassLogger(loggerContext, Class1.class);
        // configureClassLogger(loggerContext, Class2.class);
        // Add configurations for other classes...
    }

    private <T> void configureClassLogger(LoggerContext loggerContext, Class<T> clazz) {
        Logger logger = loggerContext.getLogger(clazz);
        RollingFileAppender<ILoggingEvent> fileAppender = createRollingFileAppender(loggerContext, clazz.getSimpleName().toUpperCase());
        logger.addAppender(fileAppender);
    }

    private <T> TimeBasedRollingPolicy<ILoggingEvent> createTimeBasedRollingPolicy(LoggerContext loggerContext,
                                                                                   RollingFileAppender<ILoggingEvent> fileAppender,
                                                                                   Class<T> clazz) {
        // Get package name
        String packageName = (ObjectUtils.isNotEmpty(clazz))
                ? clazz.getPackage().getName().replace(".", "/") + "/" + clazz.getSimpleName()
                : "common";

        String fileLog = "logs/%d{yyyy-MM-dd}/" + packageName.toLowerCase() + ".log";

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setFileNamePattern(fileLog);
        rollingPolicy.setMaxHistory(30); // Keep 30 days of history
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(fileAppender);
        rollingPolicy.start();
        return rollingPolicy;
    }

}