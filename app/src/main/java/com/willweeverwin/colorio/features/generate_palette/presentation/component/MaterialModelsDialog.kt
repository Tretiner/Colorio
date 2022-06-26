package com.willweeverwin.colorio.features.generate_palette.presentation.component

import android.content.Context
import androidx.appcompat.app.AlertDialog

class MaterialModelsDialog(
    ctx: Context,
    private val onChoice: (Int) -> Unit
) {
    private var dialogBuilder = AlertDialog
        .Builder(ctx)
        .setTitle("Options")

    fun makeDialog(
        items: Collection<String>,
        checkedInd: Int,
    ): AlertDialog = dialogBuilder
        .setSingleChoiceItems(items.toTypedArray(), checkedInd) { _, which -> onChoice(which) }
        .create()
}