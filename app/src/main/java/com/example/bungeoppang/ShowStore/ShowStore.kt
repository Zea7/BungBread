package com.example.bungeoppang.ShowStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.os.TestLooperManager
import android.util.Log
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
import com.example.bungeoppang.retrofit.Menu
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
    private val list:ArrayList<String> = ArrayList<String>()


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
        var adapter:MenuAdapter? = null
        var number:Int? = null

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).launch {
                getInfo()
            }.join()
            name.text = info.store.registrant.nickName
            storeName.text = info.store.storeName
            desc.text = info.store.desc
            address.text = info.store.address
            adapter = MenuAdapter(info.store.menus)

            comments.adapter = CommentAdapter(info.store.comments)
            menu.adapter = adapter
            number = getImage()
            Glide.with(baseContext).load(resources.getIdentifier("category_"+number.toString(), "drawable", packageName)).into(storeIcon)
            star.setOnClickListener{
                if(!touch){
                    Glide.with(baseContext).load(R.drawable.star_gold).into(star)
                    touch = true
                }

                else{
                    Glide.with(baseContext).load(R.drawable.star).into(star)
                    touch = false
                    CoroutineScope(Dispatchers.Main).launch{
                        if(ServerConnect.pickStoreByUser(Variables.USER_ID, info.store._id))
                            Toast.makeText(baseContext, "찜 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun getImage():Int{
        for(i in info.store.menus){
            list.add(i.itemName.split(' ')[i.itemName.split(' ').size-1])
        }
        val hash:HashMap<String, Int> = HashMap()
        var y = 0
        var string = ""
        Log.d("Menu", list.size.toString())
        for(i in list){
            Log.d("Menus", i)
            var x  = 1
            if(hash.containsKey(i))
                x = hash[i]?.plus(1)!!
            hash.put(i, x)
            if(x > y){
                string = i
                y = x}
        }
        Log.d("Menu", string)
        if(string== "붕어빵")
            y = 1
        else if (string == "타코야끼")
            y = 2
        else if(string == "떡볶이")
            y = 3
        else if(string == "와플")
            y = 4
        else if(string == "순대")
            y = 5
        else if(string == "어묵")
            y = 6
        else if(string == "호떡")
            y = 7
        else if(string == "고구마")
            y = 8
        else if(string == "델리만쥬")
            y = 9
        else if(string == "호두과자")
            y = 10
        else y = 1
        return y
    }

    suspend fun getInfo() {
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        CoroutineScope(Dispatchers.IO).launch{
            info = ServerConnect.getStoreByLocation(latitude, longitude, baseContext)
        }.join()
    }
}