package com.psb.searchablespinner.common.internal

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.psb.searchablespinner.R

internal object Utilities {
    fun fetchPrimaryColor(context: Context): Int {
        val typedValue = TypedValue()
        val a: TypedArray =
            context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }
}