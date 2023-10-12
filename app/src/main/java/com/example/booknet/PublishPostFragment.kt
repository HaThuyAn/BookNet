package com.example.booknet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class PublishPostFragment : Fragment() {
    private lateinit var publish: Button
    private lateinit var cancel: Button
    private lateinit var content: TextInputEditText
    private lateinit var image: ImageView
    private lateinit var imageUri: Uri
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var username: String = ""

    private val viewModel: UserIdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = viewModel.retrieveUserId(requireContext())

        if (userId != null) {
            // read the database to get username
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(userId).get().addOnSuccessListener {
                username = it.child("username").value.toString()
            }
        }

        publish = view.findViewById(R.id.publish)
        content = view.findViewById(R.id.content)

        /*var imageSuccess = false
        var contentSuccess = false*/

        var imageUrl = ""
        publish.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("Posts")
            val postId = UUID.randomUUID()
            val fileName = UUID.randomUUID().toString() + ".jpg"
            storage = FirebaseStorage.getInstance().getReference("images/$fileName")
            storage.putFile(imageUri)
                .addOnSuccessListener { uploadTask ->
                    uploadTask.storage.downloadUrl
                        .addOnSuccessListener { uri ->
                            imageUrl = uri.toString()
                            // Log.d("URL", imageUrl) // successfully retrieve the image
                            val post = Post(postId.toString(), userId!!, username, content.text.toString(), imageUrl)
                            database.child(postId.toString()).setValue(post)
                                .addOnSuccessListener {

                                    findNavController().navigate(R.id.action_publishPostFragment_to_nav_go_global)
                                }
                                .addOnFailureListener {

                                    // Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {  }
                }
                .addOnFailureListener {

                }
            /*if (imageSuccess && contentSuccess) {
                findNavController().navigate(R.id.action_publishPostFragment_to_nav_go_global)
            }*/
        }

        cancel = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        val card: CardView = view.findViewById(R.id.card)
        card.setOnClickListener {
            image = view.findViewById(R.id.bookImage)
            uploadImage(image)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publish_post, container, false)
    }

    private fun uploadImage(image: ImageView?) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            imageUri = data?.data!!
            image.setImageURI(imageUri)
        }
    }
}