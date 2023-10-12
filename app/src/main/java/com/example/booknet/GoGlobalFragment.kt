package com.example.booknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
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

class GoGlobalFragment : Fragment(), OnUserNameClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var publish: Button
    private lateinit var goGlobalAdapter: GoGlobalAdapter

    private lateinit var database: DatabaseReference

    private val viewModel: ImageURLViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveAllPosts()

        publish = view.findViewById(R.id.publish)
        publish.setOnClickListener {
            findNavController().navigate(R.id.action_nav_go_global_to_publishPostFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_go_global, container, false)

        goGlobalAdapter = GoGlobalAdapter(viewModel = viewModel)
        goGlobalAdapter.setOnUsernameClickListener(this)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = goGlobalAdapter
        return view
    }

    override fun onUsernameClick(position: Int, post: Post) {
        val bundle = bundleOf("authorId" to post.authorId)
        findNavController().navigate(R.id.action_nav_go_global_to_postCollectionFragment, bundle)
    }

    private fun retrieveAllPosts() {
        database = FirebaseDatabase.getInstance().getReference("Posts")
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempPosts = ArrayList<Post>()
                // Iterate through each child and retrieve the data
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    // Handle the retrieved post (e.g., print it)
                    if (post != null) {
                        tempPosts.add(post)
                    }
                }
                PostData.posts.clear()
                PostData.posts.addAll(tempPosts)

                // Update the adapter with the new data
                goGlobalAdapter.submitList(PostData.posts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("Database error: ${error.message}")
            }
        })
    }
}