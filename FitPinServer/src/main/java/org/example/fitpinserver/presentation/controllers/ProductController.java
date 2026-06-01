package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.ProductService;
import org.example.fitpinserver.presentation.dtos.BrandDTO;
import org.example.fitpinserver.presentation.dtos.ProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        new BrandDTO(product.getBrand().getId(), product.getBrand().getName()),
                        product.getImageUrl()
                ))
                .toList();
    }
}
