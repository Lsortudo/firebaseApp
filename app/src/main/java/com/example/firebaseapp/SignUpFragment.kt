package com.example.firebaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    //private lateinit var auth: FirebaseAuth
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment with binding
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

        // Call SignUp function
        // Initialize Firebase Auth
        binding.btnSignUp.setOnClickListener {
            funSignUp()
        }


    }

    private fun funSignUp() {
        val email = binding.etEmail
        val password = binding.etPassword

        val userEmail = email.text.toString()
        val userPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
            findNavController().navigate(R.id.homeFragment)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(context, "error occurred ${it.localizedMessage}", Toast.LENGTH_LONG)
                    .show()
            }
    }


}