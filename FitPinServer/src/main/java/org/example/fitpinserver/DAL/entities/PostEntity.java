package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostEntity {
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
    private UserEntity publisher;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<CommentEntity> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_products",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Getter
    private List<ProductEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<PostLikeEntity> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<SaveEntity> saves = new ArrayList<>();

    public PostEntity() {
    }

    public PostEntity(String caption, UserEntity publisher, Instant timestamp) {
        this.caption = caption;
        this.publisher = publisher;
        this.timestamp = timestamp;
    }

    public void addSave(SaveEntity save){
        this.saves.add(save);
        save.setPost(this);
    }

    public void removeSave(SaveEntity save){
        if(this.saves.remove(save) && save.getPost() == this)
            save.setPost(null);
    }

    public void addPostLike(PostLikeEntity postLike){
        this.postLikes.add(postLike);
        postLike.setPost(this);
    }

    public void removePostLike(PostLikeEntity postLike){
        if(this.postLikes.remove(postLike) && postLike.getPost() == this)
            postLike.setPost(null);
    }

    public void addComment(CommentEntity comment){
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(CommentEntity comment){
        if(this.comments.remove(comment) && comment.getPost() == this)
            comment.setPost(null);
    }

    public void addProduct(ProductEntity product) {
        this.products.add(product);
        product.getPosts().add(this);
    }

    public void removeProduct(ProductEntity product) {
        if (this.products.remove(product)) {
            product.getPosts().remove(this);
        }
    }

}
