package com.kajdasz.sample.pixabay.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kajdasz.sample.pixabay.R
import com.kajdasz.sample.pixabay.databinding.FragmentSearchBinding
import com.kajdasz.sample.pixabay.domain.model.ImageSearchRepoResult
import com.kajdasz.sample.pixabay.ui.search.list.ImageListAdapter
import com.kajdasz.sample.pixabay.ui.search.list.SearchScrollListener
import com.kajdasz.sample.pixabay.ui.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        const val INIT_QUERY = "fruits"
    }

    private lateinit var binding: FragmentSearchBinding

    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var scrollListener: SearchScrollListener

    private val searchViewModel: ImageSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        searchBox.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN)
            ) {
                searchBox.hideKeyboard()
                searchViewModel.fetchSearch(title = searchBox.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        imageListAdapter = ImageListAdapter()
        movieList.adapter = imageListAdapter
        imageListAdapter.setOnMovieClicked { image ->
//            val dialog = AlertDialog.Builder(requireContext())
//                .setMessage(HtmlCompat.fromHtml(getString(R.string.details_confirmation, image.user), HtmlCompat.FROM_HTML_MODE_COMPACT))
//                .setPositiveButton(android.R.string.ok) { _, _ ->
//                    val action = SearchFragmentDirections.actionShowDetails(image)
//                    findNavController().navigate(action)
//                }
//                .setNegativeButton(android.R.string.cancel, null)
//                .create()
//
//            dialog.show()
            val action = SearchFragmentDirections.actionShowDetails(image)
            findNavController().navigate(action)
        }

        scrollListener = SearchScrollListener(
            fetchMoreAction = {
                searchViewModel.fetchNextPage()
            },
            isLoading = {
                searchViewModel.isLoading.value == true
            },
            hasAllResults = {
                (searchViewModel.searchResult.value as? ImageSearchRepoResult.Data)?.hasAllResults == true
            }
        )

        movieList.addOnScrollListener(scrollListener)

        searchViewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ImageSearchRepoResult.Error -> {
                    errorText.isVisible = true
                    movieList.isVisible = false
                    errorText.text = getString(R.string.search_error, result.exception.message)
                }
                is ImageSearchRepoResult.NoItems -> {
                    errorText.isVisible = true
                    movieList.isVisible = false
                    errorText.text = getString(R.string.search_no_items, searchBox.text.toString())
                }
                is ImageSearchRepoResult.Data -> {
                    errorText.isVisible = false
                    movieList.isVisible = true
                    imageListAdapter.submitList(result.imagesList)
                }
            }
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loading.isInvisible = !isLoading
        }

        if (searchViewModel.isFirstRun) {
            searchViewModel.isFirstRun = false
            initFirstSearch()
        }
    }

    private fun initFirstSearch() {
        binding.searchBox.apply {
            setText(INIT_QUERY)
            hideKeyboard()
        }
        searchViewModel.fetchSearch(title = INIT_QUERY)
    }
}
