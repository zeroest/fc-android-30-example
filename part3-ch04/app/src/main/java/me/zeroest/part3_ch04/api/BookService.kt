package me.zeroest.part3_ch04.api

import me.zeroest.part3_ch04.model.BestSellerDto
import me.zeroest.part3_ch04.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json")
    fun getBestSeller(
        @Query("key") apiKey: String,
        @Query("categoryId") categoryId: String
    ): Call<BestSellerDto>

}