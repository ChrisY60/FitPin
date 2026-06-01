package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.business.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByPublisherUsername(username);
    }

    @Override
    @Transactional
    public Post createPost(String publisherUsername, String caption, String imageUrl, List<Long> productIds, List<String> tagNames) {
        User publisher = userRepository.findByUsername(publisherUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post(caption, Instant.now(), publisher);
        post.setImageUrl(imageUrl);

        productRepository.findAllByIds(productIds).forEach(post::addProduct);

        tagNames.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .distinct()
                .forEach(name -> post.getTags().add(new Tag(null, name)));

        return postRepository.save(post);
    }
}
