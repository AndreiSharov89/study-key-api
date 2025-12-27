package com.example.key_api.data.dto

import com.google.gson.annotations.SerializedName

class NamesResponse(
    // These might be used in Search results, keep them if needed
    val searchType: String?,
    val expression: String?,

    // Add the NameData fields here directly
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("errorMessage") val errorMessage: String?
) : Response()