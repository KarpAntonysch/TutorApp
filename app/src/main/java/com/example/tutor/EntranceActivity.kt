package com.example.tutor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutor.databinding.ActivityEntranceBinding
import com.google.firebase.auth.FirebaseAuth

class EntranceActivity : AppCompatActivity() {
    lateinit var binding: ActivityEntranceBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        registration()
        login()
    }

    private fun registration() {
        binding.btnRegistration.setOnClickListener {
            val email = binding.edMail.text.toString()
            val password = binding.edPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun login() {
        binding.btnEntering.setOnClickListener {
            val email = binding.edMail.text.toString()
            val password = binding.edPassword.text.toString()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}



