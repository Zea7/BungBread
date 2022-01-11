package com.example.bungeoppang.ShowStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.os.TestLooperManager
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bungeoppang.R
import com.example.bungeoppang.ServerConnect
import com.example.bungeoppang.Variables
import com.example.bungeoppang.retrofit.StoreLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.properties.Delegates

class ShowStore : AppCompatActivity() {
    private lateinit var info:StoreLocation
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private lateinit var name:TextView
    private lateinit var storeName:TextView
    private lateinit var storeIcon:ImageView
    private lateinit var menu:RecyclerView
    private lateinit var comments:RecyclerView
    private lateinit var desc:TextView
    private lateinit var address:TextView
    private lateinit var star:ImageView
    private var touch:Boolean = false


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_store)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        name = findViewById(R.id.nickname)
        storeName = findViewById(R.id.store_name)
        storeIcon = findViewById(R.id.store_icon)
        menu = findViewById(R.id.category_show)
        comments = findViewById(R.id.comment_recycler)
        desc = findViewById(R.id.desc)
        address = findViewById(R.id.address)
        star = findViewById(R.id.star)

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).launch {
                getInfo()


            }.join()
            name.text = info.store.registrant.nickName
            storeName.text = info.store.storeName
            desc.text = info.store.desc
            address.text = info.store.address
            menu.adapter = MenuAdapter(info.store.menus)
            star.setOnClickListener{
                if(touch)
                    Glide.with(baseContext).load(R.drawable.star_gold).into(star)
                else{
                    Glide.with(baseContext).load(R.drawable.star).into(star)
                    CoroutineScope(Dispatchers.Main).launch{
                        if(ServerConnect.pickStoreByUser(Variables.USER_ID, info.store._id))
                            Toast.makeText(baseContext, "찜 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    suspend fun getInfo() {
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        CoroutineScope(Dispatchers.IO).launch{
            info = ServerConnect.getStoreByLocation(latitude, longitude, baseContext)
        }.join()
    }
}