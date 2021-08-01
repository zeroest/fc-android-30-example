package me.zeroest.part3_ch04.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book (
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverLargeUrl") val coverLargeUrl: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String,
): Parcelable
