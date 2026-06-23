package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductJPARepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByNameIgnoreCaseAndBrand_Id(String name, Long brandId);
}
