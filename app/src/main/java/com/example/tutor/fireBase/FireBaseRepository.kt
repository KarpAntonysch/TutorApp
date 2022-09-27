package com.example.tutor.fireBase

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FireBaseRepository {

    fun addDataToFB(
        firstName: String,
        secondName: String,
        price: Int,
        schoolClass: Int,
        activeStatus: Boolean = true,
        id: Int,
        requireContext: Context
    ) {
        val fireStoreDB = FirebaseFirestore.getInstance()
        val user = hashMapOf("firstName" to firstName,
            "secondName" to secondName,
            "price" to price,
            "schoolClass" to schoolClass,
            "activeStatus" to activeStatus,
            "id" to id)
        fireStoreDB.collection("Users").document("${FirebaseAuth.getInstance()
            .currentUser?.uid}").collection("Students").document().set(user)
            .addOnSuccessListener {
            Toast.makeText(requireContext, "Добавлено в ФаирБэйз", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}


