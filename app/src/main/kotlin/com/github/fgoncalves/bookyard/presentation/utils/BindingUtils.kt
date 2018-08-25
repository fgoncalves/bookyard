package com.github.fgoncalves.bookyard.presentation.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.fgoncalves.bookyard.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) =
        if (url == null || url.isBlank()) {
            view.setImageResource(R.mipmap.placeholder)
        } else {
            Picasso.with(view.context)
                    .load(url)
                    .error(R.mipmap.placeholder)
                    .into(view)
        }

@BindingAdapter("imageUri")
fun loadImage(view: ImageView, uri: Uri?) = loadImage(view, uri?.toString())
