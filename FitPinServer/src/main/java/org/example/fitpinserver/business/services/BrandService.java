package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand createBrand(String name);
}
