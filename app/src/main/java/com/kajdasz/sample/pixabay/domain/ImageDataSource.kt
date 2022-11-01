package com.kajdasz.sample.pixabay.domain

import com.kajdasz.sample.pixabay.domain.model.LocalImage

interface ImageDataSource {
    fun clearImages()

    fun addImages(images: List<LocalImage>)

    fun getImages() : List<LocalImage>

    fun getImage(id: Int) : LocalImage?
}