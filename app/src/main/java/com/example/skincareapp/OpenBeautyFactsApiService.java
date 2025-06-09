package com.example.skincareapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface OpenBeautyFactsApiService {
    @GET("cgi/search.pl")
    Call<ProductSearchResponse> searchProducts(
            @Query("search_terms") String query,
            @Query("search_simple") int simple,
            @Query("json") int json,
            @Query("page") int page
    );

}

