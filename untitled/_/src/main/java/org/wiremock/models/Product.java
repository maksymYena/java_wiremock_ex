package org.wiremock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Integer id;
    private String title;

    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;


    public static List<Product> generateRandomProducts(int size) {
        Random random = new Random();
        List<Product> products = new ArrayList<>();

        for(int i = 0; i < size; i++ ) {
            Rating rating = Rating.builder()
                    .count(random.nextInt(5))
                    .rate((double) random.nextInt(10))
                    .build();

            Product createdRandomProduct = Product.builder()
                    .id(random.nextInt(4_000))
                    .rating(rating)
                    .title("Some title: " + random.nextInt(1_000))
                    .price((double) random.nextInt(10_000))
                    .category("category_" + random.nextInt(10_000))
                    .image("image")
                    .description("description")
                    .build();

            products.add(createdRandomProduct);
        }
        return products;
    }
}
