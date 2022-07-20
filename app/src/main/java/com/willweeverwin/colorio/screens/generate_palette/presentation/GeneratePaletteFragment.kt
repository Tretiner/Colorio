package com.willweeverwin.colorio.screens.generate_palette.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.R
import com.willweeverwin.colorio.databinding.FragmentGeneratePaletteBinding
import com.willweeverwin.colorio.databinding.LayoutGeneratedPaletteBinding
import com.willweeverwin.colorio.screens.generate_palette.presentation.dialog.ColorPaletteInfoDialog
import com.willweeverwin.colorio.screens.generate_palette.presentation.dialog.ModelsDialog
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GeneratePaletteFragment : Fragment() {

    private val vm: GeneratePaletteViewModel by viewModels()

    private var _binding: FragmentGeneratePaletteBinding? = null
    private val binding get() = _binding!!

    private var modelsDialogBuilder: ModelsDialog? = null

    private lateinit var ctx: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentGeneratePaletteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctx = requireContext()

        lifecycleScope.launch {
            modelsDialogBuilder = ModelsDialog(ctx) { vm.chooseModelAt(it) }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    vm.paletteState.collect { colors ->
                        changeColors(colors ?: return@collect)
                    }
                }

                launch {
                    vm.checkedModelState.collect { newModelName ->
                        setCheckedModelName(newModelName)
                    }
                }

                launch {
                    vm.modelsState.collectIndexed { ind, newModels ->
                        refreshModels(newModels)
                        if (ind == 1) cancel()
                    }
                }

                launch { vm.snackbarState.collect { msg -> snackbar(msg) } }
            }
        }

        binding.apply {
            color1.setColorListeners(0)
            color2.setColorListeners(1)
            color3.setColorListeners(2)
            color4.setColorListeners(3)
            color5.setColorListeners(4)

            btnGetPalette.setOnClickListener { vm.refreshColors() }
            btnSavePalette.setOnClickListener { showSavePaletteDialog() }
            btnChangePaletteModel.setOnClickListener { showModelsDialog() }
        }
    }

    override fun onDestroyView() {
        modelsDialogBuilder?.clear()
        super.onDestroyView()
    }

    private fun showModelsDialog() {
        modelsDialogBuilder?.dialog?.show()
    }

    private fun refreshModels(newModels: Collection<String>) {
        modelsDialogBuilder?.setModels(newModels, vm.checkedModelIndex)
    }

    private fun setCheckedModelName(newModelName: String) {
        binding.btnChangePaletteModel.text = newModelName
    }

    private fun showSavePaletteDialog() {
        ColorPaletteInfoDialog().apply {
            refreshColors(vm.paletteColors)
            onApply = { name, description -> vm.savePalette(name, description) }
            onCancel = { dismiss() }
        }.show(childFragmentManager, "dialog_palette_info")
    }

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

            colorBind.apply {
                root.setBackgroundColor(colors[i].resColor)
                colorText.text = colors[i].hexString

                val colorRes = colors[i].getTextColorRes(ctx)
                colorText.setTextColor(colorRes)
                colorLock.setColorFilter(colorRes)
            }
        }
    }

    private fun LayoutGeneratedPaletteBinding.setColorListeners(colorIndex: Int) {
        root.setOnClickListener { showColorPicker(colorIndex) }
        root.setOnLongClickListener { colorText.copyTextToClipboard(); true }
        colorLock.setOnClickListener { colorLock.toggleLock(colorIndex) }
    }

    private fun showColorPicker(colorIndex: Int) {
//        colorPicker?.dialog?.show()
        Log.d("call picker", "someday...")
    }

    private fun ImageView.toggleLock(colorIndex: Int) = this.setImageResource(
        if (vm.toggleLock(colorIndex))
            R.drawable.ic_lock_locked
        else
            R.drawable.ic_lock_unlocked
    )

    private fun TextView.copyTextToClipboard() {
        vm.copyToClipboard(ctx, this.text.toString())
    }

    private fun snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}