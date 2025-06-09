package com.example.skincareapp;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {

    private static FavoritesManager instance;
    private List<Product> favorites;

    private FavoritesManager() {
        favorites = new ArrayList<>();
    }

    public static FavoritesManager getInstance() {
        if (instance == null) {
            instance = new FavoritesManager();
        }
        return instance;
    }

    public void addToFavorites(Product product) {
        if (!favorites.contains(product)) {
            favorites.add(product);
        }
    }

    public void removeFromFavorites(Product product) {
        favorites.remove(product);
    }

    public List<Product> getFavorites() {
        return favorites;
    }
}