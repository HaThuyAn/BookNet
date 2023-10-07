package com.example.booknet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnUserNameClickListener {
    fun onUsernameClick(position: Int)
}

class GoGlobalAdapter(private var usernameClickListener: OnUserNameClickListener? = null) : RecyclerView.Adapter<GoGlobalAdapter.ViewHolder>() {
    fun setOnUsernameClickListener(listener: OnUserNameClickListener) {
        this.usernameClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.go_global_item_layout, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = PostData.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = PostData.posts.get(position)
        holder.bind(item)
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val author = v.findViewById<TextView>(R.id.username)
        val content = v.findViewById<TextView>(R.id.content)
        val image = v.findViewById<ImageView>(R.id.bookImage)

        fun bind(item: Post) {
            author.text = item.author
            content.text = item.content
            image.setImageResource(item.imageResourceId)
            author.setOnClickListener {
                usernameClickListener?.onUsernameClick(adapterPosition)
            }
        }
    }
}