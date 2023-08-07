package com.example.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.activity.DetailActivity
import com.example.shopapp.databinding.CustomRcvCartBinding
import com.example.shopapp.model.MyCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartAdapter(private var lst: MutableList<MyCart>, var context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private lateinit var binding: CustomRcvCartBinding
    private var totalPriceCart = 0
    private val firestore  = Firebase.firestore

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        binding = CustomRcvCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        binding.apply {
            holder.itemView.apply {
                txtName.text = lst[position].name
                txtPrice.text = lst[position].totalPrice
                txtDate.text = lst[position].currentDate
                txtTime.text = lst[position].currentTime
                txtQL.text = lst[position].totalQl
                imgDelete.setOnClickListener {
                    delete(position)
                }

                var totalPrice = lst[position].totalPrice?.toInt()

                totalPriceCart += totalPrice!!

                val intent = Intent("MyTotalCart")
                intent.putExtra("totalPriceCart",totalPriceCart)

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }

    private fun delete(position: Int) {
        val id = lst[position].id
        firestore.collection("AddToCart")
            .document("1")
            .collection("User")
            .document(id!!)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    lst.removeAt(position)
                    notifyDataSetChanged()
                }
            }
    }
}