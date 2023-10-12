package com.example.booknet

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class ImageURLViewModel: ViewModel() {
    fun imageUrl(postId: String, callback: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("Posts")
        database.child(postId).get()
            .addOnSuccessListener { dataSnapshot ->
                val url = dataSnapshot.child("imageUrl").value.toString()
                callback(url)
            }
            .addOnFailureListener {
                callback("")
            }
    }
}