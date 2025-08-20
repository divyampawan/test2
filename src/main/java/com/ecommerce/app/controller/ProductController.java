package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDto;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.mapper.ProductMapper;
import com.ecommerce.app.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final IProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(IProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(productMapper.toDtoList(products));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            return productService.getProductById(id)
                    .map(productMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        try {
            // Validate required fields
            if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Product name is required");
            }
            if (productDto.getPrice() == null || productDto.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Product price must be a positive number");
            }
            
            Product product = productMapper.toEntity(productDto);
            Product savedProduct = productService.saveProduct(product);
            
            // Create the location header with the new product's URL
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedProduct.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(productMapper.toDto(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating product: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            // Check if product exists
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Validate required fields
            if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Product name is required");
            }
            if (productDto.getPrice() == null || productDto.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Product price must be a positive number");
            }
            
            // Update the existing product
            Product product = existingProduct.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            product.setImageUrl(productDto.getImageUrl());
            
            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(productMapper.toDto(updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            // Check if product exists
            if (!productService.getProductById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting product: " + e.getMessage());
        }
    }
}
