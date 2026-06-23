package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    List<Brand> findAll();
    List<Brand> searchByName(String query);
    Optional<Brand> findById(Long id);
    Optional<Brand> findByNameIgnoreCase(String name);
    Brand save(Brand brand);
}
