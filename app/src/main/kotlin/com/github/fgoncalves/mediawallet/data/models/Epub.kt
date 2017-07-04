package com.github.fgoncalves.mediawallet.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Epub(
    @SerializedName("isAvailable") @Expose val isAvailable: Boolean?)
