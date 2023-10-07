package com.example.booknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyAccountFragment : Fragment() {
    private lateinit var postCollection: Button
    private lateinit var writeSomething: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postCollection = view.findViewById(R.id.published_posts)
        postCollection.setOnClickListener {
            findNavController().navigate(R.id.action_nav_my_account_to_postCollectionFragment)
        }

        writeSomething = view.findViewById(R.id.write)
        writeSomething.setOnClickListener {
            findNavController().navigate(R.id.action_nav_my_account_to_publishPostFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }
}