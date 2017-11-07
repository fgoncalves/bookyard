package com.github.fgoncalves.bookyard.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccessInfo(
        @SerializedName("country") @Expose val country: String?,
        @SerializedName("viewability") @Expose val viewability: String?,
        @SerializedName("textToSpeechPermission") @Expose val textToSpeechPermission: String?,
        @SerializedName("embeddable") @Expose val embeddable: Boolean?,
        @SerializedName("publicDomain") @Expose val publicDomain: Boolean?,
        @SerializedName("quoteSharingAllowed") @Expose val quoteSharingAllowed: Boolean?,
        @SerializedName("epub") @Expose val epub: Epub?,
        @SerializedName("pdf") @Expose val pdf: Pdf?,
        @SerializedName("webReaderLink") @Expose val webReaderLink: String?,
        @SerializedName("accessViewStatus") @Expose val accessViewStatus: String?)
