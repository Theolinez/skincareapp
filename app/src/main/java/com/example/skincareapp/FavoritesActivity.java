package com.example.skincareapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private static final String TAG = "FavouritesActivity";

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private FavoritesManager favoritesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setTitle("Favorites");

        try {
            favoritesManager = FavoritesManager.getInstance(); // Use singleton instance
            initializeViews();
            setupRecyclerView();
            loadFavorites();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing FavoritesActivity", e);
        }
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.favoritesRecyclerView);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(favoritesManager.getFavorites(), product -> {
            // Handle favorite item click if needed
            Log.d(TAG, "Clicked on product: " + product.getName());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadFavorites() {
        List<Product> favorites = favoritesManager.getFavorites();
        if (favorites != null && !favorites.isEmpty()) {
            adapter = new ProductAdapter(favorites, product -> {
                Log.d(TAG, "Clicked on product: " + product.getName());
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "No favorites to display");
            Toast.makeText(this, "No favorites to display", Toast.LENGTH_SHORT).show();
        }
    }
}