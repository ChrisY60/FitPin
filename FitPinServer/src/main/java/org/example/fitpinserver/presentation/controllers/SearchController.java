package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.SearchService;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.presentation.dtos.BrandDTO;
import org.example.fitpinserver.presentation.dtos.ProductDTO;
import org.example.fitpinserver.presentation.dtos.SearchResponseDTO;
import org.example.fitpinserver.presentation.dtos.SearchUserDTO;
import org.example.fitpinserver.presentation.dtos.TagDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final String PRODUCTS = "products";
    private static final String BRANDS = "brands";
    private static final String USERS = "users";
    private static final String TAGS = "tags";

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public SearchResponseDTO search(@RequestParam(required = false, defaultValue = "") String q,
                                    @RequestParam(required = false) String type) {
        boolean all = type == null || type.isBlank();

        List<ProductDTO> products = all || PRODUCTS.equalsIgnoreCase(type) ? searchProducts(q) : List.of();
        List<BrandDTO> brands = all || BRANDS.equalsIgnoreCase(type) ? searchBrands(q) : List.of();
        List<SearchUserDTO> users = all || USERS.equalsIgnoreCase(type) ? searchUsers(q) : List.of();
        List<TagDTO> tags = all || TAGS.equalsIgnoreCase(type) ? searchTags(q) : List.of();

        return new SearchResponseDTO(products, brands, users, tags);
    }

    private List<ProductDTO> searchProducts(String q) {
        return searchService.searchProducts(q).stream()
                .map(this::toProductDTO)
                .toList();
    }

    private List<BrandDTO> searchBrands(String q) {
        return searchService.searchBrands(q).stream()
                .map(brand -> new BrandDTO(brand.getId(), brand.getName()))
                .toList();
    }

    private List<SearchUserDTO> searchUsers(String q) {
        return searchService.searchUsers(q).stream()
                .map(user -> new SearchUserDTO(user.getId(), user.getUsername(), user.getProfilePictureUrl()))
                .toList();
    }

    private List<TagDTO> searchTags(String q) {
        return searchService.searchTags(q).stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .toList();
    }

    private ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                new BrandDTO(product.getBrand().getId(), product.getBrand().getName()),
                product.getImageUrl()
        );
    }
}
