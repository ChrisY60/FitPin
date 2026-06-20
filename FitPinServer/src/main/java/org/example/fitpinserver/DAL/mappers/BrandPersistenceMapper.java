package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.example.fitpinserver.domain.models.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandPersistenceMapper {

    public Brand toDomain(BrandEntity entity) {
        return new Brand(entity.getId(), entity.getName());
    }
}
