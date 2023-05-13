package com.willweeverwin.colorio.screens.save_palette.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.willweeverwin.colorio.databinding.ItemSavedPaletteBinding
import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity

class SavedPalettesAdapter(
    palettes: List<SavedPaletteEntity>,
    private val onUpdate: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<SavedPalettesAdapter.ViewHolder>() {

    private var _palettes = palettes as MutableList

    fun added(fromIndex: Int, _size: Int) {
        notifyItemRangeInserted(fromIndex, _size)
    }

    fun updateAt(pos: Int) {
        notifyItemChanged(pos)
    }

    fun deleteAt(pos: Int) {
        notifyItemRemoved(pos)
    }

    @SuppressLint("notifyDataSetChanged")
    fun refreshed() {
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSavedPaletteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(palette: SavedPaletteEntity) {
            binding.apply {
                color1.setBackgroundColor(palette.colors[0].resource)
                color2.setBackgroundColor(palette.colors[1].resource)
                color3.setBackgroundColor(palette.colors[2].resource)
                color4.setBackgroundColor(palette.colors[3].resource)
                color5.setBackgroundColor(palette.colors[4].resource)

                tvName.text = palette.name
                tvDesc.text = palette.desc
            }
        }

        fun setListeners(): ViewHolder {
            binding.apply {
                root.setOnClickListener { onUpdate(adapterPosition) }
                btnDelete.setOnClickListener { onDelete(adapterPosition) }
            }

            return this@ViewHolder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedPaletteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding).setListeners()
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(_palettes[pos])
    }

    override fun getItemCount() = _palettes.size

}