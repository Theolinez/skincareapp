package com.example.skincareapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchResponse {
    @SerializedName("count")
    private int count;

    @SerializedName("page")
    private int page;

    @SerializedName("page_count")
    private int pageCount;

    @SerializedName("products")
    private List<Product> products;
    public int getCount() {
        return count;
    }
    public int getPage() {
        return page;
    }
    public int getPageCount() {
        return pageCount;
    }
    public List<Product> getProducts() {
        return products != null ? products : new ArrayList<>();
    }
}
