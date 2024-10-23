package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.config.property.FixedThreadProperties;
import org.example.config.property.ScheduledThreadProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfiguration {

    private final FixedThreadProperties fixedThreadProperties;
    private final ScheduledThreadProperties scheduledThreadProperties;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(fixedThreadProperties.getSize(), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("FixedThreadPool-" + thread.getId());

            return thread;
        });
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadProperties.getSize(), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("ScheduledThreadPool-" + thread.getId());
            return thread;
        });
    }
}