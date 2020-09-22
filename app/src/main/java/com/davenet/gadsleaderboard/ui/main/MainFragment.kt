package com.davenet.gadsleaderboard.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davenet.gadsleaderboard.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = SectionsPagerAdapter(lifecycle, childFragmentManager)
        TabLayoutMediator(
            tabs, viewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(TAB_TITLES[0])
                }
                1 -> {
                    tab.text = getString((TAB_TITLES[1]))
                }
            }
        }.attach()

        submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_submitFragment)
        }
    }

}