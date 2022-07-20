package com.willweeverwin.colorio.screens.generate_palette.presentation.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ModelsDialog(
    ctx: Context,
    private val onChoice: (Int) -> Unit
) {

    private val builder = AlertDialog
        .Builder(ctx)
        .setTitle("Models")

    fun setModels(items: Collection<String>, checkedInd: Int){
        dialog = builder
            .setSingleChoiceItems(items.toTypedArray(), checkedInd) { _, which -> onChoice(which) }
            .create()
    }

    var dialog = builder.create()

    fun clear(){
        dialog.dismiss()
    }
}