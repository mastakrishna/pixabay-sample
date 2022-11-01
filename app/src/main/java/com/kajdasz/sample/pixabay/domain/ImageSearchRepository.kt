package com.kajdasz.sample.pixabay.domain

import com.kajdasz.sample.pixabay.domain.model.ImageSearchRepoResult

interface ImageSearchRepository {
    suspend fun searchImages(title: String, page: Int): ImageSearchRepoResult
}