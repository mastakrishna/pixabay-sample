package com.kajdasz.sample.pixabay.di

import com.kajdasz.sample.pixabay.data.ImageInMemoryDataSource
import com.kajdasz.sample.pixabay.domain.ImageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun provideImageDataSource(): ImageDataSource {
        return ImageInMemoryDataSource()
    }
}
