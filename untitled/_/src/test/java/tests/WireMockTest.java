package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.wiremock.Main;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

// Spring Boot test class for WireMock
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Main.class})
@com.github.tomakehurst.wiremock.junit5.WireMockTest
public class WireMockTest {

    @Autowired
    private WebTestClient webTestClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // WireMock extension for testing REST client
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    // Dynamic property source to set up base URL for WireMock
    @DynamicPropertySource
    public static void  setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("products_base_url", wireMockExtension::baseUrl);
    }

    // Test to verify that product count is 0 when the list is empty
    @Test
    public void testProductSizeIsEmpty() {
        wireMockExtension.stubFor(
                WireMock.get("/products")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[]")));

        webTestClient.get().uri("api/products/count")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"count\": 0}");
    }
}