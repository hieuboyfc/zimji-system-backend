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

    @Setter
    @Getter
    public static class OpenAPI {
        private String devUrl;
        private String prodUrl;
    }

}