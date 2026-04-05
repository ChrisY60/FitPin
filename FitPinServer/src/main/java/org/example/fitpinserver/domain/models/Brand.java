package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Brand {
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    private List<Product> products = new ArrayList<>();

    public Brand() {
    }

    public Brand(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Brand(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.setBrand(this);
    }

    public void removeProduct(Product product) {
        if (this.products.remove(product) && product.getBrand() == this) {
            product.setBrand(null);
        }
    }
}