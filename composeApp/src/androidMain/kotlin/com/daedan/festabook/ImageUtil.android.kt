package com.daedan.festabook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.createBitmap

fun vectorToBitmap(
    context: Context,
    vectorResId: Int,
): Bitmap {
    val drawable = AppCompatResources.getDrawable(context, vectorResId)!!
    val bitmap =
        createBitmap(
            drawable.intrinsicWidth.coerceAtLeast(1),
            drawable.intrinsicHeight.coerceAtLeast(1),
        )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
