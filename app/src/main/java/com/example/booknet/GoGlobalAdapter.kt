package com.example.booknet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

interface OnUserNameClickListener {
    fun onUsernameClick(position: Int, post: Post)
}

class GoGlobalAdapter(private var usernameClickListener: OnUserNameClickListener? = null, private val viewModel: ImageURLViewModel) : ListAdapter<Post, GoGlobalAdapter.ViewHolder>(PostDiff) {

    fun setOnUsernameClickListener(listener: OnUserNameClickListener) {
        this.usernameClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.go_global_item_layout, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val author = v.findViewById<TextView>(R.id.username)
        val content = v.findViewById<TextView>(R.id.content)
        val image = v.findViewById<ImageView>(R.id.bookImage)

        fun bind(item: Post) {
            author.text = item.author
            content.text = item.content
            viewModel.imageUrl(item.postId) { url ->
                if (!url.isNullOrEmpty()) {
                    Picasso.get().load(url).into(image)
                }
            }
            author.setOnClickListener {
                usernameClickListener?.onUsernameClick(adapterPosition, item)
            }
        }
    }
}