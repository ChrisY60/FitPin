package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.elasticsearch.BrandSearchRepository;
import org.example.fitpinserver.DAL.mappers.BrandPersistenceMapper;
import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.domain.models.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJPARepository brandJPARepository;
    private final BrandPersistenceMapper brandPersistenceMapper;
    private final BrandSearchRepository brandSearchRepository;

    public BrandRepositoryImpl(BrandJPARepository brandJPARepository, BrandPersistenceMapper brandPersistenceMapper,
                                BrandSearchRepository brandSearchRepository) {
        this.brandJPARepository = brandJPARepository;
        this.brandPersistenceMapper = brandPersistenceMapper;
        this.brandSearchRepository = brandSearchRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandJPARepository.findAll().stream()
                .map(brandPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Brand> searchByName(String query) {
        return brandSearchRepository.searchByName(query).stream()
                .map(document -> new Brand(document.getId(), document.getName()))
                .toList();
    }
}
