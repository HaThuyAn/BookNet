package com.example.booknet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class EditFragment : Fragment() {
    private lateinit var save: Button
    private lateinit var cancel: Button
    private lateinit var image: ImageView
    private lateinit var contentTextInput: TextInputEditText
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var imageUri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = EditFragmentArgs.fromBundle(requireArguments())
        val postId = args.postId
        val content = args.content
        val imageURL = args.imageURL

        var imageChanged = false

        contentTextInput = view.findViewById(R.id.content)
        contentTextInput.setText(content)

        image = view.findViewById(R.id.bookImage)
        if (imageURL != null) {
            Picasso.get().load(imageURL).into(image)
        }

        save = view.findViewById(R.id.save)
        save.setOnClickListener {
            if (!imageChanged) {
                updateData(postId, contentTextInput.text.toString(), null)
            } else {
                var imageUrl = ""
                val fileName = UUID.randomUUID().toString() + ".jpg"
                storage = FirebaseStorage.getInstance().getReference("images/$fileName")
                storage.putFile(imageUri)
                    .addOnSuccessListener { uploadTask ->
                        uploadTask.storage.downloadUrl
                            .addOnSuccessListener { uri ->
                                imageUrl = uri.toString()
                                updateData(postId, contentTextInput.text.toString(), imageUrl)

                                storage = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL)
                                storage.delete()
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener {

                                    }
                            }
                    }
                    .addOnFailureListener {

                    }
            }
        }

        cancel = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        val card: CardView = view.findViewById(R.id.card)
        card.setOnClickListener {
            imageChanged = true
            image = view.findViewById(R.id.bookImage)
            uploadImage(image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
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

    private fun updateData(postId: String, content: String, imageURL: String?) {
        database = FirebaseDatabase.getInstance().getReference("Posts")
        val data = if (imageURL != null) {
            mapOf<String, String>("content" to content, "imageUrl" to imageURL)
        } else {
            mapOf<String, String>("content" to content)
        }

        database.child(postId).updateChildren(data)
            .addOnSuccessListener {
                findNavController().navigateUp()
            }
            .addOnFailureListener {

            }
    }
}