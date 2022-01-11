package com.example.bungeoppang.ShowStore

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bungeoppang.R
import com.example.bungeoppang.retrofit.Menu

class MenuAdapter(val menu:List<Menu>) : RecyclerView.Adapter<MenuAdapter.MenuShow>() {

    private val list:ArrayList<String> = ArrayList<String>()


    class MenuShow(itemView: View) : RecyclerView.ViewHolder(itemView){
        var price:TextView
        var count:TextView
        var menu:TextView
        init{
            menu = itemView.findViewById(R.id.menu_menu)
            price = itemView.findViewById(R.id.price)
            count = itemView.findViewById(R.id.count)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuShow {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_show, parent, false)

        return MenuShow(view)
    }

    override fun onBindViewHolder(holder: MenuShow, position: Int) {
        val menu_item = menu[position]
        holder.menu.text = menu_item.itemName

        holder.count.text = menu_item.count.toString() + "개"
        holder.price.text = menu_item.price.toString() +"원"
    }

    override fun getItemCount(): Int {
        return menu.size
    }

}