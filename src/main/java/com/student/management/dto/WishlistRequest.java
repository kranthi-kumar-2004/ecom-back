package com.student.management.dto;

public class WishlistRequest {

    private Long productId;
    private String name;
    private double price;
    private String image;

    public WishlistRequest() {
    }

    public WishlistRequest(Long productId, String name, double price, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
    }
  
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}