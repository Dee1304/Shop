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
import com.example.shopapp.databinding.CustomRcvPopularProductsBinding
import com.example.shopapp.model.ViewAll

class ViewAllAdaptter(private var lst: List<ViewAll>, var context: Context) : RecyclerView.Adapter<ViewAllAdaptter.ViewAllViewHolder>() {
    private lateinit var binding: CustomRcvPopularProductsBinding

    inner class ViewAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder {
        binding = CustomRcvPopularProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAllViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: ViewAllViewHolder, position: Int) {
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