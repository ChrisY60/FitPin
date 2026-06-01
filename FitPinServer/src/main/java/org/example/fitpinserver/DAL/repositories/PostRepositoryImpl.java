package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.entities.TagEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.PostPersistenceMapper;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.business.repositories.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;
    private final PostPersistenceMapper postPersistenceMapper;
    private final UserJPARepository userJPARepository;
    private final ProductJPARepository productJPARepository;
    private final TagJPARepository tagJPARepository;

    public PostRepositoryImpl(PostJPARepository postJPARepository, PostPersistenceMapper postPersistenceMapper,
                              UserJPARepository userJPARepository, ProductJPARepository productJPARepository,
                              TagJPARepository tagJPARepository) {
        this.postJPARepository = postJPARepository;
        this.postPersistenceMapper = postPersistenceMapper;
        this.userJPARepository = userJPARepository;
        this.productJPARepository = productJPARepository;
        this.tagJPARepository = tagJPARepository;
    }

    @Override
    public List<Post> findAll() {
        return postJPARepository.findAll().stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Post> findByPublisherUsername(String username) {
        return postJPARepository.findByPublisher_Username(username).stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Post save(Post post) {
        UserEntity publisherEntity = userJPARepository.findById(post.getPublisher().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        PostEntity entity = postPersistenceMapper.toEntity(post, publisherEntity);

        for (Product product : post.getProducts()) {
            ProductEntity productEntity = productJPARepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + product.getId()));
            entity.getProducts().add(productEntity);
        }

        for (Tag tag : post.getTags()) {
            TagEntity tagEntity = tagJPARepository.findByName(tag.getName())
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity();
                        newTag.setName(tag.getName());
                        return tagJPARepository.save(newTag);
                    });
            entity.getTags().add(tagEntity);
        }

        return postPersistenceMapper.toDomain(postJPARepository.save(entity));
    }
}
