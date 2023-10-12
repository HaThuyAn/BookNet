package com.example.booknet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostCollectionFragment : Fragment(), OnButtonClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postCollectionAdapter: PostCollectionAdapter

    private lateinit var database: DatabaseReference

    private val imageURLViewModel: ImageURLViewModel by viewModels()
    private val userIdViewModel: UserIdViewModel by viewModels()

    private var authorId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authorId = arguments?.getString("authorId") ?: ""

        if (authorId.isNotBlank()) {
            retrievePosts(authorId)
        }

        postCollectionAdapter = PostCollectionAdapter(imageURLViewModel = imageURLViewModel, authorId = authorId, userIdViewModel = userIdViewModel)
        postCollectionAdapter.setOnButtonClickListener(this)
        recyclerView.adapter = postCollectionAdapter

        Log.d("PostCollectionFragment", "onViewCreated is called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_collection, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        Log.d("PostCollectionFragment", "onCreateView is called")
        return view
    }

    override fun onEditClick(position: Int) {
        findNavController().navigate(R.id.action_postCollectionFragment_to_editFragment)
    }

    override fun onDeleteClick(position: Int) {
        // TODO: Implement deletion logic
    }

    private fun retrievePosts(authorId: String) {
        database = FirebaseDatabase.getInstance().getReference("Posts")
        database.orderByChild("authorId").equalTo(authorId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempPosts = ArrayList<Post>()
                // Iterate through each child and retrieve the data
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        tempPosts.add(post)
                    }
                }
                PostData.posts.clear()
                PostData.posts.addAll(tempPosts)

                // Update the adapter with the new data
                postCollectionAdapter.submitList(PostData.posts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("Database error: ${error.message}")
            }
        })
    }
}