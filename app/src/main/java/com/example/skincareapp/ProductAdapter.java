package com.example.skincareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private FavoritesManager favoritesManager;
    private OnProductClickListener onProductClickListener;

    public ProductAdapter(List<Product> products, OnProductClickListener onProductClickListener) {
        this.products = products;
        this.favoritesManager = FavoritesManager.getInstance(); // Use singleton instance
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        // Update favorite icon based on whether the product is in favorites
        if (favoritesManager.getFavorites().contains(product)) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite); // Filled heart icon
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border); // Empty heart icon
        }

        // Set click listener for the favorite icon
        holder.favoriteIcon.setOnClickListener(v -> {
            if (favoritesManager.getFavorites().contains(product)) {
                favoritesManager.getFavorites().remove(product);
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(holder.itemView.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                favoritesManager.addToFavorites(product);
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(holder.itemView.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView favoriteIcon;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
}