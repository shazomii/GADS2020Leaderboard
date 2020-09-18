package com.davenet.gadsleaderboard.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davenet.gadsleaderboard.R
import com.davenet.gadsleaderboard.ui.submission.SubmitActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewpager.adapter = SectionsPagerAdapter(lifecycle, supportFragmentManager)
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
            startActivity(Intent(this, SubmitActivity::class.java))
        }
    }
}