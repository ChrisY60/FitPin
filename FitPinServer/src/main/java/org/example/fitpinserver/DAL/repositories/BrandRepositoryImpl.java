package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.repositories.elasticsearch.BrandSearchRepository;
import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.example.fitpinserver.DAL.mappers.BrandPersistenceMapper;
import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJPARepository brandJPARepository;
    private final BrandPersistenceMapper brandPersistenceMapper;
    private final BrandSearchRepository brandSearchRepository;
    private final SearchIndexService searchIndexService;

    public BrandRepositoryImpl(BrandJPARepository brandJPARepository, BrandPersistenceMapper brandPersistenceMapper,
                                BrandSearchRepository brandSearchRepository, SearchIndexService searchIndexService) {
        this.brandJPARepository = brandJPARepository;
        this.brandPersistenceMapper = brandPersistenceMapper;
        this.brandSearchRepository = brandSearchRepository;
        this.searchIndexService = searchIndexService;
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

    @Override
    public Optional<Brand> findById(Long id) {
        return brandJPARepository.findById(id).map(brandPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Brand> findByNameIgnoreCase(String name) {
        return brandJPARepository.findByNameIgnoreCase(name).map(brandPersistenceMapper::toDomain);
    }

    @Override
    public Brand save(Brand brand) {
        BrandEntity saved = brandJPARepository.save(new BrandEntity(brand.getName()));
        Brand savedDomain = brandPersistenceMapper.toDomain(saved);
        searchIndexService.indexBrand(savedDomain);
        return savedDomain;
    }
}
