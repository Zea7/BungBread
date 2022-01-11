package com.example.bungeoppang.addStore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bungeoppang.R
import org.json.JSONArray

class MenuViewAdapter(list: ArrayList<Int>): RecyclerView.Adapter<MenuViewAdapter.MenuHolder>() {
    private val list:ArrayList<Int> = list
    private lateinit var context: Context
    private val items:Array<Int> = arrayOf(
        R.drawable.category_1, R.drawable.category_2, R.drawable.category_3,
        R.drawable.category_4, R.drawable.category_5, R.drawable.category_6, R.drawable.category_7
        , R.drawable.category_8, R.drawable.category_9, R.drawable.category_10
    )
    private val menus:ArrayList<String> = arrayListOf("붕어빵", "타코야끼","떡볶이","와플","순대","어묵","호떡","고구마","델리만쥬","호두과자")
    private val holders:ArrayList<MenuListAdapter> = ArrayList()

    class MenuHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val recyclerView:RecyclerView
        val plus:ImageView
        val minus:ImageView
        val icon:ImageView
        val menu:TextView
        init{
            recyclerView = itemView.findViewById(R.id.menu_add_list)
            plus = itemView.findViewById(R.id.plus_button)
            minus = itemView.findViewById(R.id.delete_button)
            icon = itemView.findViewById(R.id.menu_icon)
            menu = itemView.findViewById(R.id.menu_category)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item, parent, false)
        context = parent.context

        return MenuHolder(view)
    }

    fun getAllJsons(): JSONArray? {
        var jsons = JSONArray()
        if(holders.size == 0){
            Toast.makeText(context, "하나 이상의 메뉴 카테고리를 선택해주세요!", Toast.LENGTH_SHORT).show()
            return null
        }
        for(i in holders){
            val json = i.getInfoOfMenus()
            for(j in 0 until json.length()){
                jsons.put(json.get(j))
            }
        }

        return jsons
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        var pos = list[position] - 1
        context = holder.icon.context
        Glide.with(context).load(items[pos]).into(holder.icon)
        holder.menu.text = menus[pos]
        val adapterList = MenuListAdapter(menus[pos])
        holder.recyclerView.adapter  = adapterList
        holder.recyclerView.layoutManager = object:LinearLayoutManager(context){
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        holders.add(adapterList)

        holder.plus.setOnClickListener{
            adapterList.increaseItems()
        }

        holder.minus.setOnClickListener{
            adapterList.decreaseItems(context)
        }
        holder.recyclerView.adapter
    }

    override fun getItemCount(): Int {
        return list.size
    }
}