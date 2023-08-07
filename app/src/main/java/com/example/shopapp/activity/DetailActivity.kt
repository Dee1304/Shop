package com.example.shopapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ActivityDetailBinding
import com.example.shopapp.model.ViewAll
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val firestore = Firebase.firestore

    private lateinit var viewAll: ViewAll

    private var ql = 1
    private var price = 1
    private var saveTime: String? = null
    private var saveDate: String? = null

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cal = Calendar.getInstance()
        val date = cal.time
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val stf = SimpleDateFormat("hh:mm:ss")
        saveDate = sdf.format(date)
        saveTime = stf.format(date)

        getDataProducts()

        if (viewAll.price != null){
            price = viewAll.price!!.toInt()
        }

        binding.apply {
            btnAddToCart.setOnClickListener {
                addToCart()
            }

            btnBuyNow.setOnClickListener {
                senDetail()
            }

            imgPlus.setOnClickListener {
                if (ql < 1000) {
                    ql++
                    txtQL.text = ql.toString()
                    if (viewAll != null) {
                        price = viewAll.price!!.toInt() * ql
                        binding.txtPrice.text = price.toString()
                    }
                }
            }

            imgMinus.setOnClickListener {
                if (ql > 1) {
                    ql--
                    txtQL.text = ql.toString()
                    price = viewAll.price!!.toInt() * ql
                    binding.txtPrice.text = price.toString()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun senDetail() {
        val intent = Intent(this@DetailActivity, AddressActivity::class.java)
        intent.putExtra("name", viewAll.name_product)
        intent.putExtra("totalPrice", price.toString())
        intent.putExtra("totalQL", ql.toString())
        startActivity(intent)
    }

    private fun addToCart() {
        val cartMap = HashMap<String, String>()
        cartMap["name"] = binding.txtName.text.toString()
        cartMap["price"] = binding.txtPrice.text.toString()
        cartMap["totalPrice"] = price.toString()
        cartMap["totalQl"] = ql.toString()
        cartMap["currentTime"] = saveTime!!
        cartMap["currentDate"] = saveDate!!

        firestore.collection("AddToCart").document("1")
            .collection("User")
            .add(cartMap)
            .addOnCompleteListener {
                Toast.makeText(this, "Add To Cart", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun getDataProducts() {
        val bundle = intent.extras
        viewAll = ViewAll(
            bundle?.getString("img"),
            bundle?.getString("name"),
            bundle?.getString("describe"),
            bundle?.getString("price")
        )

        Glide.with(this).load(viewAll.img_url).into(binding.imgProduct)
        binding.txtName.text = viewAll.name_product
        binding.txtDescribe.text = viewAll.describe
        binding.txtPrice.text = viewAll.price
    }
}