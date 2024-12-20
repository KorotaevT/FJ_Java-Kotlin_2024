package org.example.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("rest.spring-app-currency")
public class CurrencyRestProperties {
    private String url;
    private Duration readTimeout;
    private Duration connectTimeout;
    private Integer maximumNumberConcurrentRequests;
}