package com.example.skincareapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ProductRepository repository;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    private List<Product> currentProducts = new ArrayList<>();

    private EditText searchInput;
    private Spinner categorySpinner;
    private Spinner skinConcernSpinner;
    private RangeSlider priceRangeSlider;
    private Button searchButton;
    private Button clearFiltersButton;

    private boolean isLoading = false;
    private Handler mainHandler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());

        // Set activity title
        setTitle(getString(R.string.main_activity_title));

        Button viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        try {
            repository = new ProductRepository(this);

            initializeViews();
            setupRecyclerView();
            setupSpinners();
            setupListeners();
            setupPriceSlider();

            loadInitialProducts();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            showErrorMessage("Failed to initialize app: " + e.getMessage());
        }
    }

    private void initializeViews() {
        try {
            recyclerView = findViewById(R.id.recyclerView);
            searchInput = findViewById(R.id.searchInput);
            categorySpinner = findViewById(R.id.categorySpinner);
            skinConcernSpinner = findViewById(R.id.skinConcernSpinner);
            priceRangeSlider = findViewById(R.id.priceRangeSlider);
            searchButton = findViewById(R.id.searchButton);
            clearFiltersButton = findViewById(R.id.clearFiltersButton);

            // Set hints
            if (searchInput != null) {
                searchInput.setHint(getString(R.string.search_hint));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            throw e;
        }
    }

    private void setupRecyclerView() {
        try {
            adapter = new ProductAdapter(currentProducts, product -> {
                try {
                    if (repository != null && product != null) {
                        repository.toggleFavorite(product, true);
                        String message = getString(R.string.added_to_favorites, product.getName());
                        showToast(message);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error adding to favorites", e);
                    showToast("Failed to add to favorites");
                }
            });

            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView", e);
            throw e;
        }
    }

    private void setupSpinners() {
        try {
            // Category spinner setup
            if (categorySpinner != null) {
                ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                        this, R.array.product_categories, android.R.layout.simple_spinner_item);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);
            }

            // Skin concern spinner setup
            if (skinConcernSpinner != null) {
                ArrayAdapter<CharSequence> concernAdapter = ArrayAdapter.createFromResource(
                        this, R.array.skin_concerns, android.R.layout.simple_spinner_item);
                concernAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                skinConcernSpinner.setAdapter(concernAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up spinners", e);
        }
    }

    private void setupPriceSlider() {
        try {
            if (priceRangeSlider != null) {
                // Get initial values from resources
                float[] initialValues = getInitialSliderValues();
                priceRangeSlider.setValueFrom(initialValues[0]);
                priceRangeSlider.setValueTo(initialValues[1]);
                priceRangeSlider.setValues(initialValues[0], initialValues[1]);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up price slider", e);
        }
    }

    private float[] getInitialSliderValues() {
        try {
            String[] values = getResources().getStringArray(R.array.initial_slider_values);
            return new float[]{
                    Float.parseFloat(values[0]),
                    Float.parseFloat(values[1])
            };
        } catch (Exception e) {
            Log.w(TAG, "Error getting slider values, using fallback", e);
            // Fallback values
            return new float[]{5.0f, 200.0f};
        }
    }

    private void setupListeners() {
        try {
            if (searchButton != null) {
                searchButton.setOnClickListener(v -> performSearch());
            }
            if (clearFiltersButton != null) {
                clearFiltersButton.setOnClickListener(v -> clearAllFilters());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up listeners", e);
        }
    }

    private void clearAllFilters() {
        try {
            if (searchInput != null) {
                searchInput.setText("");
            }
            if (categorySpinner != null) {
                categorySpinner.setSelection(0); // "All Categories"
            }
            if (skinConcernSpinner != null) {
                skinConcernSpinner.setSelection(0); // "All Concerns"
            }

            if (priceRangeSlider != null) {
                float[] initialValues = getInitialSliderValues();
                priceRangeSlider.setValues(initialValues[0], initialValues[1]);
            }

            loadInitialProducts();
        } catch (Exception e) {
            Log.e(TAG, "Error clearing filters", e);
            showErrorMessage("Failed to clear filters");
        }
    }

    private void loadInitialProducts() {
        if (isLoading || repository == null) {
            return;
        }

        showLoadingState();

        // Add timeout mechanism
        mainHandler.postDelayed(() -> {
            if (isLoading) {
                hideLoadingState();
                showErrorMessage("Request timeout. Please try again.");
            }
        }, 30000); // 30 second timeout

        // Load all products and filter for skincare
        repository.searchProducts("", null, null, null, new ProductRepository.ProductCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                runOnUiThread(() -> {
                    hideLoadingState();
                    updateProductList(products);

                    if (products.isEmpty()) {
                        showToast(getString(R.string.trying_specific_brands));
                        // Try searching by popular skincare brands
                        searchSkincareByBrands();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    hideLoadingState();
                    showErrorMessage(errorMessage);
                    // Fallback to searching by brands
                    searchSkincareByBrands();
                });
            }
        });
    }

    private void searchSkincareByBrands() {
        if (repository == null) {
            return;
        }

        // Search by brands known to have skincare products
        String[] skincareBrands = {"clinique", "maybelline", "revlon", "l'oreal", "nyx"};

        for (String brand : skincareBrands) {
            repository.searchProductsByBrand(brand, new ProductRepository.ProductCallback() {
                @Override
                public void onProductsLoaded(List<Product> products) {
                    runOnUiThread(() -> {
                        if (!products.isEmpty()) {
                            currentProducts.addAll(products);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    // Ignore individual brand errors
                    Log.w(TAG, "Error loading brand " + brand + ": " + errorMessage);
                }
            });
        }
    }

    private void performSearch() {
        if (isLoading || repository == null) {
            return;
        }

        try {
            String query = searchInput != null ? searchInput.getText().toString().trim() : "";
            String selectedCategory = getSelectedCategory();

            List<Float> values = priceRangeSlider != null ? priceRangeSlider.getValues() : List.of(5.0f, 200.0f);
            double minPrice = values.get(0);
            double maxPrice = values.get(1);

            showLoadingState();

            // Add timeout mechanism
            mainHandler.postDelayed(() -> {
                if (isLoading) {
                    hideLoadingState();
                    showErrorMessage("Search timeout. Please try again.");
                }
            }, 30000); // 30 second timeout

            repository.searchProducts(query, selectedCategory, minPrice, maxPrice,
                    new ProductRepository.ProductCallback() {
                        @Override
                        public void onProductsLoaded(List<Product> products) {
                            runOnUiThread(() -> {
                                hideLoadingState();
                                updateProductList(products);

                                if (products.isEmpty()) {
                                    showEmptyState();
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() -> {
                                hideLoadingState();
                                showErrorMessage(errorMessage);
                            });
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error performing search", e);
            hideLoadingState();
            showErrorMessage("Search failed: " + e.getMessage());
        }
    }

    private String getSelectedCategory() {
        try {
            if (categorySpinner != null) {
                String selected = categorySpinner.getSelectedItem().toString();
                String[] categories = getResources().getStringArray(R.array.product_categories);
                // Return null for "All Categories" to indicate no filtering
                return selected.equals(categories[0]) ? null : selected.toLowerCase();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting selected category", e);
        }
        return null;
    }

    private void updateProductList(List<Product> products) {
        try {
            currentProducts.clear();
            if (products != null) {
                currentProducts.addAll(products);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating product list", e);
        }
    }

    private void showLoadingState() {
        isLoading = true;
        showToast(getString(R.string.loading_products));
        if (searchButton != null) {
            searchButton.setEnabled(false);
        }
    }

    private void hideLoadingState() {
        isLoading = false;
        if (searchButton != null) {
            searchButton.setEnabled(true);
        }
    }

    private void showEmptyState() {
        showToast(getString(R.string.no_products_found));
    }

    private void showErrorMessage(String errorMessage) {
        String message = getString(R.string.error_loading_products) + ": " + errorMessage;
        showToast(message);
    }

    private void showToast(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing toast", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handler
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }
}