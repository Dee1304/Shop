package com.example.shopapp.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.activity.AddressActivity
import com.example.shopapp.adapter.CartAdapter
import com.example.shopapp.databinding.FragmentCartBinding
import com.example.shopapp.model.MyCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class CartFragment(private var contexts: Context) : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private val firestore = Firebase.firestore

    private var lstCart: MutableList<MyCart> = mutableListOf()

    private val cartAdapter: CartAdapter = CartAdapter(lstCart, contexts)

    private val mMessageReceiver = MyReceiver()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)

        getDataCart()

        binding.apply {
            rcvCart.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvCart.adapter = cartAdapter

            btnBuyNow.setOnClickListener {
                sendCart()
            }
        }

        LocalBroadcastManager.getInstance(contexts)
            .registerReceiver(mMessageReceiver, IntentFilter("MyTotalCart"))
        LocalBroadcastManager.getInstance(contexts)
            .registerReceiver(mMessageReceiver, IntentFilter("DeleteItemCart"))

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendCart() {
        val intent = Intent(context, AddressActivity::class.java)
        intent.putExtra("cart", lstCart as Serializable)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataCart() {
        firestore.collection("AddToCart")
            .document("1")
            .collection("User")
            .get()
            .addOnCompleteListener {
                it.result
            }
            .addOnSuccessListener {
                for (doc in it) {
                    val id = doc.id
                    val cart = doc.toObject(MyCart::class.java)
                    cart.id = id
                    lstCart.add(cart)
                    lstCart.sortByDescending { sort ->
                        sort.currentDate
                    }
                    lstCart.sortByDescending { sort ->
                        sort.currentTime
                    }
                    cartAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {  }
    }

    @Suppress("UNREACHABLE_CODE")
    inner class MyReceiver : BroadcastReceiver() {

        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val totalPriceCart = p1!!.getIntExtra("totalPriceCart", 0)
            binding.txtPriceCart.text = totalPriceCart.toString()

            for (cart in lstCart) {
                var mId: String? = null
                mId = p1.getStringExtra("${cart.id}")
                if (mId != null) {
                    firestore.collection("AddToCart")
                        .document("1")
                        .collection("User")
                        .document(mId!!)
                        .delete()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (cart.id.equals(mId)) {
                                    lstCart.remove(cart)
                                }
                                cartAdapter.notifyDataSetChanged()
                            }
                        }
                }
            }
        }
    }

}