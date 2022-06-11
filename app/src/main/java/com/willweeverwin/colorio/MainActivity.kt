package com.willweeverwin.colorio

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.willweeverwin.colorio.databinding.ActivityMainBinding
import com.willweeverwin.colorio.databinding.PaletteColorLayoutBinding
import com.willweeverwin.colorio.ui.model.RGBColor
import com.willweeverwin.colorio.ui.presentation.ColorPaletteViewModel
import com.willweeverwin.colorio.ui.util.UIEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val vm: ColorPaletteViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            color1.setColorListeners(0)
            color2.setColorListeners(1)
            color3.setColorListeners(2)
            color4.setColorListeners(3)
            color5.setColorListeners(4)

            btnGetPalette.setOnClickListener { vm.getPalette() }
            btnSavePalette.setOnClickListener { vm.savePalette() }
            btnChangePaletteModel.setOnClickListener { showModelDialog() }
        }

        lifecycleScope.launch {
            vm.refreshModels()
            vm.initModelDialog(this@MainActivity)
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.eventFlow.collectLatest { event ->
                    when (event) {
                        is UIEvent.ShowSnackbar -> showSnackbar(event.message)
                        is UIEvent.ChangePalette -> changePalette(event.colors)
                        is UIEvent.ChangeModel -> changeModel(event.model)
                        is UIEvent.SetupUI -> setupUI(event.colors, event.model)
                    }
                }
            }
        }
    }

    private fun changeModel(text: String) {
        binding.btnChangePaletteModel.text = text
    }

    private fun showModelDialog() {
        vm.getAlertDialog()?.show()
    }


    private fun PaletteColorLayoutBinding.setColorListeners(colorIndex: Int) {
        root.setOnLongClickListener { colorText.copyTextToClipboard() }
        root.setOnClickListener { it.betterCallColorPicker() }
        colorLock.setOnClickListener { it.toggleColorLock(colorIndex) }
    }

    private fun TextView.copyTextToClipboard(): Boolean {
        vm.copyToClipboard(this@MainActivity, this.text.toString())
        return true
    }

    private fun View.betterCallColorPicker() {
        Log.d("call picker", "someday...")
    }

    private fun View.toggleColorLock(colorIndex: Int) = (this as ImageView).setImageResource(
        if (vm.toggleLock(colorIndex))
            R.drawable.ic_lock_locked
        else
            R.drawable.ic_lock_unlocked
    )


    private fun setupUI(colors: List<RGBColor>, modelText: String) {
        println("setup UI \n----------------------\n-\n-\n-\n-\n-\n-\n-\n-\n-")
        changePalette(colors)
        changeModel(modelText)
    }

    private fun changePalette(colors: List<RGBColor>) {
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
            val colorRes = colors[i].getTextColorRes(this)
            colorBind.colorText.setTextColor(colorRes)
            colorBind.colorLock.setColorFilter(colorRes)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}