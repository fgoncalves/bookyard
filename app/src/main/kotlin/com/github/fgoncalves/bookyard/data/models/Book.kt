package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("kind") @Expose val kind: String?,
    @SerializedName("totalItems") @Expose val totalItems: Int?,
    @SerializedName("items") @Expose val items: List<Item>?)
