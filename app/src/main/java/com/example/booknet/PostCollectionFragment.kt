package com.example.booknet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PostCollectionFragment : Fragment(), OnButtonClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postCollectionAdapter: PostCollectionAdapter

    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_collection, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onEditClick(position: Int) {
        val action = PostCollectionFragmentDirections.actionPostCollectionFragmentToEditFragment(
            PostData.posts[position].postId,
            PostData.posts[position].content,
            PostData.posts[position].imageUrl
        )
        findNavController().navigate(action)
    }

    override fun onDeleteClick(position: Int) {
        database = FirebaseDatabase.getInstance().getReference("Posts")
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(PostData.posts[position].imageUrl)
        val ref = database.child(PostData.posts[position].postId)
        ref.removeValue()
            .addOnSuccessListener {
                storageRef.delete()
                    .addOnSuccessListener {
                        retrievePosts(authorId)
                    }
                    .addOnFailureListener {

                    }
                Log.d("FirebaseDB", "Post successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseDB", "Deletion failed: ${e.message}")
            }
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

                postCollectionAdapter.submitList(PostData.posts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("Database error: ${error.message}")
            }
        })
    }
}