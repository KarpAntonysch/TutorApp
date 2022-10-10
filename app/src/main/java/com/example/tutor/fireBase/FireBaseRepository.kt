package com.example.tutor.fireBase


import com.example.tutor.bd.entities.StudentEntity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FireBaseRepository {
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signOut() {
        firebaseAuth.signOut()
    }

    //функция c возвращаемым тип AuthResult, которой заключен в Resource класс
    suspend fun createUser(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult =
                    firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword)
                        .await()
                val profileName =
                    UserProfileChangeRequest.Builder().setDisplayName(userName).build()
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
        studentEntityFB: StudentEntity,
    ) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document().set(studentEntityFB)
    }

    fun readDataFromDB(): List<StudentEntity> {
        val userList = arrayListOf<StudentEntity>()
        fireStoreDB.collection("Users").document(
            "${
                FirebaseAuth.getInstance()
                    .currentUser?.uid
            }"
        ).collection("Students").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val firstName = document.data["firstName"].toString()
                val secondName = document.data["secondName"].toString()
                val price = document.data["price"].toString().toInt()
                val schoolClass = document.data["schoolClass"].toString().toInt()
                val activeStatus = document.data["activeStatus"].toString().toBoolean()
                val id = document.data["id"].toString().toInt()
                val student =
                    StudentEntity(firstName, secondName, price, schoolClass, activeStatus)
                student.id = id
                userList.add(student)
                println("qqqqq $userList ")/* ${document } =>${document.data}*/
            }

        }
        return userList
    }
}


inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}



