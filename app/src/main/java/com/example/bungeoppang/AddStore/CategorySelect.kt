package com.example.bungeoppang.AddStore

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bungeoppang.R
import java.io.Serializable

class CategorySelect : AppCompatActivity(), Serializable {
    var save: Button? = null
    var view:RecyclerView? =null
    var list:ArrayList<Int>? = null

    fun getInfo(){
        list = intent.getSerializableExtra("selected") as ArrayList<Int>?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_category_select)

        getInfo()


        save = findViewById(R.id.save_category)
        view = findViewById(R.id.select_icons)
        val categoryadapter = CategorySelectAdapter(list)

        view?.adapter = categoryadapter


        save?.setOnClickListener{
            intent = Intent(this, AdditionalInfo::class.java)
            intent.putExtra("selected",categoryadapter.getList())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    class CategorySelectAdapter(list:ArrayList<Int>?): RecyclerView.Adapter<CategorySelectAdapter.SelectHolder>(){
        var selected:ArrayList<Int> = getSelected(list)
        private val items:Array<Int> = arrayOf(
            R.drawable.category_1,
            R.drawable.category_2,
            R.drawable.category_3,
            R.drawable.category_4,
            R.drawable.category_5,
            R.drawable.category_6,
            R.drawable.category_7
            ,
            R.drawable.category_8,
            R.drawable.category_9,
            R.drawable.category_10
        )
        class SelectHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val img:ImageView
            init{
                img = itemView.findViewById(R.id.category_icon)
                (itemView as ViewGroup).removeView(itemView.findViewById(R.id.delete_circle_button))
            }
        }

        fun getSelected(list:ArrayList<Int>?):ArrayList<Int>{
            if(list == null)
                return arrayListOf(0)
            else
                return list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

            return SelectHolder(view)
        }

        override fun onBindViewHolder(holder: SelectHolder, position: Int) {
            val pos = position + 1
            val matrix = ColorMatrix()
            matrix.setSaturation(0.0F)
            val filter = ColorMatrixColorFilter(matrix)

            if(selected.contains(pos))
                Glide.with(holder.img.context).load(items[position]).into(holder.img)
            else{
                Glide.with(holder.img.context).load(items[position]).into(holder.img)
                holder.img.colorFilter = filter
            }

            holder.img.setOnClickListener{
                if(selected.contains(pos)) {
                    selected.remove(pos)
                    holder.img.colorFilter = filter
                }
                else {
                    selected.add(pos)
                    holder.img.colorFilter = null
                }
            }

        }

        fun getList():ArrayList<Int> {
            if(!selected.contains(0))
                selected.add(0)
            return selected
        }

        override fun getItemCount(): Int {
            return (items.size)
        }
    }
}