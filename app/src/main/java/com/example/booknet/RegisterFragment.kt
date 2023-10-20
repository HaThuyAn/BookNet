package com.example.booknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID
import androidx.fragment.app.viewModels

class RegisterFragment : Fragment() {
    private lateinit var register: Button
    private lateinit var username: TextInputEditText
    private lateinit var database: DatabaseReference

    private val viewModel: UserIdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register = view.findViewById(R.id.register)
        username = view.findViewById(R.id.username_input)

        register.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("Users")

            val enteredUsername = username.text.toString().trim() // Get the username and trim leading/trailing whitespace

            if (enteredUsername.isNotEmpty()) {
                val userId = UUID.randomUUID()
                viewModel.saveUserId(requireContext(), userId)

                val user = User(userId.toString(), enteredUsername)
                database.child(userId.toString()).setValue(user)
                    .addOnSuccessListener {
                        findNavController().navigate(R.id.action_registerFragment_to_nav_my_account)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Show the toolbar when the fragment is destroyed
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}