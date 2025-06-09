package com.example.skincareapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productBrand, productPrice, productDescription;
    private RatingBar productRatingBar;
    private Button btnAddToFavorites, btnAddToRoutine, btnAddToBuyList;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productBrand = findViewById(R.id.productBrand);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productRatingBar = findViewById(R.id.productRatingBar);

        btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        btnAddToRoutine = findViewById(R.id.btnAddToRoutine);
        btnAddToBuyList = findViewById(R.id.btnAddToBuyList);

        // Get product from intent extras (assuming Parcelable or Serializable)
        if (getIntent().hasExtra("product")) {
            currentProduct = (Product) getIntent().getSerializableExtra("product");
            if (currentProduct != null) {
                populateProductDetails(currentProduct);
            }
        }

        btnAddToFavorites.setOnClickListener(v -> {
            if (currentProduct != null) {
                // Add to favorites logic
                Toast.makeText(this, currentProduct.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
                // You can add your repository or DB call here
            }
        });

        btnAddToRoutine.setOnClickListener(v -> {
            if (currentProduct != null) {
                Toast.makeText(this, currentProduct.getName() + " added to routine", Toast.LENGTH_SHORT).show();
                // Add your routine logic here
            }
        });

        btnAddToBuyList.setOnClickListener(v -> {
            if (currentProduct != null) {
                Toast.makeText(this, currentProduct.getName() + " added to to-buy list", Toast.LENGTH_SHORT).show();
                // Add your to-buy list logic here
            }
        });
    }

    private void populateProductDetails(Product product) {
        productName.setText(product.getName());
        productBrand.setText(product.getBrand());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        productDescription.setText(product.getDescription());
        productRatingBar.setRating(product.getRating());

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .into(productImage);
    }
}
