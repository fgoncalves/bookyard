package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IndustryIdentifier(
    @SerializedName("type") @Expose val type: String?,
    @SerializedName("identifier") @Expose val identifier: String?)
