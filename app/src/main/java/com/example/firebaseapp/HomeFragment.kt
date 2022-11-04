package com.example.firebaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser

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

        //val user = Firebase.auth.currentUser
        /*user?.let {
            val email = user.email
        }*/


        db.collection("users").document("user ${user?.email}").get()
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(context, "UID ${Firebase.auth.currentUser!!.uid}", Toast.LENGTH_SHORT).show()
                    val saldo = it.result.get("saldo").toString()
                    val firstName = it.result.get("firstName").toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()) else it.toString() }
                    val lastName = it.result.get("lastName").toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()) else it.toString() }
                    val completeName = "${firstName} ${lastName}"

                    binding.tvAccountBalance.text = saldo
                    binding.tvNameAPI.text = completeName
                }
            }

        binding.ivProfilePicture.setOnClickListener {
            val tvSaldoValor =  binding.tvAccountBalance.text.toString()
            val doubleSaldoValor = tvSaldoValor.toDouble()
            val actualBalance = doubleSaldoValor + 100.43
            val returnString = String.format("%.2f", actualBalance)
            Toast.makeText(context, "Adicionado +100 e o saldo atual é: ${actualBalance}", Toast.LENGTH_SHORT).show()
            binding.tvAccountBalance.text = returnString.toString()

            returnBalanceToFirebase(returnString)

            /*val stringSaldoValor = tvSaldoValor.toString()
            Toast.makeText(context, "${stringSaldoValor}", Toast.LENGTH_LONG).show()
            val intSaldoValor = Integer.valueOf(stringSaldoValor)
            val saldoAtual = intSaldoValor * 2
            val returnString = saldoAtual.toString()
            binding.tvSaldoValor.text = returnString*/

        }


    }

    private fun returnBalanceToFirebase(returnString: String) {
        val map = hashMapOf(
            "saldo" to returnString,
        )
        db.collection("users").document("user ${user?.email}").set(map, SetOptions.merge()).addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(
                    context,
                    "Balance sent to the database",
                    Toast.LENGTH_LONG
                ).show()
                //clearFields()
            }
        }

    }
}