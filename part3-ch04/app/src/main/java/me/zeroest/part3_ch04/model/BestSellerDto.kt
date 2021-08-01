package me.zeroest.part3_ch04.model

import com.google.gson.annotations.SerializedName

data class BestSellerDto (
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>,
)