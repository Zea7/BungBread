package com.example.bungeoppang.ShowStore

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
        list.add(menu_item.itemName.split(" ")[menu_item.itemName.split(" ").size -1])
        holder.count.text = menu_item.count.toString() + "개"
        holder.price.text = menu_item.price.toString() +"원"
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    fun getImage():Int{
        val hash:HashMap<String, Int> = HashMap()
        for(i in list){
            var x  = 1
            if(hash.containsKey(i))
                x = hash.get(i)?.plus(1) ?: 0
            hash.put(i, x)
        }
        return 1
    }
}