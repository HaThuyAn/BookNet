package com.example.booknet

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostCollectionFragment : Fragment(), OnButtonClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postCollectionAdapter: PostCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_collection, container, false)

        postCollectionAdapter = PostCollectionAdapter()
        postCollectionAdapter.setOnButtonClickListener(this)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = postCollectionAdapter
        return view
    }

    override fun onEditClick(position: Int) {
        findNavController().navigate(R.id.action_postCollectionFragment_to_editFragment)
    }

    override fun onDeleteClick(position: Int) {
        // TODO: Implement deletion logic
    }
}