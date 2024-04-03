package com.zimji.system;

import com.zimji.system.configuration.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.zimji.*"})
@EnableScheduling
@EnableAsync
@EnableJpaRepositories({"com.zimji.*"})
@EntityScan(basePackages = {"com.zimji.*"})
@EnableConfigurationProperties(SystemProperties.class)
public class SystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        // Chạy ứng dụng Spring Boot
        SpringApplication.run(SystemApplication.class, args);

        // Log các thông điệp
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
    }

}
