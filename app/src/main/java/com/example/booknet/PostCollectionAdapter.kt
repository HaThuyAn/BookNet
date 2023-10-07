package com.example.booknet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnButtonClickListener {
    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)
}

class PostCollectionAdapter(private var buttonClickListener: OnButtonClickListener? = null) : RecyclerView.Adapter<PostCollectionAdapter.ViewHolder>() {
    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.buttonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.post_collection_item_layout, parent, false) as View
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
        val edit = v.findViewById<Button>(R.id.edit)
        val delete = v.findViewById<Button>(R.id.delete)

        fun bind(item: Post) {
            author.text = item.author
            content.text = item.content
            image.setImageResource(item.imageResourceId)
            edit.setOnClickListener {
                buttonClickListener?.onEditClick(adapterPosition)
            }
            delete.setOnClickListener {
                buttonClickListener?.onDeleteClick(adapterPosition)
            }
        }
    }
}