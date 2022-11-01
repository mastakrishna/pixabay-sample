package com.kajdasz.sample.pixabay.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kajdasz.sample.pixabay.domain.model.LocalImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _image = MutableLiveData(savedStateHandle.get<LocalImage>("image"))

    val image: LiveData<LocalImage?> = _image
}