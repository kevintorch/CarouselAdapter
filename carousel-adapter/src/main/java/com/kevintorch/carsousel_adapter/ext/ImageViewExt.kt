package com.kevintorch.carsousel_adapter.ext

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

@JvmOverloads
internal fun ImageView.load(
    uri: Uri? = null,
    url: String? = null,
    file: File? = null,
    drawable: Drawable? = null,
    placeholder: Drawable? = null,
    placeholderDrawableResId: Int = 0,
    error: Drawable? = null,
    onLoadFailed: () -> Boolean = { false },
    onResourceReady: () -> Boolean = { false }
) {
    Glide.with(context)
        .load(uri ?: url ?: file ?: drawable)
        .placeholder(placeholder)
        .placeholder(placeholderDrawableResId)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean = onLoadFailed()

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean = onResourceReady()
        })
        .error(error)
        .into(this)
}