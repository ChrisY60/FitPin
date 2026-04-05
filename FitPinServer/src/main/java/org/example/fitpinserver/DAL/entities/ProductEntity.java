package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {
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
    private BrandEntity brand;

    @ManyToMany(mappedBy = "products")
    @Getter
    private List<PostEntity> posts = new ArrayList<>();

    public ProductEntity() {
    }

    public ProductEntity(String name, BrandEntity brand) {
        this.name = name;
        this.brand = brand;
    }

    public void removeFromAllPosts() {
        for (PostEntity post : new ArrayList<>(posts)) {
            post.removeProduct(this);
        }
    }
    @PreRemove // this way i can automatically remove the product from all posts its inside
    private void preRemove() {
        removeFromAllPosts();
    }
}
