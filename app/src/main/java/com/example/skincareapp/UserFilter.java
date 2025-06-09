package com.example.skincareapp;

import java.util.List;

public class UserFilter {
    private String productType;      // e.g., "serum"
    private double maxPrice;         // e.g., 30.00
    private List<String> skinConcerns; // e.g., ["acne", "scarring"]

    public UserFilter(String productType, double maxPrice, List<String> skinConcerns) {
        this.productType = productType;
        this.maxPrice = maxPrice;
        this.skinConcerns = skinConcerns;
    }

    public String getProductType() {
        return productType;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public List<String> getSkinConcerns() {
        return skinConcerns;
    }
}
