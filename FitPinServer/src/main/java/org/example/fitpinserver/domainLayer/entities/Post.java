package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String caption;

    @Getter
    @Setter
    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User publisher;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_products",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Getter
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Save> saves = new ArrayList<>();

    public Post() {
    }

    public Post(String caption, User publisher, Instant timestamp) {
        this.caption = caption;
        this.publisher = publisher;
        this.timestamp = timestamp;
    }

    public void addSave(Save save){
        this.saves.add(save);
        save.setPost(this);
    }

    public void removeSave(Save save){
        if(this.saves.remove(save) && save.getPost() == this)
            save.setPost(null);
    }

    public void addPostLike(PostLike postLike){
        this.postLikes.add(postLike);
        postLike.setPost(this);
    }

    public void removePostLike(PostLike postLike){
        if(this.postLikes.remove(postLike) && postLike.getPost() == this)
            postLike.setPost(null);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment){
        if(this.comments.remove(comment) && comment.getPost() == this)
            comment.setPost(null);
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.getPosts().add(this);
    }

    public void removeProduct(Product product) {
        if (this.products.remove(product)) {
            product.getPosts().remove(this);
        }
    }

}
