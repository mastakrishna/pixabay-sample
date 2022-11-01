package com.kajdasz.sample.pixabay.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalImage(
    val id: Int,
    val tags: String,
    val webformatURL: String,
    val largeImageURL: String,

    val downloads: Int,
    val likes: Int,
    val comments: Int,

    val user: String,
) : Parcelable
