package org.example.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

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