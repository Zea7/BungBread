package com.example.bungeoppang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


import android.R.id.tabs
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Main Fragment
        var fragment1:MainFragment = MainFragment.newInstance()

        //Map Fragment
        var fragment2:MapFragment = MapFragment.newInstance()

        //Information of user Fragment
        var fragment3:InfoFragment = InfoFragment.newInstance()

        var icons = arrayOf(R.drawable.magnifier, R.drawable.map, R.drawable.person)


        var tab:TabLayout = findViewById(R.id.main_tab)

        with(tab) {

            for(i: Int in 0..2)
                getTabAt(i)?.setIcon(icons.get(i))
            supportFragmentManager.beginTransaction().add(R.id.tab_fragment, fragment1).commit();

            addOnTabSelectedListener(object:OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab!!.position
                    var selected: Fragment = fragment1
                    if(position == 0) selected = fragment1 else if(position == 1) selected = fragment2 else if(position == 3) selected = fragment3
                    supportFragmentManager.beginTransaction().replace(R.id.tab_fragment, selected).commit();
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }
}