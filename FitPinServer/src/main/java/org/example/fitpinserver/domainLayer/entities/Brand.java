package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "brand")
    @Getter
    private List<Product> products = new ArrayList<>();

    public Brand() {
    }

    public Brand(String name) {
        this.name = name;
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setBrand(this);
    }
    public void removeProduct(Product product){
        if(this.products.remove(product) && product.getBrand() == this)
            product.setBrand(null);
    }


}
