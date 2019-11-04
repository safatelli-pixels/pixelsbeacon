package com.pixelstrade.audioguide.rest;


import com.pixelstrade.audioguide.models.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {


    @GET("api/articles")
    Call<ArticleResponse> getArticles();
}
