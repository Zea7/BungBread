package com.example.bungeoppang.info

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bungeoppang.R
import com.example.bungeoppang.data.Stores

class StoreAdapter(val context: Context, var itemList: ArrayList<Stores.StoreItem>, private val inflater: LayoutInflater):
    RecyclerView.Adapter<StoreAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvMenu: TextView = itemView.findViewById(R.id.tv_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.rv_item_store, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}