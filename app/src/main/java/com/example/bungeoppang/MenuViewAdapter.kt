package com.example.bungeoppang

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MenuViewAdapter(list: ArrayList<Int>): RecyclerView.Adapter<MenuViewAdapter.MenuHolder>() {
    private val list:ArrayList<Int> = list
    private val items:Array<Int> = arrayOf(R.drawable.category_1, R.drawable.category_2,R.drawable.category_3,
        R.drawable.category_4,R.drawable.category_5,R.drawable.category_6,R.drawable.category_7
        ,R.drawable.category_8,R.drawable.category_9,R.drawable.category_10)
    private val menus:ArrayList<String> = arrayListOf("붕어빵", "타코야끼","떡볶이","와플","순대","어묵","호떡","고구마","델리만쥬","호두과자")

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewAdapter.MenuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item, parent, false)

        return MenuHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewAdapter.MenuHolder, position: Int) {
        var pos = list[position] - 1
        val context = holder.icon.context
        Glide.with(context).load(items[pos]).into(holder.icon)
        holder.menu.text = menus[pos]
        var adapterForList = MenuListAdapter()
        holder.recyclerView.adapter  = adapterForList
        holder.plus.setOnClickListener{
            adapterForList.increaseItems()
        }

        holder.minus.setOnClickListener{
            adapterForList.decreaseItems(context)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}