package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJPARepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
}
