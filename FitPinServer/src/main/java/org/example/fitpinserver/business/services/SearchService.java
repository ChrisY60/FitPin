package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;

import java.util.List;

public interface SearchService {
    List<Product> searchProducts(String query);
    List<Brand> searchBrands(String query);
    List<User> searchUsers(String query);
    List<Tag> searchTags(String query);
}
