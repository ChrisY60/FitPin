package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.mappers.TagPersistenceMapper;
import org.example.fitpinserver.business.repositories.TagRepository;
import org.example.fitpinserver.domain.models.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagJPARepository tagJPARepository;
    private final TagPersistenceMapper tagPersistenceMapper;

    public TagRepositoryImpl(TagJPARepository tagJPARepository, TagPersistenceMapper tagPersistenceMapper) {
        this.tagJPARepository = tagJPARepository;
        this.tagPersistenceMapper = tagPersistenceMapper;
    }

    @Override
    public List<Tag> searchByName(String query) {
        return tagJPARepository.findByNameContainingIgnoreCase(query).stream()
                .map(tagPersistenceMapper::toDomain)
                .toList();
    }
}
