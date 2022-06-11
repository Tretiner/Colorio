package com.willweeverwin.colorio.ui.component

import android.content.Context
import androidx.appcompat.app.AlertDialog


class MaterialMenuDialog(
    private val ctx: Context,
    private val callback: (Int) -> Unit
) {
    fun make(items: Array<String>, checkedInd: Int) = AlertDialog
        .Builder(ctx)
        .setTitle("Options")
        .setSingleChoiceItems(items, checkedInd) { _, which -> callback(which) }
        .create()
}