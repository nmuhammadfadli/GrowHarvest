package com.features.growharvest.Transaksi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.features.growharvest.R
import com.google.android.material.tabs.TabLayout


private lateinit var  tabLayout : TabLayout
private lateinit var viewPager1 : ViewPager2
private lateinit var adapter: FragmentAdapter
class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        tabLayout = findViewById(R.id.tabLayout1)
        viewPager1 = findViewById(R.id.viewPager1)

        adapter = FragmentAdapter(supportFragmentManager,lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        viewPager1.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager1.currentItem = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager1.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}