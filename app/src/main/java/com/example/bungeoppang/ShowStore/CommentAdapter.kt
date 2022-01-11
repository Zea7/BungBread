package com.example.bungeoppang.ShowStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bungeoppang.R
import com.example.bungeoppang.retrofit.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentAdapter(val comment:List<Comment>): RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var text:TextView
        init{
            name = itemView.findViewById(R.id.comment_name)
            text = itemView.findViewById(R.id.comment_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.comment_list, parent, false)

        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment_item = comment[position]

        holder.name.text = comment_item.writer.toString()
        holder.text.text = comment_item.contents
    }

    override fun getItemCount(): Int {
        return comment.size
    }
}