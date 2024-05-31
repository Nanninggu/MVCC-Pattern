package com.example.MultiVersion.Concurrency.Control_Pattern.controller;

import com.example.MultiVersion.Concurrency.Control_Pattern.dto.ProductEntity;
import com.example.MultiVersion.Concurrency.Control_Pattern.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable("id") int id) {
        ProductEntity product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable("id") int id, @RequestBody ProductEntity newProduct) {
        productService.updateProduct(id, newProduct.getName(), newProduct.getPrice());
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/conflict")
    public String conflict() {
        // Assuming a product with id 1 exists
        int productId = 1;
        String newName = "TEST1";
        int newPrice = 10000;

        productService.generateConflict(productId, newName, newPrice);

        return "Conflict generated";
    }
}
