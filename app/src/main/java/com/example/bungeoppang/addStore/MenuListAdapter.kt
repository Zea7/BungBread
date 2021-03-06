package com.example.bungeoppang.addStore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bungeoppang.R
import org.json.JSONArray
import org.json.JSONObject

class MenuListAdapter(val category:String): RecyclerView.Adapter<MenuListAdapter.ListHolder>() {
    private var menuName:String? = null
    private var menuCount:Int? = null
    private var menuPrice:Int? = null
    private var holders:ArrayList<ListHolder> = ArrayList<ListHolder>()
    var count = 1

    class ListHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val menu_name:EditText
        val menu_count:EditText
        val menu_price:EditText
        init{
            menu_name = itemView.findViewById(R.id.menu_name)
            menu_count = itemView.findViewById(R.id.menu_count)
            menu_price = itemView.findViewById(R.id.menu_price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)

        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holders.add(holder)
    }

    fun getInfoOfMenus():JSONArray{
        val jsons = JSONArray()
        for(i in  holders){
            if(i.menu_name.text.toString() == "")
                menuName = "?"
            else
                menuName  = i.menu_name.text.toString()
            menuName += String.format(" %s", category)
            if(i.menu_count.text.toString() == "")
                menuCount = 0
            else
                menuCount  = i.menu_count.text.toString().toInt()
            if(i.menu_price.text.toString() == "")
                menuPrice = 0
            else
                menuPrice  = i.menu_price.text.toString().toInt()


            val json = JSONObject()
            json.put("itemName", menuName)
            json.put("count", menuCount)
            json.put("price", menuPrice)

            jsons.put(json)
        }

        return jsons
    }

    override fun getItemCount(): Int {
        return count
    }

    fun increaseItems(){
        count++
        notifyItemInserted(count)
        notifyItemRangeChanged(count-1, count)
    }

    fun decreaseItems(context: Context){
        if(count == 1) {
            Toast.makeText(context, "1????????? ?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show()
            return
        }
        notifyItemRemoved(count)
        notifyItemRangeChanged(count, count-1)
        count -= 1
        holders.removeAt(count)
    }
}