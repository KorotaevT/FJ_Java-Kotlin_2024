package org.example.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import org.example.dto.response.EventKudagoResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class KudagoServiceTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private KudagoService kudagoService;

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(
                    wireMockConfig()
                            .dynamicPort()
                            .usingFilesUnderClasspath("wiremock")
            )
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("rest.kudago.url", wireMock::baseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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

    @ParameterizedTest
    @CsvSource({
            "400", "500"
    })
    void testKudagoBadResponseEvents(int responseHttpCode) {
        wireMock.stubFor(
                WireMock.get(urlEqualTo("/events?fields=title,price,dates,location&actual_since=1729458000"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withStatus(responseHttpCode)
                        )
        );

        long actualSince = 1729458000;
        var result = kudagoService.getEvents(actualSince);

        assertThat(result == null);
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
        assertThat(result[0].getSlug()).isEqualTo("airports");
        assertThat(result[0].getName()).isEqualTo("Аэропорты");

        assertThat(result[1].getId()).isEqualTo(89);
        assertThat(result[1].getSlug()).isEqualTo("amusement");
        assertThat(result[1].getName()).isEqualTo("Развлечения");

        assertThat(result[2].getId()).isEqualTo(114);
        assertThat(result[2].getSlug()).isEqualTo("animal-shelters");
        assertThat(result[2].getName()).isEqualTo("Питомники");

        assertThat(result[3].getId()).isEqualTo(48);
        assertThat(result[3].getSlug()).isEqualTo("theatre");
        assertThat(result[3].getName()).isEqualTo("Театры");

        assertThat(result[4].getId()).isEqualTo(127);
        assertThat(result[4].getSlug()).isEqualTo("workshops");
        assertThat(result[4].getName()).isEqualTo("Мастерские");
    }

    @Test
    void testGetLocations() {
        var result = kudagoService.getLocations();

        assertThat(result).hasSize(5);

        assertThat(result[0].getSlug()).isEqualTo("ekb");
        assertThat(result[0].getName()).isEqualTo("Екатеринбург");

        assertThat(result[1].getSlug()).isEqualTo("kzn");
        assertThat(result[1].getName()).isEqualTo("Казань");

        assertThat(result[2].getSlug()).isEqualTo("msk");
        assertThat(result[2].getName()).isEqualTo("Москва");

        assertThat(result[3].getSlug()).isEqualTo("nnv");
        assertThat(result[3].getName()).isEqualTo("Нижний Новгород");

        assertThat(result[4].getSlug()).isEqualTo("spb");
        assertThat(result[4].getName()).isEqualTo("Санкт-Петербург");
    }

}