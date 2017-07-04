package com.github.fgoncalves.mediawallet.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VolumeInfo(
    @SerializedName("title") @Expose val title: String?,
    @SerializedName("authors") @Expose val authors: List<String>?,
    @SerializedName("publishedDate") @Expose val publishedDate: String?,
    @SerializedName("description") @Expose val description: String?,
    @SerializedName(
        "industryIdentifiers") @Expose val industryIdentifiers: List<IndustryIdentifier>?,
    @SerializedName("readingModes") @Expose val readingModes: ReadingModes?,
    @SerializedName("pageCount") @Expose val pageCount: Int?,
    @SerializedName("printType") @Expose val printType: String?,
    @SerializedName("categories") @Expose val categories: List<String>?,
    @SerializedName("averageRating") @Expose val averageRating: Float?,
    @SerializedName("ratingCount") @Expose val ratingCount: Int?,
    @SerializedName("maturityRating") @Expose val maturityRating: String?,
    @SerializedName("allowAnonLogging") @Expose val allowAnonLogging: Boolean?,
    @SerializedName("contentVersion") @Expose val contentVersion: String?,
    @SerializedName("imageLinks") @Expose val imageLinks: ImageLinks?,
    @SerializedName("language") @Expose val language: String?,
    @SerializedName("previewLink") @Expose val previewLink: String?,
    @SerializedName("infoLink") @Expose val infoLink: String?,
    @SerializedName("canonicalVolumeLink") @Expose val canonicalVolumeLink: String?)
