package com.github.fgoncalves.mediawallet.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchInfo(
    @SerializedName("textSnippet") @Expose val textSnippet: String?)
