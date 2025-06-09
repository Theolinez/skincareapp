package com.example.skincareapp;

import java.util.ArrayList;
import java.util.List;

public class ProductFilterUtil {

    public static List<Product> localFilterProducts(List<Product> products, List<String> concerns) {
        List<Product> filtered = new ArrayList<>();
        if (products == null || products.isEmpty()) return filtered;  // Defensive check

        for (Product product : products) {
            if (matchesConcerns(product, concerns)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    private static boolean matchesConcerns(Product product, List<String> concerns) {
        if (product == null) return false;  // null product can't match

        if (concerns == null || concerns.isEmpty()) {
            return true; // No filtering criteria, so everything matches
        }

        List<String> productConcerns = product.getConcerns();  // Safe getter that returns empty list if null
        for (String concern : concerns) {
            if (!productConcerns.contains(concern)) {
                return false;
            }
        }
        return true;
    }
}
