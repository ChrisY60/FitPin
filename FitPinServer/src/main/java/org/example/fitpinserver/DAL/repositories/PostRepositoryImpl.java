package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.mappers.PostPersistenceMapper;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.business.repositories.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;
    private final PostPersistenceMapper postPersistenceMapper;

    public PostRepositoryImpl(PostJPARepository postJPARepository, PostPersistenceMapper postPersistenceMapper) {
        this.postJPARepository = postJPARepository;
        this.postPersistenceMapper = postPersistenceMapper;
    }

    @Override
    public List<Post> findAll() {
        return postJPARepository.findAll().stream()
                .map(postPersistenceMapper::toDomain)
                .toList();
    }
}
