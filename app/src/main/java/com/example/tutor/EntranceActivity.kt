package com.example.tutor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutor.databinding.ActivityEntranceBinding
import com.example.tutor.fireBase.FireBaseViewModel
import com.example.tutor.fireBase.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class EntranceActivity : AppCompatActivity() {
    lateinit var binding: ActivityEntranceBinding
    private val fireBaseViewModel = FireBaseViewModel()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        entrance()
        registration()
        signIn()

    }
    private fun transition(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun signIn(){
        binding.btnEntering.setOnClickListener {
            fireBaseViewModel.signInUser(binding.edMail.text.toString(),binding.edPassword.text.toString())
            fireBaseViewModel.userSignUpStatus.observe(this) {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(applicationContext, "Загрузка", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(applicationContext, "Успешно", Toast.LENGTH_SHORT).show()
                        transition()
                    }
                    is Resource.Error -> {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun registration(){
        binding.btnRegistration.setOnClickListener {
          binding.edRegName.visibility = View.VISIBLE
          binding.edRegSecondName.visibility = View.VISIBLE
            fireBaseViewModel.createUser( binding.edRegName.text.toString()+binding.edRegSecondName.text.toString()
                ,binding.edMail.text.toString(), binding.edPassword.text.toString())
                // реализация статуса
            fireBaseViewModel.userRegistrationStatus.observe(this) {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(applicationContext, "Загрузка", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(applicationContext, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                        val userName = UserProfileChangeRequest.Builder()
                            .setDisplayName(binding.edRegName.text.toString()+binding.edRegSecondName.text.toString()).build()
                        auth.currentUser?.updateProfile(userName)
                        transition()
                    }
                    is Resource.Error -> {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun entrance() {
        val user = auth.currentUser
        if (user != null) {
          transition()
        }
    }

}



