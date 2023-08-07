package com.example.shopapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shopapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            btnDangNhap.setOnClickListener {
                login()
            }
            txtDangKi.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun login() {
        val userEmail = binding.edtEmailLogin.text.toString()
        val userPassword = binding.edtPasswordLogin.text.toString()

        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val sharedPref = getSharedPreferences("Login", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("email", userEmail)
                    editor.putString("password", userPassword)
                    editor.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Email or Password k đúng", Toast.LENGTH_LONG).show()
            }
    }
}