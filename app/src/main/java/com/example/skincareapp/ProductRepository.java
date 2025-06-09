package com.example.skincareapp;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private static final String TAG = "ProductRepository";
    private final ProductDao productDao;

    public interface ProductCallback {
        void onProductsLoaded(List<Product> products);
        void onError(String errorMessage);
    }

    public ProductRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "products-db").build();
        productDao = db.productDao();
    }

    public void searchProducts(String query, String category, Double minPrice, Double maxPrice, ProductCallback callback) {
        // Add null safety
        if (callback == null) {
            Log.w(TAG, "Callback is null, cannot proceed with search");
            return;
        }

        try {
            Call<List<Product>> call = ApiClient.getInstance()
                    .getApiService()
                    .getAllProducts();

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> allProducts = response.body();
                            Log.d(TAG, "Received " + allProducts.size() + " products from API");

                            List<Product> filtered = filterProducts(allProducts, query, category, minPrice, maxPrice);
                            Log.d(TAG, "Filtered to " + filtered.size() + " products");

                            callback.onProductsLoaded(filtered);
                        } else {
                            String errorMsg = "API response unsuccessful. Code: " + response.code();
                            Log.e(TAG, errorMsg);
                            callback.onError(errorMsg);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing API response", e);
                        callback.onError("Error processing response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    String errorMsg = "Network error: " + (t.getMessage() != null ? t.getMessage() : "Unknown error");
                    Log.e(TAG, "API call failed", t);
                    callback.onError(errorMsg);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating API call", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    public void searchProductsByBrand(String brand, ProductCallback callback) {
        if (callback == null || brand == null || brand.trim().isEmpty()) {
            Log.w(TAG, "Invalid parameters for brand search");
            return;
        }

        try {
            Call<List<Product>> call = ApiClient.getInstance()
                    .getApiService()
                    .getProductsByBrand(brand);

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = response.body();
                            Log.d(TAG, "Received " + products.size() + " products for brand: " + brand);

                            // Filter for skincare products with null safety
                            List<Product> skincareProducts = new ArrayList<>();
                            for (Product product : products) {
                                if (product != null && product.isSkincareProduct()) {
                                    assignMockDataIfNeeded(product);
                                    skincareProducts.add(product);
                                }
                            }

                            Log.d(TAG, "Filtered to " + skincareProducts.size() + " skincare products");
                            callback.onProductsLoaded(skincareProducts);
                        } else {
                            String errorMsg = "Failed to fetch products by brand. Code: " + response.code();
                            Log.e(TAG, errorMsg);
                            callback.onError(errorMsg);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing brand search response", e);
                        callback.onError("Error processing response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    String errorMsg = "Network error for brand search: " + (t.getMessage() != null ? t.getMessage() : "Unknown error");
                    Log.e(TAG, "Brand search API call failed", t);
                    callback.onError(errorMsg);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating brand search API call", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    private List<Product> filterProducts(List<Product> products, String query, String category, Double minPrice, Double maxPrice) {
        List<Product> filtered = new ArrayList<>();

        if (products == null || products.isEmpty()) {
            Log.w(TAG, "No products to filter");
            return filtered;
        }

        try {
            for (Product product : products) {
                // Add comprehensive null safety
                if (product == null) {
                    continue;
                }

                // Skip products with missing essential data
                if (product.getName() == null || product.getName().trim().isEmpty()) {
                    continue;
                }

                // Focus on skincare products
                if (!product.isSkincareProduct()) {
                    continue;
                }

                // Assign mock data for missing fields
                assignMockDataIfNeeded(product);

                // Apply filters with null safety
                boolean matchesQuery = query == null || query.trim().isEmpty() ||
                        (product.getName() != null && product.getName().toLowerCase().contains(query.toLowerCase())) ||
                        (product.getBrand() != null && product.getBrand().toLowerCase().contains(query.toLowerCase()));

                boolean matchesCategory = category == null || category.isEmpty() ||
                        (product.getType() != null && product.getType().toLowerCase().contains(category.toLowerCase()));

                boolean matchesPrice = (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice);

                if (matchesQuery && matchesCategory && matchesPrice) {
                    filtered.add(product);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error filtering products", e);
        }

        return filtered;
    }

    private void assignMockDataIfNeeded(Product product) {
        if (product == null) {
            return;
        }

        try {
            // Assign random price if not available or zero
            if (product.getPrice() <= 0) {
                product.setPrice(mockPrice());
            }

            // Assign random rating if not available
            if (product.getRating() <= 0) {
                product.setRating((float) mockRating());
            }

            // Add mock skincare concerns
            if (product.getConcerns() == null || product.getConcerns().isEmpty()) {
                product.setConcerns(generateSkincareConcerns());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error assigning mock data", e);
        }
    }

    private List<String> generateSkincareConcerns() {
        List<String> concerns = new ArrayList<>();
        String[] possibleConcerns = {"acne", "dryness", "aging", "sensitivity", "oiliness", "dark spots"};

        try {
            // Randomly assign 1-3 concerns
            int numConcerns = 1 + (int)(Math.random() * 3);
            for (int i = 0; i < numConcerns; i++) {
                String concern = possibleConcerns[(int)(Math.random() * possibleConcerns.length)];
                if (!concerns.contains(concern)) {
                    concerns.add(concern);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error generating concerns", e);
            // Fallback to default concern
            concerns.add("general");
        }

        return concerns;
    }

    public void toggleFavorite(Product product, boolean isFavorite) {
        if (product == null) {
            Log.w(TAG, "Cannot toggle favorite for null product");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ProductEntity entity = new ProductEntity(product);
                entity.setFavorite(isFavorite);
                productDao.insertAll(List.of(entity));
                Log.d(TAG, "Favorite toggled for product: " + product.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error toggling favorite", e);
            }
        });
    }

    private double mockPrice() {
        return 5 + (Math.random() * 45); // $5-$50 range
    }

    private double mockRating() {
        return 3 + (Math.random() * 2); // 3-5 star range
    }
}