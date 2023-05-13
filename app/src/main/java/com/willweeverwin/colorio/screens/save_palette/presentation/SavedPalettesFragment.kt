package com.willweeverwin.colorio.screens.save_palette.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.databinding.FragmentSavedPalettesBinding
import com.willweeverwin.colorio.screens.generate_palette.presentation.dialog.ColorPaletteInfoDialog
import com.willweeverwin.colorio.screens.generate_palette.presentation.util.RecyclerEvent
import com.willweeverwin.colorio.screens.save_palette.presentation.adapter.SavedPalettesAdapter
import com.willweeverwin.colorio.screens.save_palette.presentation.decorator.Spacer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SavedPalettesFragment : Fragment() {

    private var _binding: FragmentSavedPalettesBinding? = null
    private val binding get() = _binding!!

    private val vm: SavedPalettesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentSavedPalettesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val palettesAdapter = SavedPalettesAdapter(
            vm.palettes,
            onUpdate = { pos -> showSavedPaletteDialog(pos) },
            onDelete = { pos -> vm.deletePaletteAt(pos) }
        )

        binding.apply {
            swipeToRefresh.setOnRefreshListener { vm.refreshPalettes() }

            palettes.apply {
                setHasFixedSize(true)
                addItemDecoration(Spacer(40))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, x: Int, y: Int) {
                        if (!recyclerView.canScrollVertically(1) && y > 0) {
                            Log.d("scroll", "i was activated on start")
                            vm.loadNewPalettes()
                        }
                    }
                })

                adapter = palettesAdapter
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    vm.snackbarFlow.collectLatest { message -> snackbar(message) }
                }

                vm.recyclerFlow.collect { event ->
                    when (event) {
                        is RecyclerEvent.ItemUpdated -> palettesAdapter.updateAt(event.pos)
                        is RecyclerEvent.ItemDeleted -> palettesAdapter.deleteAt(event.pos)
                        is RecyclerEvent.ItemsLoaded -> palettesAdapter.added(event.fromIndex, event.size)
                        is RecyclerEvent.Refreshed -> palettesAdapter.refreshed().also { binding.swipeToRefresh.isRefreshing = false }
                    }
                }

            }
        }
    }

    private fun showSavedPaletteDialog(pos: Int) {
        val palette = vm.palettes[pos]

        ColorPaletteInfoDialog().apply {
            refreshColors(palette.colors)
            name = palette.name
            desc = palette.desc

            onApply = { _name, _desc ->
                if (name != _name || desc != _desc) {
                    Log.d("paletteInfoDialog", "changes detected")

                    palette.name = _name
                    palette.desc = _desc

                    vm.updatePaletteAt(pos, palette)
                } else true
            }
        }.show(childFragmentManager, "dialog_palette_info")
    }

    private fun snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}