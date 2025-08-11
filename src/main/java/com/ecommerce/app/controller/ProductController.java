package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDto;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.mapper.ProductMapper;
import com.ecommerce.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Temporarily disabled - using ApiController instead
//@RestController
//@RequestMapping("/api/v1/products")
//@CrossOrigin(origins = "*")
@Deprecated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(productMapper.toDtoList(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(productMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(productMapper.toDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(productMapper.toDto(updatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
