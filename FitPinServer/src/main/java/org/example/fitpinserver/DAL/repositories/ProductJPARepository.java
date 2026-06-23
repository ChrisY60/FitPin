package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJPARepository extends JpaRepository<ProductEntity, Long> {
}
