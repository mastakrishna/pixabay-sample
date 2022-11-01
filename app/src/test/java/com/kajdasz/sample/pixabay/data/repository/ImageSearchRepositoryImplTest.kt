package com.kajdasz.sample.pixabay.data.repository

import com.kajdasz.sample.pixabay.domain.ImageDataSource
import com.kajdasz.sample.pixabay.domain.model.ImageSearchRepoResult
import com.kajdasz.sample.pixabay.domain.model.LocalImage
import com.kajdasz.sample.pixabay.network.PixabayApiService
import com.kajdasz.sample.pixabay.network.model.ImageSearchResult
import com.kajdasz.sample.pixabay.network.model.PixaImage
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ImageSearchRepositoryImplTest {

    @MockK
    private lateinit var pixabayApiService: PixabayApiService

    @MockK
    private lateinit var imageDataSource: ImageDataSource

    private lateinit var imageSearchRepositoryImpl: ImageSearchRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coEvery { imageDataSource.clearImages() } just Runs
        coEvery { imageDataSource.addImages(any()) } just Runs

        imageSearchRepositoryImpl = ImageSearchRepositoryImpl(pixabayApiService, imageDataSource)
    }

    @Test
    fun `given api service and data source returning no images when searching for images return NoItems`() = runBlocking {
        val imageSearchResult = ImageSearchResult(
            images = emptyList(),
            totalImages = 0,
            totalImagesAccessible = 0,
        )
        coEvery { pixabayApiService.searchImages(any(), any()) } returns imageSearchResult
        coEvery { imageDataSource.getImages() } returns emptyList()

        val title = "title"
        val page = 1
        val result = imageSearchRepositoryImpl.searchImages(title, page)

        coVerify {
            pixabayApiService.searchImages(title, page)
            imageDataSource.clearImages()
            imageDataSource.addImages(emptyList())
            imageDataSource.getImages()
        }

        assertEquals(ImageSearchRepoResult.NoItems, result)
    }

    @Test
    fun `given api service throwing exception when searching for images return Error`() = runBlocking {
        val exception = Exception("error")
        coEvery { pixabayApiService.searchImages(any(), any()) } throws exception

        val title = "title"
        val page = 1
        val result = imageSearchRepositoryImpl.searchImages(title, page)

        coVerify {
            pixabayApiService.searchImages(title, page)
        }

        verify(exactly = 0) {
            imageDataSource.clearImages()
            imageDataSource.addImages(any())
            imageDataSource.getImages()
        }

        assertEquals(ImageSearchRepoResult.Error(exception), result)
    }

    @Test
    fun `given api service and data source returning images less than total accessible when searching for images return Data with hasAllResults false`() = runBlocking {
        val pixaImage = mockk<PixaImage>()
        val localImage = mockk<LocalImage>()
        every { pixaImage.toLocalImage() } returns localImage

        val pixaImages = listOf(pixaImage)
        val localImages = listOf(localImage)

        val imageSearchResult = ImageSearchResult(
            images = pixaImages,
            totalImages = 1,
            totalImagesAccessible = 2,
        )
        coEvery { pixabayApiService.searchImages(any(), any()) } returns imageSearchResult
        coEvery { imageDataSource.getImages() } returns localImages

        val title = "title"
        val page = 1
        val result = imageSearchRepositoryImpl.searchImages(title, page)

        coVerify {
            pixabayApiService.searchImages(title, page)
            imageDataSource.clearImages()
            imageDataSource.addImages(localImages)
            imageDataSource.getImages()
        }

        assertEquals(
            ImageSearchRepoResult.Data(
                imagesList = localImages,
                hasAllResults = false,
            ),
            result
        )
    }

    @Test
    fun `given api service and data source returning images equal to total accessible when searching for images return Data with hasAllResults true`() = runBlocking {
        val pixaImage = mockk<PixaImage>()
        val localImage = mockk<LocalImage>()
        val localImage2 = mockk<LocalImage>()
        every { pixaImage.toLocalImage() } returns localImage

        val pixaImages = listOf(pixaImage)
        val imagesConvertedToLocal = listOf(localImage)
        val localImages = listOf(localImage, localImage2)

        val imageSearchResult = ImageSearchResult(
            images = pixaImages,
            totalImages = 2,
            totalImagesAccessible = 2,
        )
        coEvery { pixabayApiService.searchImages(any(), any()) } returns imageSearchResult
        coEvery { imageDataSource.getImages() } returns localImages

        val title = "title"
        val page = 2
        val result = imageSearchRepositoryImpl.searchImages(title, page)

        coVerify {
            pixabayApiService.searchImages(title, page)
            imageDataSource.addImages(imagesConvertedToLocal)
            imageDataSource.getImages()
        }

        verify(exactly = 0) {
            imageDataSource.clearImages()
        }

        assertEquals(
            ImageSearchRepoResult.Data(
                imagesList = localImages,
                hasAllResults = true,
            ),
            result
        )
    }
}