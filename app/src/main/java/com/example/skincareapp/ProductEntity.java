package com.example.skincareapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")

public class ProductEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String productType;
    private double price;
    private boolean isFavorite;
    private String imageUrl;
    private float rating;

    public ProductEntity() {
        // Required empty constructor for Room
    }

    public ProductEntity(String id, String name, String productType, double price, boolean isFavorite, String imageUrl, float rating) {
        this.id = id;
        this.name = name;
        this.productType = productType;
        this.price = price;
        this.isFavorite = isFavorite;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public ProductEntity(Product product) {
        String productId = product.getId();
        this.id = productId != null ? productId : (product.getName() + product.getImageUrl());
        this.name = product.getName();
        this.productType = product.getType();
        this.price = product.getPrice();
        this.isFavorite = false; // default, can toggle later
        this.imageUrl = product.getImageUrl();
        this.rating = (float) product.getRating(); // cast if needed
    }
    public void setName(String name) { this.name = name; }
    public void setProductType(String productType) { this.productType = productType; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(float rating) { this.rating = rating; }

    @NonNull public String getId() { return id; }

    public String getName() { return name; }
    public String getProductType() { return productType; }
    public double getPrice() { return price; }
    public boolean isFavorite() { return isFavorite; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }

    public void setId(@NonNull String id) {
        this.id = id;
    }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
