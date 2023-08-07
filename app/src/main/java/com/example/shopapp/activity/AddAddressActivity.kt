package com.example.shopapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shopapp.databinding.ActivityAddAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAddressBinding
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnAddAddress.setOnClickListener {
                val name = edtName.text.toString()
                val address = edtAddress.text.toString()
                val phone = edtPhone.text.toString()

                if ( name.isEmpty() || address.isEmpty() || phone.isEmpty()){
                    Toast.makeText(this@AddAddressActivity, "Không được để trống thông tin", Toast.LENGTH_LONG).show()
                }

                if (name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty()){
                    val map = HashMap<String, String>()
                    map["name"] = name
                    map["address"] = address
                    map["phone"] = phone

                    firestore.collection("CurrentUser").document("1")
                        .collection("Address")
                        .add(map)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this@AddAddressActivity, "Add Address thành công", Toast.LENGTH_LONG).show()
                            }
                        }
                    startActivity(Intent(this@AddAddressActivity, AddressActivity::class.java))
                }


            }
        }


    }
}