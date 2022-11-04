package com.example.firebaseapp.util

import com.google.firebase.firestore.FirebaseFirestore

class firebaseGetBalance(email: String) {
    private val db = FirebaseFirestore.getInstance()

    val emailGet = email

    /*db.collection("users").document("user ${emailGet}").get() {

    }*/

}