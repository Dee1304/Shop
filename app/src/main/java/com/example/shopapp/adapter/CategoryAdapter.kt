package com.example.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.activity.ViewAllActivity
import com.example.shopapp.databinding.CustomRcvCategoryBinding
import com.example.shopapp.model.Category
import com.example.shopapp.model.ViewAll

class CategoryAdapter(private var lst: List<Category>, var context: Context): RecyclerView.Adapter<CategoryAdapter.CategoryiewHolder>() {
    private lateinit var binding: CustomRcvCategoryBinding
    inner class CategoryiewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryiewHolder {
        binding = CustomRcvCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryiewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: CategoryiewHolder, position: Int) {
        binding.apply {
            holder.itemView.apply {
                Glide.with(context).load(lst[position].img_url).into(imgCate)
                txtNameCate.text = lst[position].name
            }
            holder.itemView.setOnClickListener{
                val intent = Intent(context, ViewAllActivity::class.java)
                intent.putExtra("type", lst[position].type)
                context.startActivity(intent)
            }
        }
    }
}