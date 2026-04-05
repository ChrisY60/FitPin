package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "brand")
    @Getter
    private List<ProductEntity> products = new ArrayList<>();

    public BrandEntity() {
    }

    public BrandEntity(String name) {
        this.name = name;
    }

    public void addProduct(ProductEntity product){
        this.products.add(product);
        product.setBrand(this);
    }
    public void removeProduct(ProductEntity product){
        if(this.products.remove(product) && product.getBrand() == this)
            product.setBrand(null);
    }


}
