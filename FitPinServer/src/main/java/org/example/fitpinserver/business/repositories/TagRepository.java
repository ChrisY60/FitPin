package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll();
    List<Tag> searchByName(String query);
}
