package com.example.shopapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shopapp.databinding.ActivityRegistrationBinding
import com.example.shopapp.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    private val auth = Firebase.auth
    private val db = Firebase.database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            txtDangNhap.setOnClickListener {
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            btnDangKi.setOnClickListener {
                createUser()
            }
        }
    }

    private fun createUser() {
        binding.apply {
            val userName = edtNameRegister.text.toString()
            val userEmail = edtEmailRegister.text.toString()
            val userPassword = edtPasswordRegister.text.toString()

            if (userName.isEmpty()){
                Toast.makeText(this@RegistrationActivity, "Không được để trống Name", Toast.LENGTH_LONG).show()
            }
            if (userEmail.isEmpty()){
                Toast.makeText(this@RegistrationActivity, "Không được để trống Email", Toast.LENGTH_LONG).show()
            }
            if (userPassword.isEmpty() || userPassword.length<6){
                Toast.makeText(this@RegistrationActivity, "Password phải nhiều hơn 6 kí tự", Toast.LENGTH_LONG).show()
            }

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        val id = it.result.user?.uid
                        val user = User(userName, userEmail, userPassword)
                        if (id != null) {
                            db.child(id).setValue(user)
                        }
                        Toast.makeText(this@RegistrationActivity, "Đăng kí thành công", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@RegistrationActivity, "Đăng kí thất bại", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}