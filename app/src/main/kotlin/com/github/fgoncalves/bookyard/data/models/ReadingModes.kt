package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReadingModes(
    @SerializedName("text") @Expose val text: Boolean?,
    @SerializedName("image") @Expose val image: Boolean?)
