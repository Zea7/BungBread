package com.example.bungeoppang.addStore

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bungeoppang.R
import java.io.Serializable


class CategoryAdapter(item_list: ArrayList<Int>, getResult: ActivityResultLauncher<Intent>, var changeMenuInterface: ChangeMenu) :
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>(), Serializable{
    private val items:Array<Int> = arrayOf(
        R.drawable.plus, R.drawable.category_1, R.drawable.category_2, R.drawable.category_3,
        R.drawable.category_4, R.drawable.category_5, R.drawable.category_6, R.drawable.category_7
    , R.drawable.category_8, R.drawable.category_9, R.drawable.category_10
    )
    private var list:ArrayList<Int> = getList(item_list.sorted())
    private var getResult = getResult



    fun getList(list:List<Int>):ArrayList<Int>{
        var l:ArrayList<Int> = arrayListOf()
        l.addAll(list)
        return l
    }


    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var img: ImageView
        val deleteButton: ImageView
        init {
            img = itemView.findViewById(R.id.category_icon)
            img.clipToOutline = true
            deleteButton = itemView.findViewById(R.id.delete_circle_button)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        if(position == 0) {
            Glide.with(holder.img.context).load(items[0]).into(holder.img)
            val view = (holder.img.parent as ViewGroup)
            view.removeView(view.findViewById(R.id.delete_circle_button))
            holder.img.setOnClickListener{
                val intent = Intent(holder.img.context, CategorySelect :: class.java)
                intent.putExtra("selected", list)
                getResult.launch(intent)
            }
        }
        else {
            Glide.with(holder.img.context).load(items[list[position]]).into(holder.img)
            holder.deleteButton.setOnClickListener {
                deleteItem(position, holder.img.context)
                changeMenuInterface.changed(list)

            }
        }


    }

    private fun deleteItem(position: Int, context: Context){
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}