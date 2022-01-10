package com.example.bungeoppang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bungeoppang.info.InfoFragment
import com.gun0912.tedpermission.TedPermission
import java.util.ArrayList
import com.gun0912.tedpermission.PermissionListener as PermissionListener

class MainActivity : AppCompatActivity() {
    companion object{
        var latitude:Double = 0.0
        var longitude:Double = 0.0
        var zoomLevel = -1
        var main:MainActivity? = null;
    }
    private val TAG:String = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main = this

        val permissionListener = object:PermissionListener{
            override fun onPermissionGranted() {
                Toast.makeText(baseContext, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(baseContext, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 활성화해야 앱이 작동합니다.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION).check()


        //Main Fragment
        var fragment1:MainFragment = MainFragment.newInstance()

        //Map Fragment
        var fragment2:MapFragment = MapFragment.newInstance()

        //Information of user Fragment
        var fragment3: InfoFragment = InfoFragment.newInstance()

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
                    if(position == 0) selected = fragment1 else if(position == 1) selected = fragment2 else if(position == 2) selected = fragment3
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