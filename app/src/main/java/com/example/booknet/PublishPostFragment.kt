package com.example.booknet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PublishPostFragment : Fragment() {
    private lateinit var publish: Button
    private lateinit var cancel: Button
    private lateinit var image: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        publish = view.findViewById(R.id.publish)
        publish.setOnClickListener {
            findNavController().navigate(R.id.action_publishPostFragment_to_nav_go_global)
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
            image.setImageURI(data?.data)
        }
    }
}