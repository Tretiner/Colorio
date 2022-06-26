package com.willweeverwin.colorio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.willweeverwin.colorio.databinding.ActivityMainBinding
import com.willweeverwin.colorio.features.ViewPagerAdapter
import com.willweeverwin.colorio.features.saved_palettes.presentation.SavedPalettesFragment
import com.willweeverwin.colorio.features.generate_palette.presentation.GeneratePaletteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        val fragments = listOf(
            GeneratePaletteFragment(),
            SavedPalettesFragment()
        )

        binding.viewPager.adapter = ViewPagerAdapter(
            fragments,
            supportFragmentManager,
            lifecycle
        )
    }

    private fun setupTabLayout() {
        val icons = listOf(
            R.drawable.ic_round_color_palette,
            R.drawable.ic_round_data
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.setIcon(icons[pos])
        }.attach()
    }
}