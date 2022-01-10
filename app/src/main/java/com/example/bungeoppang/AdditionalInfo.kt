package com.example.bungeoppang

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView

class AdditionalInfo : AppCompatActivity(), ChangeMenu {
    private var address:String? = null
    private var latitude:Double = 0.0
    private var longitude: Double = 0.0
    private var list:ArrayList<Int>? = null
    private lateinit var categoryAdapter:CategoryAdapter
    private var recyclerView:RecyclerView? = null
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private var menuView:RecyclerView? = null
    private var menuAdapter:MenuViewAdapter? = null

    override fun changed(l: ArrayList<Int>) {
        list = l
        changeMenuAdapter()
    }

    fun changeMenuAdapter(){
        if(list!!.size > 1){
            var copy = ArrayList<Int>()
            copy.addAll(list!!)
            copy.remove(0)
            if(menuAdapter!=null) {
                menuAdapter = MenuViewAdapter(copy)
                menuView?.swapAdapter(menuAdapter, true)
            }
            else{
                menuAdapter = MenuViewAdapter(copy)
                menuView?.adapter = menuAdapter
            }

        } else
            menuView?.adapter = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode == RESULT_OK){
                list = result.data?.getSerializableExtra("selected") as ArrayList<Int>
                recyclerView?.swapAdapter(CategoryAdapter(list!!, getResult, this), true)
                changeMenuAdapter()
            }
        }

        categoryAdapter = CategoryAdapter(kotlin.collections.arrayListOf(0), getResult, this)

        getInfo()
        recyclerView = findViewById(R.id.category_view)
        recyclerView?.adapter = categoryAdapter
        menuView = findViewById(R.id.menu_recyclerview)
    }

    fun getInfo(){
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        address = intent.getStringExtra("address")
    }

    override fun onBackPressed() {
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        setResult(RESULT_CANCELED, intent)
        finish()
    }
}