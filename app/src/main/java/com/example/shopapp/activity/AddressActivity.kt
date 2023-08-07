package com.example.shopapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.adapter.AddressAdapter
import com.example.shopapp.databinding.ActivityAddressBinding
import com.example.shopapp.model.Address
import com.example.shopapp.model.MyCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class AddressActivity : AppCompatActivity(), AddressAdapter.SelectedAddress {
    private lateinit var binding: ActivityAddressBinding

    private var lstAddress: MutableList<Address> = arrayListOf()
    private var lstCart: MutableList<MyCart> = mutableListOf()

    private val addressAdapter = AddressAdapter(lstAddress, this, this)

    private val firestore = Firebase.firestore

    private var mAddress = ""
    private var name: String? = null
    private var totalPrice: String? = null
    private var totalQL: String? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataIntent()

        getDataFirestore()

        binding.apply {
            rcvAddress.layoutManager = LinearLayoutManager(this@AddressActivity,LinearLayoutManager.VERTICAL, false)
            rcvAddress.adapter = addressAdapter

            btnAddAddress.setOnClickListener {
                startActivity(Intent(this@AddressActivity, AddAddressActivity::class.java))
            }

            btnPayment.setOnClickListener {
                var selected = false
                for (select in lstAddress){
                    selected = select.selected
                    if (selected){
                        break
                    }
                }
                if (selected){
                    sendOrderToPayment()
                }
                else{
                    Toast.makeText(this@AddressActivity, "Please chose address", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendOrderToPayment() {
        val intent = Intent(this@AddressActivity, PaymentActivity::class.java)
        intent.putExtra("cart", lstCart as java.io.Serializable)
        intent.putExtra("address", mAddress)
        intent.putExtra("name", name)
        intent.putExtra("totalPrice", totalPrice)
        intent.putExtra("totalQL", totalQL)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private fun getDataIntent() {
        name = intent.getStringExtra("name")
        totalPrice = intent.getStringExtra("totalPrice")
        totalQL = intent.getStringExtra("totalQL")

        binding.txt.text = "$name $totalPrice $totalQL"

        if(intent.getSerializableExtra("cart") != null){
            lstCart = intent.getSerializableExtra("cart") as MutableList<MyCart>
//            binding.txt.text = "${lstCart[3].name} ${lstCart[3].totalPrice} ${lstCart[3].totalQl} "
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFirestore() {
        firestore.collection("CurrentUser")
            .document("1")
            .collection("Address")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    val address = doc.toObject(Address::class.java)
                    lstAddress.add(address)
                    addressAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun setAddress(address: String) {
        mAddress = address
    }
}