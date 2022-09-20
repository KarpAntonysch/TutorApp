package com.example.tutor.fireBase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class FireBaseRepository {
    var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    fun addDataToFB(
        firstName: String,
        secondName: String,
        price: Int,
        schoolClass: Int,
        activeStatus: Boolean = true,
        id: Int,
        requireContext: Context
    ) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val student = StudentEntityFB(firstName, secondName, price, schoolClass, activeStatus, id)
        val userID = FirebaseAuth.getInstance().currentUser!!.displayName!!
        database.child(userID).child("Students").child("$firstName $secondName").setValue(student)
            .addOnSuccessListener {
                Toast.makeText(requireContext, "Добавлено в ФаирБэйз", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { exception ->
            Toast.makeText(requireContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    fun readDataFromDB(){
        val postListener = object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val studentEntityFB = snapshot.children
                studentEntityFB.forEach{
                    println(it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.v("FBB","loadPost:onCancelled", error.toException())
            }
        }
       database.addValueEventListener(postListener)
    }
}