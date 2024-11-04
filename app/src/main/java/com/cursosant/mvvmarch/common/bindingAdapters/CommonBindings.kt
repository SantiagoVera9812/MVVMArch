package com.cursosant.mvvmarch.common.bindingAdapters

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosant.mvvmarch.R

@BindingAdapter("glideUrl")
fun bindLoadImage(view: ImageView, url: String){

    Glide.with(view.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .error(R.drawable.ic_broken_image)
        .into(view)

}

@BindingAdapter("setVisibility")
fun bindSetVisibility(view: View, isVisible: Boolean) {

    view.visibility = if (isVisible) View.VISIBLE else View.GONE

}

@BindingAdapter("setRating")
fun bindRatingBar(view: AppCompatRatingBar, value: String){

    view.rating = value.toFloat()
}