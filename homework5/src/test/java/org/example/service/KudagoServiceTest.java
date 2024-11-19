package org.example.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import groovy.util.logging.Slf4j;
import org.example.dto.response.EventKudagoResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.MockObjects.AIRPORTS_NAME;
import static org.example.MockObjects.AIRPORTS_SLUG;
import static org.example.MockObjects.AMUSEMENT_NAME;
import static org.example.MockObjects.AMUSEMENT_SLUG;
import static org.example.MockObjects.ANIMAL_SHELTERS_NAME;
import static org.example.MockObjects.ANIMAL_SHELTERS_SLUG;
import static org.example.MockObjects.EKATERINBURG_NAME;
import static org.example.MockObjects.EKATERINBURG_SLUG;
import static org.example.MockObjects.KAZAN_NAME;
import static org.example.MockObjects.KAZAN_SLUG;
import static org.example.MockObjects.MOSCOW_NAME;
import static org.example.MockObjects.MOSCOW_SLUG;
import static org.example.MockObjects.NIZHNY_NOVGOROD_NAME;
import static org.example.MockObjects.NIZHNY_NOVGOROD_SLUG;
import static org.example.MockObjects.ST_PETERSBURG_NAME;
import static org.example.MockObjects.ST_PETERSBURG_SLUG;
import static org.example.MockObjects.THEATRE_NAME;
import static org.example.MockObjects.THEATRE_SLUG;
import static org.example.MockObjects.WORKSHOPS_NAME;
import static org.example.MockObjects.WORKSHOPS_SLUG;

@Slf4j
@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@SpringBootTest
@Testcontainers
class KudagoServiceTest {

    @Autowired
    private KudagoService kudagoService;

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(
                    wireMockConfig()
                            .port(8081)
                            .usingFilesUnderClasspath("wiremock")
            )
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("rest.kudago.url", wireMock::baseUrl);
    }

    @Test
    void testGetEvents() {
        long actualSince = 1729458000;
        var eventResponse = kudagoService.getEvents(actualSince);

        assertThat(eventResponse.getResults()).hasSize(2);
        List<EventKudagoResult> resultDTO = eventResponse.getResults();
        EventKudagoResult firstResult = resultDTO.get(0);
        EventKudagoResult secondResult = resultDTO.get(1);

        assertThat(firstResult.getDates()).hasSize(1);
        assertThat(firstResult.getDates().getFirst().getStart()).isEqualTo(1643662800);
        assertThat(firstResult.getDates().getFirst().getEnd()).isEqualTo(1643662800);
        assertThat(firstResult.getTitle()).isEqualTo("выставка «Сокровища Древней Греции» в Музее Международного нумизматического клуба");
        assertThat(firstResult.getLocation().getSlug()).isEqualTo("msk");
        assertThat(firstResult.getPrice()).isEqualTo("от 300 до 550 рублей");

        assertThat(secondResult.getDates()).hasSize(2);
        assertThat(secondResult.getDates().get(0).getStart()).isEqualTo(1612386000);
        assertThat(secondResult.getDates().get(0).getEnd()).isEqualTo(1677618000);
        assertThat(secondResult.getDates().get(1).getStart()).isEqualTo(1735676800);
        assertThat(secondResult.getDates().get(1).getEnd()).isEqualTo(1735678800);
        assertThat(secondResult.getTitle()).isEqualTo("иммерсивная выставка «Алиса в Зазеркалье»");
        assertThat(secondResult.getLocation().getSlug()).isEqualTo("spb");
        assertThat(secondResult.getPrice()).isEqualTo("350 рублей");
    }

    @Test
    public void testGetCategoriesSemaphoreWithTenThreads() throws InterruptedException {
        int numThreads = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    if (kudagoService.getCategories() != null) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await();

        assertThat(successCount.get()).isEqualTo(numThreads);
    }

    @Test
    public void testGetLocationsSemaphoreWithTenThreads() throws InterruptedException {
        int numThreads = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    if (kudagoService.getLocations() != null) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await();

        assertThat(successCount.get()).isEqualTo(numThreads);
    }

    @Test
    public void testGetEventsSemaphoreWithTenThreads() throws InterruptedException {
        int numThreads = 10;
        long actualSince = 1729458000;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    if (kudagoService.getEvents(actualSince) != null) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await();

        assertThat(successCount.get()).isEqualTo(numThreads);
    }

    @Test
    void testGetCategories() {
        var result = kudagoService.getCategories();

        assertThat(result).hasSize(5);

        assertThat(result[0].getId()).isEqualTo(123);
        assertThat(result[0].getSlug()).isEqualTo(AIRPORTS_SLUG);
        assertThat(result[0].getName()).isEqualTo(AIRPORTS_NAME);

        assertThat(result[1].getId()).isEqualTo(89);
        assertThat(result[1].getSlug()).isEqualTo(AMUSEMENT_SLUG);
        assertThat(result[1].getName()).isEqualTo(AMUSEMENT_NAME);

        assertThat(result[2].getId()).isEqualTo(114);
        assertThat(result[2].getSlug()).isEqualTo(ANIMAL_SHELTERS_SLUG);
        assertThat(result[2].getName()).isEqualTo(ANIMAL_SHELTERS_NAME);

        assertThat(result[3].getId()).isEqualTo(48);
        assertThat(result[3].getSlug()).isEqualTo(THEATRE_SLUG);
        assertThat(result[3].getName()).isEqualTo(THEATRE_NAME);

        assertThat(result[4].getId()).isEqualTo(127);
        assertThat(result[4].getSlug()).isEqualTo(WORKSHOPS_SLUG);
        assertThat(result[4].getName()).isEqualTo(WORKSHOPS_NAME);
    }

    @Test
    void testGetLocations() {
        var result = kudagoService.getLocations();

        assertThat(result).hasSize(5);

        assertThat(result[0].getSlug()).isEqualTo(EKATERINBURG_SLUG);
        assertThat(result[0].getName()).isEqualTo(EKATERINBURG_NAME);

        assertThat(result[1].getSlug()).isEqualTo(KAZAN_SLUG);
        assertThat(result[1].getName()).isEqualTo(KAZAN_NAME);

        assertThat(result[2].getSlug()).isEqualTo(MOSCOW_SLUG);
        assertThat(result[2].getName()).isEqualTo(MOSCOW_NAME);

        assertThat(result[3].getSlug()).isEqualTo(NIZHNY_NOVGOROD_SLUG);
        assertThat(result[3].getName()).isEqualTo(NIZHNY_NOVGOROD_NAME);

        assertThat(result[4].getSlug()).isEqualTo(ST_PETERSBURG_SLUG);
        assertThat(result[4].getName()).isEqualTo(ST_PETERSBURG_NAME);
    }

}