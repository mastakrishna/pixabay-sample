package com.kajdasz.sample.pixabay.domain.model

sealed class ImageSearchRepoResult {
    data class Data(
        val imagesList: List<LocalImage>,
        val hasAllResults: Boolean
    ) : ImageSearchRepoResult()

    object NoItems : ImageSearchRepoResult()

    data class Error(
        val exception: Exception
    ) : ImageSearchRepoResult()
}
