package com.kajdasz.sample.pixabay.network.model

import com.google.gson.annotations.SerializedName

data class ImageSearchResult(
    @SerializedName("hits")
    val images: List<PixaImage>,

    // The total number of hits.
    @SerializedName("total")
    val totalImages: Int,

    // The number of images accessible through the API. By default, the API is limited to return a maximum of 500 images per query.
    @SerializedName("totalHits")
    val totalImagesAccessible: Int,
)
