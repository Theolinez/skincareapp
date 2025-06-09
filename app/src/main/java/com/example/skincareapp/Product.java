package com.example.skincareapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Product {
    @SerializedName("id")
    private Integer id; // Changed to Integer to handle null

    @SerializedName("name")
    private String name;

    @SerializedName("brand")
    private String brand;

    @SerializedName("product_type")
    private String type;

    @SerializedName("description")
    private String description;

    @SerializedName("image_link")
    private String imageUrl;

    @SerializedName("price")
    private String priceString;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("category")
    private String category;

    @SerializedName("product_colors")
    private List<ProductColor> productColors;

    // Local-only fields
    private List<String> concerns;
    private boolean isFavorite = false;

    // Default constructor
    public Product() {
        // Initialize collections to prevent null pointer exceptions
        this.productColors = new ArrayList<>();
        this.concerns = new ArrayList<>();
    }

    // Inner class for product colors
    public static class ProductColor {
        @SerializedName("hex_value")
        private String hexValue;

        @SerializedName("colour_name")
        private String colourName;

        // Null-safe getters
        public String getHexValue() {
            return hexValue != null ? hexValue : "#000000"; // Default black color
        }

        public String getColourName() {
            return colourName != null ? colourName : "Unknown Color";
        }
    }

    // Null-safe getters with proper defaults
    public String getId() {
        if (id != null) {
            return String.valueOf(id);
        }
        // Generate a fallback ID based on name and brand
        String fallbackName = (name != null && !name.trim().isEmpty()) ? name.trim() : "unknown";
        String fallbackBrand = (brand != null && !brand.trim().isEmpty()) ? brand.trim() : "brand";
        return (fallbackName + "_" + fallbackBrand + "_" + System.currentTimeMillis()).replaceAll("\\s+", "_");
    }

    public String getName() {
        return (name != null && !name.trim().isEmpty()) ? name.trim() : "Unknown Product";
    }

    public String getBrand() {
        return (brand != null && !brand.trim().isEmpty()) ? brand.trim() : "Unknown Brand";
    }

    public String getType() {
        return (type != null && !type.trim().isEmpty()) ? type.trim() : "beauty";
    }

    public String getDescription() {
        return (description != null && !description.trim().isEmpty()) ? description.trim() : "No description available";
    }

    public String getImageUrl() {
        // Return null for empty/whitespace strings to let Glide handle placeholder
        return (imageUrl != null && !imageUrl.trim().isEmpty()) ? imageUrl.trim() : null;
    }

    public double getPrice() {
        if (priceString != null && !priceString.trim().isEmpty()) {
            try {
                // Remove any currency symbols and parse
                String cleanPrice = priceString.trim().replaceAll("[^\\d.]", "");
                if (!cleanPrice.isEmpty()) {
                    return Double.parseDouble(cleanPrice);
                }
            } catch (NumberFormatException e) {
                // Log the error if you have logging available
                // Log.w("Product", "Invalid price format: " + priceString, e);
            }
        }
        return 0.0;
    }

    public float getRating() {
        if (rating != null && rating >= 0 && rating <= 5) {
            return rating.floatValue();
        }
        return 0.0f;
    }

    public String getCategory() {
        return (category != null && !category.trim().isEmpty()) ? category.trim() : "General";
    }

    public List<ProductColor> getProductColors() {
        if (productColors == null) {
            productColors = new ArrayList<>();
        }
        return productColors;
    }

    public List<String> getConcerns() {
        if (concerns == null) {
            concerns = new ArrayList<>();
        }
        return concerns;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // Null-safe setters with validation
    public void setPrice(double price) {
        if (price >= 0) {
            this.priceString = String.valueOf(price);
        }
    }

    public void setRating(float rating) {
        if (rating >= 0 && rating <= 5) {
            this.rating = (double) rating;
        }
    }

    public void setConcerns(List<String> concerns) {
        if (concerns != null) {
            // Filter out null or empty strings
            this.concerns = new ArrayList<>();
            for (String concern : concerns) {
                if (concern != null && !concern.trim().isEmpty()) {
                    this.concerns.add(concern.trim().toLowerCase());
                }
            }
        } else {
            this.concerns = new ArrayList<>();
        }
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    // Additional null-safe setters
    public void setName(String name) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
    }

    public void setBrand(String brand) {
        this.brand = (brand != null && !brand.trim().isEmpty()) ? brand.trim() : null;
    }

    public void setType(String type) {
        this.type = (type != null && !type.trim().isEmpty()) ? type.trim() : null;
    }

    public void setDescription(String description) {
        this.description = (description != null && !description.trim().isEmpty()) ? description.trim() : null;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = (imageUrl != null && !imageUrl.trim().isEmpty()) ? imageUrl.trim() : null;
    }

    public void setCategory(String category) {
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
    }

    public void setProductColors(List<ProductColor> productColors) {
        if (productColors != null) {
            this.productColors = new ArrayList<>(productColors);
        } else {
            this.productColors = new ArrayList<>();
        }
    }

    // Enhanced helper method to check if this is a skincare product with null safety
    public boolean isSkincareProduct() {
        // Check product type first
        if (type != null && !type.trim().isEmpty()) {
            String lowerType = type.trim().toLowerCase();
            if (containsSkincareKeywords(lowerType)) {
                return true;
            }
        }

        // Check product name as fallback
        if (name != null && !name.trim().isEmpty()) {
            String lowerName = name.trim().toLowerCase();
            if (containsSkincareKeywords(lowerName)) {
                return true;
            }
        }

        // Check brand for skincare-specific brands
        if (brand != null && !brand.trim().isEmpty()) {
            String lowerBrand = brand.trim().toLowerCase();
            if (isSkincareBrand(lowerBrand)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsSkincareKeywords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String[] skincareKeywords = {
                "cleanser", "moisturizer", "serum", "toner", "sunscreen",
                "face", "skincare", "foundation", "lipstick", "mascara",
                "cream", "lotion", "gel", "mask", "exfoliant", "retinol",
                "vitamin c", "hyaluronic", "niacinamide", "salicylic",
                "glycolic", "peptide", "antioxidant", "spf"
        };

        for (String keyword : skincareKeywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSkincareBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return false;
        }

        String[] skincareBrands = {
                "cetaphil", "cerave", "neutrogena", "olay", "clinique",
                "la roche posay", "eucerin", "aveeno", "skinceuticals"
        };

        for (String skincareBrand : skincareBrands) {
            if (brand.contains(skincareBrand)) {
                return true;
            }
        }
        return false;
    }

    // Utility method to check if product has valid essential data
    public boolean hasValidData() {
        return (name != null && !name.trim().isEmpty()) &&
                (brand != null && !brand.trim().isEmpty()) &&
                getId() != null;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Product{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", brand='" + getBrand() + '\'' +
                ", type='" + getType() + '\'' +
                ", price=" + getPrice() +
                ", rating=" + getRating() +
                ", isFavorite=" + isFavorite +
                '}';
    }

    // Override equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        String thisId = getId();
        String otherId = product.getId();

        return thisId != null && thisId.equals(otherId);
    }

    @Override
    public int hashCode() {
        String id = getId();
        return id != null ? id.hashCode() : 0;
    }
}