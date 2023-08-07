package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.R
import com.example.shopapp.adapter.CategoryAdapter
import com.example.shopapp.adapter.NewProductsAdapter
import com.example.shopapp.adapter.PopularProductsAdapter
import com.example.shopapp.databinding.ActivityMainBinding
import com.example.shopapp.fragment.AccountFragment
import com.example.shopapp.fragment.CartFragment
import com.example.shopapp.fragment.HomeFragment
import com.example.shopapp.model.Category
import com.example.shopapp.model.NewProducts
import com.example.shopapp.model.PopularProducts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()

        binding.bnv.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment(this))
                R.id.cart -> replaceFragment(CartFragment(this))
                R.id.account -> replaceFragment(AccountFragment(this))
            }
            true
        }

    }

    private fun checkLogin() {
        val sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        if (email!!.isEmpty() && password!!.isEmpty()){
            Toast.makeText(this, "Please login", Toast.LENGTH_LONG).show()
        }
        else{
            replaceFragment(HomeFragment(this))
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl, fragment)
            .commit()
    }

}