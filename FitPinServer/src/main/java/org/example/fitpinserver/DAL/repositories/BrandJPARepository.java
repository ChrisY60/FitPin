package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandJPARepository extends JpaRepository<BrandEntity, Long> {
    List<BrandEntity> findByNameContainingIgnoreCase(String name);
}
