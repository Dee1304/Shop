package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopapp.adapter.NewProductsAdapter
import com.example.shopapp.adapter.ViewAllAdaptter
import com.example.shopapp.databinding.ActivityViewAllBinding
import com.example.shopapp.model.Category
import com.example.shopapp.model.PopularProducts
import com.example.shopapp.model.ViewAll
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewAllBinding

    private var lstALL: MutableList<ViewAll> = mutableListOf()

    private val allAdapter: ViewAllAdaptter = ViewAllAdaptter(lstALL, this)

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type")

        binding.apply {
            rcvAll.layoutManager = GridLayoutManager(this@ViewAllActivity, 2)
            rcvAll.adapter = allAdapter
        }

        if (type == null || type.isEmpty()){
            getDataFirestore()
        }
        else{
            getDataFirestoreToType(type)
        }
    }

    private fun getDataFirestoreToType(type: String) {
        db.collection("All").whereEqualTo("type", type)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val all = document.toObject(ViewAll::class.java)
                    lstALL.add(all)
                    allAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun getDataFirestore() {
        db.collection("All")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val all = document.toObject(ViewAll::class.java)
                    lstALL.add(all)
                    allAdapter.notifyDataSetChanged()
                }
            }
    }

}