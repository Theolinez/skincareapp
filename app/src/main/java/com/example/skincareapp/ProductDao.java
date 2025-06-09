package com.example.skincareapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    List<ProductEntity> getAll();

    @Query("SELECT * FROM products WHERE isFavorite = 1")
    LiveData<List<ProductEntity>> getFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductEntity> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity product);

    @Query("UPDATE products SET isFavorite = :isFavorite WHERE id = :productId")
    void updateFavorite(String productId, boolean isFavorite);

    @Query("SELECT * FROM products WHERE productType = :productType AND price BETWEEN :minPrice AND :maxPrice")
    LiveData<List<ProductEntity>> filterProducts(String productType, double minPrice, double maxPrice);

    @Query("DELETE FROM products")
    void deleteAll();
}
