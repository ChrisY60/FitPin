package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.mappers.BrandPersistenceMapper;
import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.domain.models.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJPARepository brandJPARepository;
    private final BrandPersistenceMapper brandPersistenceMapper;

    public BrandRepositoryImpl(BrandJPARepository brandJPARepository, BrandPersistenceMapper brandPersistenceMapper) {
        this.brandJPARepository = brandJPARepository;
        this.brandPersistenceMapper = brandPersistenceMapper;
    }

    @Override
    public List<Brand> searchByName(String query) {
        return brandJPARepository.findByNameContainingIgnoreCase(query).stream()
                .map(brandPersistenceMapper::toDomain)
                .toList();
    }
}
