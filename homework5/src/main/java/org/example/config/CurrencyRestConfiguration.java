package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.config.property.CurrencyRestProperties;
import org.example.config.property.KudagoRestProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Semaphore;

@Configuration
@RequiredArgsConstructor
public class CurrencyRestConfiguration {

    @Bean
    public RestTemplate currencyRestTemplate(
            RestTemplateBuilder restTemplateBuilder,
            CurrencyRestProperties properties
    ) {
        return restTemplateBuilder
                .rootUri(properties.getUrl())
                .setConnectTimeout(properties.getConnectTimeout())
                .setReadTimeout(properties.getReadTimeout())
                .build();
    }

    @Bean
    public Semaphore kudagoSemaphore(
            KudagoRestProperties properties
    ) {
        return new Semaphore(properties.getMaximumNumberConcurrentRequests());
    }
    
}