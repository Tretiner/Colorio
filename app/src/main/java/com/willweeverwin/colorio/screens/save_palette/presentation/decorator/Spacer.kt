package com.willweeverwin.colorio.screens.save_palette.presentation.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Spacer(private val px: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(rect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = px
            }
            left = px
            right = px
            bottom = px
        }
    }
}