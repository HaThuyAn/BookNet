package com.example.booknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyAccountFragment : Fragment() {
    private lateinit var accountName: TextView
    private lateinit var postCollection: Button
    private lateinit var writeSomething: Button

    private lateinit var database: DatabaseReference

    private val viewModel: UserIdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = viewModel.retrieveUserId(requireContext())

        if (userId != null) {
            // read the database to get username
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(userId).get().addOnSuccessListener {
                val username = it.child("username").value
                accountName = view.findViewById(R.id.username)
                accountName.text = username.toString()
            }
        }

        postCollection = view.findViewById(R.id.published_posts)
        postCollection.setOnClickListener {
            val bundle = bundleOf("authorId" to userId)
            findNavController().navigate(R.id.action_nav_my_account_to_postCollectionFragment, bundle)
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