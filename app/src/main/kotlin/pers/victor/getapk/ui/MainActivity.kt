package pers.victor.getapk.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pers.victor.getapk.R
import pers.victor.getapk.adapter.AppPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f

        vp_main.adapter = AppPagerAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(vp_main)
    }
}
