package org.wiremock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.wiremock.models.Product;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private WebClient webClient;

    // Get product by id
    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable int id) {
        return webClient.get().uri(x -> x
                        .path("/products/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    // Get list of products
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts() {
        return webClient.get().uri("/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
    }

    // Get count of products
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProductsCount() {
        int size =  getProducts().size();
        return String.format("{\"count\" : %d}", size);
    }
}
