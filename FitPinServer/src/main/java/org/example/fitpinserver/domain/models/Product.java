package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Product {
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Brand brand;

    @Getter
    private List<Post> posts = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String name, Brand brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }

    public Product(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
    }

    public void removeFromAllPosts() {
        for (Post post : new ArrayList<>(posts)) {
            post.removeProduct(this);
        }
    }
}