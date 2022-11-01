package com.kajdasz.sample.pixabay.data

import com.kajdasz.sample.pixabay.domain.ImageDataSource
import com.kajdasz.sample.pixabay.domain.model.LocalImage

class ImageInMemoryDataSource: ImageDataSource {
    private val imagesCache = mutableListOf<LocalImage>()

    override fun clearImages() {
        imagesCache.clear()
    }

    override fun addImages(images: List<LocalImage>) {
        imagesCache.addAll(images)
    }

    override fun getImages(): List<LocalImage> = imagesCache.toList()

    override fun getImage(id: Int) : LocalImage? = imagesCache.find { it.id == id }
}
