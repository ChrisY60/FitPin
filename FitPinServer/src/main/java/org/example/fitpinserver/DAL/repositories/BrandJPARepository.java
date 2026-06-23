package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandJPARepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByNameIgnoreCase(String name);
}
