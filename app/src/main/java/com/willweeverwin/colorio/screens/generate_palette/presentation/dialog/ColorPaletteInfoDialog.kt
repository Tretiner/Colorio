package com.willweeverwin.colorio.screens.generate_palette.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.databinding.DialogPaletteInfoBinding
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ColorPaletteInfoDialog : DialogFragment() {

    private var _binding: DialogPaletteInfoBinding? = null
    private val binding get() = _binding!!

    var onApply: (suspend (String, String) -> Boolean)? = null
    var onCancel: (() -> Unit) = { dismiss() }

    private var colors = listOf<RGBColor>()
    fun refreshColors(newColors: List<RGBColor>): ColorPaletteInfoDialog {
        colors = newColors
        return this
    }

    var name = ""
    var desc = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("onCreateView", "ok")

        _binding = DialogPaletteInfoBinding.inflate(inflater).apply {
            resultPalette.color1.setBackgroundColor(colors[0].resColor)
            resultPalette.color2.setBackgroundColor(colors[1].resColor)
            resultPalette.color3.setBackgroundColor(colors[2].resColor)
            resultPalette.color4.setBackgroundColor(colors[3].resColor)
            resultPalette.color5.setBackgroundColor(colors[4].resColor)

            etName.setText(name)
            etDesc.setText(desc)

            resultPalette.tvName.text = name
            resultPalette.tvDesc.text = desc

            btnAccept.setOnClickListener {
                lifecycleScope.launch {
                    val nameText = etName.text
                    val descText = etDesc.text

                    if (nameText.isValid()) {
                        withContext(Dispatchers.Main) { etName.error = "field is empty" }
                        return@launch
                    }

                    Log.d("accept palette", "$nameText\n$descText")
                    val added = onApply!!.invoke(nameText.toString().trim(), descText.toString().trim())

                    if (added) dismiss()
                    else snackbar("item with that name already exists")
                }
            }

            btnCancel.setOnClickListener { onCancel() }

            etName.addTextChangedListener { resultPalette.tvName.text = it }
            etDesc.addTextChangedListener { resultPalette.tvDesc.text = it }
        }

        return binding.root
    }

    override fun onResume() {
        dialog!!.window!!.attributes = dialog!!.window!!.attributes.apply {
            width = MATCH_PARENT
            height = WRAP_CONTENT
        }
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun snackbar(text: CharSequence) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun CharSequence?.isValid() = this.isNullOrBlank()
}