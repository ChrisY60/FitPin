package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.business.services.BrandService;
import org.example.fitpinserver.domain.models.Brand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    @Transactional
    public Brand createBrand(String name) {
        String trimmedName = name == null ? "" : name.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Brand name must not be blank");
        }

        return brandRepository.findByNameIgnoreCase(trimmedName)
                .orElseGet(() -> brandRepository.save(new Brand(trimmedName)));
    }
}
