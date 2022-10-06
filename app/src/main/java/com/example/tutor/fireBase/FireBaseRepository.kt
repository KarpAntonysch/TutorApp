package com.example.tutor.fireBase

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FireBaseRepository {
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signOut(){
        firebaseAuth.signOut()
    }
    //функция c возвращаемым тип AuthResult, которой заключен в Resource класс
    suspend fun createUser(userName: String, userEmailAddress: String, userLoginPassword: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()
                val profileName = UserProfileChangeRequest.Builder().setDisplayName(userName).build()
                firebaseAuth.currentUser?.updateProfile(profileName)
                Resource.Success(registrationResult)
            }
        }
    }
    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }
    fun addStudentToFBCloud(
        studentEntityFB: StudentEntityFB,
        requireContext: Context
    ) {
        fireStoreDB.collection("Users").document("${FirebaseAuth.getInstance()
            .currentUser?.uid}").collection("Students").document().set(studentEntityFB)
            .addOnSuccessListener {
                Toast.makeText(requireContext, "Добавлено в ФаирБэйз", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

   /* fun  readDataFromDB(): Task<QuerySnapshot> {
        return fireStoreDB.collection("Users").document("${FirebaseAuth.getInstance()
            .currentUser?.uid}").collection("Students").get().addOnSuccessListener {
            print(it.documentChanges)
        }
    }*/
    }


inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}



