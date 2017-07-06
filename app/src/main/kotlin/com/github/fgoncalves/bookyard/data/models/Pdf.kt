package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pdf(
    @SerializedName("isAvailable") @Expose val isAvailable: Boolean?)
