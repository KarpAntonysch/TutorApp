package com.example.tutor.fireBase

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutor.bd.entities.StudentEntity
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FireBaseViewModel:ViewModel(){


    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>() //AuthResult - Объект результата, полученный в результате операций, которые могут повлиять на состояние
    // аутентификации. Содержит метод, возвращающий текущего пользователя, вошедшего в систему,
    // после завершения операции.
    val userRegistrationStatus: LiveData<Resource<AuthResult>> = _userRegistrationStatus
    // _userRegistrationStatus - для изменения статуса регистрации
    // userRegistrationStatus - для единственного источника данных для UI
    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val fireBaseRepository = FireBaseRepository()
    fun createUser(userName: String, userEmailAddress: String, userLoginPassword: String) {
        val error =
            if (userEmailAddress.isEmpty() || userName.isEmpty() || userLoginPassword.isEmpty()) {
                "Заполните все строки"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailAddress).matches()) {
                "Неверный формат e-mail"
            } else null

        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
            return
        }
        _userRegistrationStatus.postValue(Resource.Loading())
        // viewModelScope - область корутин, привязанная к VM. При очищении VM обнуляется область
        // launch - Запускает новую сопрограмму, не блокируя текущий поток, и возвращает ссылку
        // на сопрограмму как Job . Сопрограмма отменяется, когда результирующее задание отменяется.
        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = fireBaseRepository.createUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)
            _userRegistrationStatus.postValue(registerResult)
        }
    }

    fun signInUser(userEmailAddress: String, userLoginPassword: String) {
        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()) {
            _userSignUpStatus.postValue(Resource.Error("Заполните все строки"))
        } else {
            _userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = fireBaseRepository.login(userEmailAddress, userLoginPassword)
                _userSignUpStatus.postValue(loginResult)
            }
        }
    }
    fun signOut(){
       fireBaseRepository.signOut()
    }

    fun readData(){
        fireBaseRepository.readDataFromDB()
    }
}