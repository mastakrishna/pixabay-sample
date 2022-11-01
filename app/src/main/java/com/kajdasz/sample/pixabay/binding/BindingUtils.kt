package com.kajdasz.sample.pixabay.binding

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kajdasz.sample.pixabay.R

private const val NO_IMAGE = "N/A"

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(imageUrl: String) {
    if (imageUrl != NO_IMAGE) {
        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.no_image))
    }
}
