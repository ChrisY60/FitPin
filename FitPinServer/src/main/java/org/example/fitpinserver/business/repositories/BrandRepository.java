package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Brand;

import java.util.List;

public interface BrandRepository {
    List<Brand> findAll();
    List<Brand> searchByName(String query);
}
