package com.zimji.system.configuration;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Configuration
@ConfigurationProperties(prefix = "system")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemProperties {

    OpenAPI openAPI = new OpenAPI();

    Cors cors = new Cors();

    Parameter parameter = new Parameter();

    @Setter
    @Getter
    public static class OpenAPI {
        private String devUrl;
        private String prodUrl;
    }

    @Setter
    @Getter
    public static class Cors {
        private String[] allowedOrigins;
    }

    @Setter
    @Getter
    public static class Parameter {
        private int maxAgeSecond;
    }

}