package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @Getter
    @Setter
    private Brand brand;

    @ManyToMany(mappedBy = "products")
    @Getter
    private List<Post> posts = new ArrayList<>();

    public Product() {
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
    @PreRemove // this way i can automatically remove the product from all posts its inside
    private void preRemove() {
        removeFromAllPosts();
    }
}
