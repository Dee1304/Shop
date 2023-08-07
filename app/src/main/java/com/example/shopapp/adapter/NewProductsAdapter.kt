package com.example.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.activity.DetailActivity
import com.example.shopapp.databinding.CustomRcvNewProductsBinding
import com.example.shopapp.model.NewProducts

class NewProductsAdapter(private var lst: List<NewProducts>, var context: Context) : RecyclerView.Adapter<NewProductsAdapter.NewProductsViewHolder>() {
    private lateinit var binding: CustomRcvNewProductsBinding

    inner class NewProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewProductsViewHolder {
        binding = CustomRcvNewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewProductsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: NewProductsViewHolder, position: Int) {
        binding.apply {
            holder.itemView.apply {
                Glide.with(context).load(lst[position].img_url).into(imgCate)
                txtNameNewProducts.text = lst[position].name_product
                txtPrice.text = lst[position].price
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                val bundle = Bundle()
                bundle.putString("img", lst[position].img_url)
                bundle.putString("name", lst[position].name_product)
                bundle.putString("describe", lst[position].describe)
                bundle.putString("price", lst[position].price)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        }
    }
}