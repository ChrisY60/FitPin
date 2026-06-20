package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.TagEntity;
import org.example.fitpinserver.domain.models.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagPersistenceMapper {

    public Tag toDomain(TagEntity entity) {
        return new Tag(entity.getId(), entity.getName());
    }
}
