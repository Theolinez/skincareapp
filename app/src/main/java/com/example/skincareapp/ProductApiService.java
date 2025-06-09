package com.example.skincareapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApiService {
    // Get all products
    @GET("api/v1/products.json")
    Call<List<Product>> getAllProducts();

    // Search by brand
    @GET("api/v1/products.json")
    Call<List<Product>> getProductsByBrand(@Query("brand") String brand);

    // Search by product type
    @GET("api/v1/products.json")
    Call<List<Product>> getProductsByType(@Query("product_type") String productType);

    // Search by brand and type
    @GET("api/v1/products.json")
    Call<List<Product>> searchProducts(
            @Query("brand") String brand,
            @Query("product_type") String productType
    );
}
