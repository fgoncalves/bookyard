package com.github.fgoncalves.bookyard.presentation.utils

import android.databinding.BindingAdapter
import android.widget.ImageView
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
