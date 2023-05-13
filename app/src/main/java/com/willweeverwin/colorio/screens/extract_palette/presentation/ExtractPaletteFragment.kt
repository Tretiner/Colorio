package com.willweeverwin.colorio.screens.extract_palette.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.databinding.FragmentExtractPaletteBinding
import com.willweeverwin.colorio.screens.generate_palette.presentation.dialog.ColorPaletteInfoDialog
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExtractPaletteFragment : Fragment() {

    private val vm: ExtractPaletteViewModel by viewModels()

    private var _binding: FragmentExtractPaletteBinding? = null
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        vm.extractColors(uri ?: return@registerForActivityResult)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtractPaletteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            img.setOnClickListener { launcher.launch("image/*") }
            btnSavePalette.setOnClickListener { showSavePaletteDialog() }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.uiStateFlow.collectLatest { state -> applyState(state) }
                }
                launch {
                    vm.snackbarFlow.collectLatest { text -> snackbar(text) }
                }
            }
        }
    }

    private fun applyState(state: Pair<Bitmap?, List<RGBColor>>) {
        val (bitmap, colors) = state
        val ctx = requireContext()

        binding.apply {
            img.setImageBitmap(bitmap)

            val colorBinds = listOf(
                binding.color1,
                binding.color2,
                binding.color3,
                binding.color4,
                binding.color5,
            )

            colors.forEachIndexed { i, color ->
                colorBinds[i].apply {
                    root.setBackgroundColor(color.resource)

                    val colorRes = color.getTextColorRes(ctx)
                    colorText.setTextColor(colorRes)
                    colorText.text = color.hex
                }
            }
        }
    }

    private fun showSavePaletteDialog() {
        ColorPaletteInfoDialog().apply {
            refreshColors(vm.paletteColors)
            onApply = { name, description -> vm.savePalette(name, description) }
            onCancel = { dismiss() }
        }.show(childFragmentManager, "dialog_palette_info")
    }

    private fun snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}