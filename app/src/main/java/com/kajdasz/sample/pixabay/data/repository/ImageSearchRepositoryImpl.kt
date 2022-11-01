package com.kajdasz.sample.pixabay.data.repository

import com.kajdasz.sample.pixabay.domain.ImageDataSource
import com.kajdasz.sample.pixabay.domain.ImageSearchRepository
import com.kajdasz.sample.pixabay.domain.model.ImageSearchRepoResult
import com.kajdasz.sample.pixabay.network.PixabayApiService
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(
    private val pixabayApiService: PixabayApiService,
    private val imageDataSource: ImageDataSource,
) : ImageSearchRepository {
    
    override suspend fun searchImages(title: String, page: Int): ImageSearchRepoResult =
        try {
            val searchResult = pixabayApiService.searchImages(title, page)
            if (page == 1) {
                imageDataSource.clearImages()
            }
            imageDataSource.addImages(searchResult.images.map { it.toLocalImage() })

            val searchImagesList = imageDataSource.getImages()
            if (searchImagesList.isNotEmpty()) {
                ImageSearchRepoResult.Data(
                    imagesList = searchImagesList,
                    hasAllResults = searchImagesList.size >= searchResult.totalImagesAccessible
                )
            } else
                ImageSearchRepoResult.NoItems
        } catch (e: Exception) {
            ImageSearchRepoResult.Error(e)
        }
}
