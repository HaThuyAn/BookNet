package com.example.booknet

import android.content.Intent
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

class EditFragment : Fragment() {
    private lateinit var save: Button
    private lateinit var cancel: Button
    private lateinit var image: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        save = view.findViewById(R.id.save)
        save.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_postCollectionFragment)
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
            image.setImageURI(data?.data)
        }
    }
}