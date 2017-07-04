package com.github.fgoncalves.mediawallet.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaleInfo(
    @SerializedName("country") @Expose val country: String?,
    @SerializedName("saleability") @Expose val saleability: String?,
    @SerializedName("isEbook") @Expose val isEbook: Boolean?)
