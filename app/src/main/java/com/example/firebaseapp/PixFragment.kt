package com.example.firebaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.databinding.FragmentPixBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class PixFragment : Fragment() {

    lateinit var binding: FragmentPixBinding
    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPixBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("users").document("user ${user?.email}").get().addOnCompleteListener { consulta ->
            if (consulta.isSuccessful) {
                val moneyFromFirebase = consulta.result.get("saldo").toString()

                binding.tvBalanceAvailableBalance.text = moneyFromFirebase

            }
        }

        binding.tvIconClose.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.btnArrowRight.setOnClickListener {
        //Numero pra pegar / atualizar tvBalanceAvailableBalance
            val userBalance = binding.tvBalanceAvailableBalance.text.toString()
            val doubleMoney = userBalance.toDouble()

            val userTransferValue = binding.etBalanceValue.text.toString()
            val doubleTransferValue = userTransferValue.toDouble()

            val userNowBalance = doubleMoney - doubleTransferValue

            val returnString = String.format("%.2f", userNowBalance)
            returnBalanceToFirebase(returnString)
            //transferMoney(doubleTransferValue)

            binding.tvBalanceAvailableBalance.setText("${userNowBalance}")
            Toast.makeText(context, "${returnString},${doubleTransferValue} , ${userNowBalance}", Toast.LENGTH_SHORT).show()





            //user?.email?.let { it1 -> firebaseGetBalance(it1) }
            //tirar isso   findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun transferMoney(receiverBalanceMoneyUpdate: String) {
        val userBalance = binding.tvBalanceAvailableBalance.text.toString()
        val doubleMoney = userBalance.toDouble()

        val userTransferValue = binding.etBalanceValue.text.toString()
        val doubleTransferValue = userTransferValue.toDouble()

        val userNowBalance = doubleMoney - doubleTransferValue

        val returnString = String.format("%.2f", userNowBalance)


        val receiverMoneyMoney = receiverBalanceMoneyUpdate
        val receiverEmail = binding.etEmail.text.toString()
        getReceiverActualBalance()
        //Codigo acima pra tentar pegar a funcao embaixo

        // tava ativado     getReceiverActualBalance(DoubleTransferValue)
        // ? val receiverMoney = receiverBalance.toDouble()
        val DoubleTransferValue = doubleTransferValue
        val receivernowBalance = receiverMoneyMoney + DoubleTransferValue

        val returnStringReceiver = String.format("%.2f", receivernowBalance)

        val map = hashMapOf(
            "saldo" to returnStringReceiver
        )

        db.collection("users").document("user ${receiverEmail}").set(map, SetOptions.merge()).addOnCompleteListener{
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
    //receiverEmail: String
    private fun getReceiverActualBalance() {
        val receiverEmail = binding.etEmail.text.toString()
        db.collection("users").document("user ${receiverEmail}").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val receiverBalance = it.result.get("saldo").toString()
                    // preciso mandar pra la transferMoney(receiverBalance)
                    transferMoney(receiverBalance)
                }
            }

        //return teste
    }

    private fun returnBalanceToFirebase(returnString: String) {
        var teste1 = returnString
        val map = hashMapOf(
            "saldo" to teste1,
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