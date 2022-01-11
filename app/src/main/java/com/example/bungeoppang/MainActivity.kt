package com.example.bungeoppang

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.content.pm.PackageManager
import android.icu.text.IDNA
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bungeoppang.ShowStore.MainFragment
import com.example.bungeoppang.info.InfoFragment
import com.example.bungeoppang.login.LoginActivity
import com.gun0912.tedpermission.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.util.ArrayList
import com.gun0912.tedpermission.PermissionListener as PermissionListener

class MainActivity : AppCompatActivity() {
    lateinit var fragment1: MainFragment
    lateinit var fragment2: InfoFragment
    var menuOn = false

    companion object{
        var latitude:Double = 0.0
        var longitude:Double = 0.0
        var zoomLevel = -1
        var main:MainActivity? = null;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        main = this

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(baseContext, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(baseContext, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 활성화해야 앱이 작동합니다.")
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).check()

        fragment1 = MainFragment.newInstance()
        fragment2 = InfoFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.tab_fragment, fragment1).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mypage -> {
                supportFragmentManager.beginTransaction().replace(R.id.tab_fragment, fragment2)
                    .commit()
                menuOn = true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(menuOn){
            supportFragmentManager.beginTransaction().replace(R.id.tab_fragment, fragment1)
                .commit()
            menuOn = false
        }
        else super.onBackPressed()
    }
}