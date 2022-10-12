package com.example.tutor.fireBase


import com.example.tutor.bd.entities.StudentEntity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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

    suspend fun addStudentToFBCloud(
        studentEntityFB: StudentEntity,
    ) {
        fireStoreDB.collection("Users").document(
                    "${FirebaseAuth.getInstance().currentUser?.uid}"
                ).collection("Students").document().set(studentEntityFB).await()
    }

    // запрос на получение списка студентов из FB отсортированный по хронологии добавления ученика
    private val studentQueryFromFB = fireStoreDB.collection("Users").document(
        "${FirebaseAuth.getInstance().currentUser?.uid}"
    ).collection("Students").orderBy("id", Query.Direction.DESCENDING)

    // функция чтения из бд
    fun getStudents()= flow {
        emit(Resource.Loading())
        emit(Resource.Success(studentQueryFromFB.get().await().documents.mapNotNull { doc ->
            doc.toObject(StudentEntity::class.java)
        }))
    }.catch { error ->
        error.message?.let { errorMessage ->
            emit(Resource.Error(errorMessage))
        }
    }
}


inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}



