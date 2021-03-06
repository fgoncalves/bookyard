package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item(
        @SerializedName("kind") @Expose val kind: String?,
        @SerializedName("id") @Expose val id: String?,
        @SerializedName("etag") @Expose val etag: String?,
        @SerializedName("selfLink") @Expose val selfLink: String?,
        @SerializedName("volumeInfo") @Expose val volumeInfo: VolumeInfo?,
        @SerializedName("saleInfo") @Expose val saleInfo: SaleInfo?,
        @SerializedName("accessInfo") @Expose val accessInfo: AccessInfo?,
        @SerializedName("searchInfo") @Expose val searchInfo: SearchInfo?) {

    val title
        get() = volumeInfo?.title ?: ""

    val authors
        get() = volumeInfo?.authors?.joinToString(",") ?: ""

    val cover
        get() = volumeInfo?.imageLinks?.thumbnail ?: ""

    val description
        get() = volumeInfo?.description
}
