package com.kajdasz.sample.pixabay.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kajdasz.sample.pixabay.domain.model.ImageSearchRepoResult
import com.kajdasz.sample.pixabay.domain.ImageSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val imageSearchRepository: ImageSearchRepository
) : ViewModel() {

    var isFirstRun = true

    private var lastSearchedTitle: String? = null
    private var lastFetchedPage = 0

    val searchResult: LiveData<ImageSearchRepoResult>
        get() = _searchResult

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _searchResult = MutableLiveData<ImageSearchRepoResult>()

    private val _isLoading = MutableLiveData<Boolean>()

    fun fetchNextPage() {
        fetchSearch(page = lastFetchedPage + 1)
    }

    fun fetchSearch(title: String? = lastSearchedTitle, page: Int = 1) {
        if (title == null) return

        lastSearchedTitle = title
        lastFetchedPage = page

        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = imageSearchRepository.searchImages(title, page)
            _searchResult.postValue(response)
            _isLoading.postValue(false)
        }
    }
}
