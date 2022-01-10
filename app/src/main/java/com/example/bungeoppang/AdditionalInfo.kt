package com.example.bungeoppang

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject


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
    private lateinit var addressText: EditText
    private lateinit var storeName: EditText
    private lateinit var addressName: TextView
    private var name:String? = null
    private var comment:String? = null
    private lateinit var commentEdit:EditText

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
        } else if (item.itemId == R.id.save) {
            getInfoFromEdit()
            val jsons = menuAdapter?.getAllJsons()
            // Variable로 변경 필요
            ServerConnect.sendStoreInfo(address!!, name!!, 12345678, "박현준", latitude, longitude, jsons, comment!!, this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save, menu)
        return true
    }

    fun getInfoFromEdit(){
        name = storeName.text.toString()
        address += " "+addressText.text.toString()
        comment = commentEdit.text.toString()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)

        commentEdit = findViewById(R.id.editComment)
        getInfo()

        addressName = findViewById(R.id.address_view)
        addressName.text = address

        addressText = findViewById(R.id.editAddress)
        storeName = findViewById(R.id.editText)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode == RESULT_OK){
                list = result.data?.getSerializableExtra("selected") as ArrayList<Int>
                recyclerView?.swapAdapter(CategoryAdapter(list!!, getResult, this), true)
                changeMenuAdapter()
            }
        }

        categoryAdapter = CategoryAdapter(kotlin.collections.arrayListOf(0), getResult, this)


        recyclerView = findViewById(R.id.category_view)
        recyclerView?.adapter = categoryAdapter
        recyclerView?.layoutManager = object:GridLayoutManager(this, 5){
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        menuView = findViewById(R.id.menu_recyclerview)
        menuView?.layoutManager = object:LinearLayoutManager(this){
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
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