package com.kajdasz.sample.pixabay.ui.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kajdasz.sample.pixabay.databinding.ImageOnListBinding
import com.kajdasz.sample.pixabay.domain.model.LocalImage

class ImageListAdapter : ListAdapter<LocalImage, ImageListAdapter.ImageViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocalImage>() {
            override fun areItemsTheSame(oldItem: LocalImage, newItem: LocalImage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: LocalImage, newItem: LocalImage) =
                oldItem == newItem
        }
    }

    private lateinit var onImageClicked: ((image: LocalImage) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageOnListBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
        holder.binding.root.setOnClickListener {
            onImageClicked(image)
        }
    }

    fun setOnMovieClicked(event: ((image: LocalImage) -> Unit)) {
        onImageClicked = event
    }

    class ImageViewHolder(val binding: ImageOnListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: LocalImage) {
            binding.image = image
        }
    }
}
