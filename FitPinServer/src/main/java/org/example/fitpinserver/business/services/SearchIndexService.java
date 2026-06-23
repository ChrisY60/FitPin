package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;

public interface SearchIndexService {
    void indexUser(User user);
    void deleteUser(Long userId);
    void indexTag(Tag tag);
    void indexProduct(Product product);
    void indexBrand(Brand brand);
}
