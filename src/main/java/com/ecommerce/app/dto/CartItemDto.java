package com.ecommerce.app.dto;

public class CartItemDto {
    
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Double productPrice;
    private Integer quantity;
    private Double subtotal;
    
    public CartItemDto() {}
    
    public CartItemDto(Long id, Long productId, String productName, String productImageUrl, Double productPrice, Integer quantity, Double subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
    
    public Double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
} 