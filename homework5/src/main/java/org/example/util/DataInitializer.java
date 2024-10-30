package org.example.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.CategoryService;
import org.example.service.KudagoService;
import org.example.service.LocationService;
import org.example.timed.Timed;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final KudagoService kudagoService;

    private final ExecutorService fixedThreadPool;

    private final ScheduledExecutorService scheduledThreadPool;

    @EventListener(ApplicationStartedEvent.class)
    @Timed
    public void init() {
        log.info("Starting data initialization...");

        scheduledThreadPool.schedule(this::initializeData, 0, TimeUnit.SECONDS);
    }

    @Scheduled(initialDelayString = "${initialization.interval}", fixedDelayString = "${initialization.interval}")
    @Timed
    public void initializeData() {
        log.info("Scheduled data initialization started...");

        CommandExecutor executor = new CommandExecutor(fixedThreadPool);

        executor.executeCommand(new FetchAndSaveCategoriesCommand(kudagoService, categoryService));
        executor.executeCommand(new FetchAndSaveLocationsCommand(kudagoService, locationService));

        log.info("Scheduled data initialization completed.");
    }

}