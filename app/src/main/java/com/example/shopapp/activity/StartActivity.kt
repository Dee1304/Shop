package com.example.shopapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shopapp.databinding.ActivityStartBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            btnDangKi.setOnClickListener {
                startActivity(Intent(this@StartActivity, RegistrationActivity::class.java))
            }
            btnDangNhap.setOnClickListener {
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            }
        }
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}