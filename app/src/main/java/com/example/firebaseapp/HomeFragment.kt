package com.example.firebaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        /*user?.let {
            val email = user.email
        }*/


        db.collection("users").document("user ${user?.email}").get()
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    val saldo = it.result.get("saldo").toString()
                    val firstName = it.result.get("firstName").toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()) else it.toString() }
                    val lastName = it.result.get("lastName").toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()) else it.toString() }
                    val completeName = "${firstName} ${lastName}"

                    binding.tvSaldoValor.text = saldo
                    binding.tvNameAPI.text = completeName
                }
            }


    }
}