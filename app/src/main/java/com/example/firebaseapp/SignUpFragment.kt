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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    // viewbinding soo i don't have to use FindViewById (DEP)
    lateinit var binding: FragmentSignUpBinding

    // Firebase connection
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment with ViewBinding
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAlreadyAccount.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }

        // Get value from EditTexts
        binding.btnSignUp.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val dateBirth = binding.etDateBirth.text.toString()
            val dinheiroInicial = "100,00"
            var saldo = dinheiroInicial.toString()

            // Call SignUp function's'
            funSignUp(email, password)
            funAdditionalData(email, password, firstName, lastName, dateBirth, saldo)
        }

    }

    private fun funAdditionalData(email: String, password: String, firstName: String, lastName: String, dateBirth: String, saldo: String) {

        val map = hashMapOf(
            "email" to email,
            "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "dateBirth" to dateBirth,
            "saldo" to saldo,
        )
        // Sending info to DB
        db.collection("users").document("user ${email}").set(map).addOnCompleteListener {
            if(it.isSuccessful) {
                Toast.makeText(
                    context,
                    "data sent to the database",
                    Toast.LENGTH_LONG
                ).show()
                //clearFields()
            }
        }


    }

    private fun funSignUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
            findNavController().navigate(R.id.signInFragment)
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

    private fun clearFields() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etFirstName.setText("")
        binding.etLastName.setText("")
        binding.etDateBirth.setText("")
    }


}