package com.example.tutor.fireBase


import com.example.tutor.bd.entities.StudentEntity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FireBaseRepository {
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    //функция для регистрации c возвращаемым типом Resource<AuthResult>
    suspend fun createUser(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
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

    // запрос на добавение студента в FB. В FB документ будет называться по id
    suspend fun addStudentToFBCloud(studentEntityFB: StudentEntity) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document(studentEntityFB.id.toString()).set(studentEntityFB)
            .await()
    }

    // запрос на получение списка студентов из FB и добавление в лБД
    private fun studentQueryFromFB(): Query {
        return fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students")
    }


    /*функция чтения из бд. Альтернативное исопльзование корутин без suspend, но с flow(предпочтитльнее для
    получения нескольких объектов).обработка ошибок не через общую функцию высшего порядка,
    а напрямую в методе.НЕ ИСПОЛЬЗУЮ, ДЛЯ ПРИМЕРА*/
    /*fun getStudents()= flow {
        emit(Resource.Loading())
        emit(Resource.Success(studentQueryFromFB(*//*true*//*).get().await().documents.mapNotNull { doc ->
            doc.toObject(StudentEntity::class.java)
        }))
    }.catch { error ->
        error.message?.let { errorMessage ->
            emit(Resource.Error(errorMessage))
        }
    }*/
    // получение списка студентов из FB. С использованием корутины(в виде suspend).
    suspend fun fbStudentList(): List<StudentEntity> {
        val fbStudentList = studentQueryFromFB().get().await().documents.mapNotNull { doc ->
            doc.toObject(StudentEntity::class.java)
        }
        return fbStudentList
    }

    // обновление активности ученика с true на false.
    fun changeStudentActiveToFireBase(studentEntityFB: StudentEntity, activeStatus: Boolean) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document(studentEntityFB.id.toString())
            .update("activeStatus", activeStatus)
    }

    // Изменение deleteStatus
    fun changeDeleteStatusToFireBase(studentEntityFB: StudentEntity) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document(studentEntityFB.id.toString())
            .update("deleteStatus", false)
    }
    // удаление ученика из FB
    /*fun deleteStudentFromFB(studentEntityFB: StudentEntity) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document( studentEntityFB.id.toString()).delete()
    }*/

    // обновление информации об ученике(полей документа)
    fun changeStudentFieldsToFireBase(
        studentEntityFB: StudentEntity, firstName: String, secondName: String,
        schoolClass: Int, price: Int, phoneNumber: String,
    ) {
        fireStoreDB.collection("Users").document(
            "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).collection("Students").document(studentEntityFB.id.toString()).update("firstName",
            firstName,
            "secondName",
            secondName,
            "schoolClass",
            schoolClass,
            "price",
            price,
            "phoneNumber",
            phoneNumber)
    }

    // функция выхода из учетной записи
    fun signOut() {
        firebaseAuth.signOut()
    }
}

// функция высшего порядка для вызовов действий FB
inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()// сюда попадает функция, которая возвращает Resource. Если возникает ошибка ее ловит catch
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}



