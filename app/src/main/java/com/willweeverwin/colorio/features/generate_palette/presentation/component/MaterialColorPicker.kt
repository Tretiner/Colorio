package com.willweeverwin.colorio.features.generate_palette.presentation.component

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.willweeverwin.colorio.R

class MaterialColorPicker(private val ctx: Context) : Dialog(ctx) {

    fun getDialog(): Dialog {
        return Dialog(ctx).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.color_picker_layout)
        }
    }

}