package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.entities.TagEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.PostPersistenceMapper;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.business.repositories.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;
    private final PostPersistenceMapper postPersistenceMapper;
    private final UserJPARepository userJPARepository;
    private final ProductJPARepository productJPARepository;
    private final TagJPARepository tagJPARepository;
    private final SearchIndexService searchIndexService;

    public PostRepositoryImpl(PostJPARepository postJPARepository, PostPersistenceMapper postPersistenceMapper,
                              UserJPARepository userJPARepository, ProductJPARepository productJPARepository,
                              TagJPARepository tagJPARepository, SearchIndexService searchIndexService) {
        this.postJPARepository = postJPARepository;
        this.postPersistenceMapper = postPersistenceMapper;
        this.userJPARepository = userJPARepository;
        this.productJPARepository = productJPARepository;
        this.tagJPARepository = tagJPARepository;
        this.searchIndexService = searchIndexService;
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
    public Optional<Post> findById(Long id) {
        return postJPARepository.findById(id).map(postPersistenceMapper::toDomain);
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
                        TagEntity savedTag = tagJPARepository.save(newTag);
                        searchIndexService.indexTag(new Tag(savedTag.getId(), savedTag.getName()));
                        return savedTag;
                    });
            entity.getTags().add(tagEntity);
        }

        return postPersistenceMapper.toDomain(postJPARepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        postJPARepository.deleteById(id);
    }

    @Override
    public List<Post> findByProductId(Long productId) {
        return postJPARepository.findByProducts_Id(productId).stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Post> findByBrandId(Long brandId) {
        return postJPARepository.findByProducts_Brand_Id(brandId).stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Post> findByTagId(Long tagId) {
        return postJPARepository.findByTags_Id(tagId).stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }
}
