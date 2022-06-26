package com.willweeverwin.colorio.features.generate_palette.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.R
import com.willweeverwin.colorio.databinding.FragmentColorPaletteBinding
import com.willweeverwin.colorio.databinding.PaletteColorLayoutBinding
import com.willweeverwin.colorio.features.generate_palette.presentation.component.MaterialColorPicker
import com.willweeverwin.colorio.features.generate_palette.presentation.component.MaterialModelsDialog
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor
import com.willweeverwin.colorio.features.generate_palette.presentation.util.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GeneratePaletteFragment : Fragment() {

    private val vm: GeneratePaletteViewModel by viewModels()

    private var _binding: FragmentColorPaletteBinding? = null
    private val binding get() = _binding!!

    private lateinit var ctx: Context

    private lateinit var modelsDialog: MaterialModelsDialog
    private lateinit var colorPicker: MaterialColorPicker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentColorPaletteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctx = requireContext()

        binding.apply {
            color1.setColorListeners(0)
            color2.setColorListeners(1)
            color3.setColorListeners(2)
            color4.setColorListeners(3)
            color5.setColorListeners(4)

            btnGetPalette.setOnClickListener { vm.refreshColors() }
            btnSavePalette.setOnClickListener { vm.savePalette() }
            btnChangePaletteModel.setOnClickListener { showModelDialog() }
        }

        lifecycleScope.launch {
            modelsDialog = MaterialModelsDialog(ctx) { vm.chooseModelAt(it) }
            colorPicker  = MaterialColorPicker(ctx)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.eventFlow.collectLatest { event ->
                    when (event) {
                        is UIEvent.ShowSnackbar -> snackbar(event.message)
                        is UIEvent.ChangeColors -> changeColors(event.colors)
                        is UIEvent.ChangeModel -> changeModel(event.model)
                    }
                }
            }
        }
    }

    private fun showModelDialog() {
        modelsDialog.makeDialog(vm.models, vm.checkedModelIndex).show()
    }

    private fun PaletteColorLayoutBinding.setColorListeners(colorIndex: Int) {
        root.setOnClickListener { it.showColorPicker() }
        root.setOnLongClickListener { colorText.copyTextToClipboard(); true }
        colorLock.setOnClickListener { it.toggleLock(colorIndex) }
    }

    private fun View.showColorPicker() {
        colorPicker.getDialog().show()
        Log.d("call picker", "someday...")
    }

    private fun TextView.copyTextToClipboard() {
        vm.copyToClipboard(ctx, this.text.toString())
    }

    private fun View.toggleLock(colorIndex: Int) = (this as ImageView).setImageResource(
        if (vm.toggleLock(colorIndex))
            R.drawable.ic_lock_locked
        else
            R.drawable.ic_lock_unlocked
    )

    private fun changeColors(colors: List<RGBColor>) {
        val colorBinds = listOf(
            binding.color1,
            binding.color2,
            binding.color3,
            binding.color4,
            binding.color5,
        )

        for ((i, colorBind) in colorBinds.withIndex()) {
            if (colors[i].locked) continue

            colorBind.root.setBackgroundColor(colors[i].toResColor())
            colorBind.colorText.text = colors[i].toHexString()
            val colorRes = colors[i].getTextColorRes(ctx)
            colorBind.colorText.setTextColor(colorRes)
            colorBind.colorLock.setColorFilter(colorRes)
        }
    }

    private fun snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun changeModel(text: String) {
        binding.btnChangePaletteModel.text = text
    }
}