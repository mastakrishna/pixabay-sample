package com.kajdasz.sample.pixabay.di

import com.kajdasz.sample.pixabay.domain.ImageDataSource
import com.kajdasz.sample.pixabay.data.repository.ImageSearchRepositoryImpl
import com.kajdasz.sample.pixabay.domain.ImageSearchRepository
import com.kajdasz.sample.pixabay.network.PixabayApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DataModule {
    @Provides
    @ViewModelScoped
    fun provideImageSearchRepository(
        pixabayApiService: PixabayApiService,
        imageDataSource: ImageDataSource,
    ): ImageSearchRepository {
        return ImageSearchRepositoryImpl(pixabayApiService, imageDataSource)
    }
}
