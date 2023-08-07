package com.example.shopapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.activity.ViewAllActivity
import com.example.shopapp.adapter.CategoryAdapter
import com.example.shopapp.adapter.NewProductsAdapter
import com.example.shopapp.adapter.PopularProductsAdapter
import com.example.shopapp.adapter.ViewAllAdaptter
import com.example.shopapp.databinding.FragmentHomeBinding
import com.example.shopapp.model.Category
import com.example.shopapp.model.NewProducts
import com.example.shopapp.model.PopularProducts
import com.example.shopapp.model.ViewAll
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment(contexts: Context) : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private var lstCate: MutableList<Category> = mutableListOf()
    private var lstNew: MutableList<NewProducts> = mutableListOf()
    private var lstPop: MutableList<PopularProducts> = mutableListOf()
    private var lstViewAll: MutableList<ViewAll> = mutableListOf()

    private val cateAdapter: CategoryAdapter = CategoryAdapter(lstCate, contexts)
    private val newAdapter: NewProductsAdapter = NewProductsAdapter(lstNew, contexts)
    private val popAdapter: PopularProductsAdapter = PopularProductsAdapter(lstPop, contexts)
    private val allAdapter: ViewAllAdaptter = ViewAllAdaptter(lstViewAll, contexts)

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFirestore()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.apply {
            pb.isVisible = true
            scroll.isInvisible = true

            rcvCate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rcvCate.adapter = cateAdapter

            rcvNew.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rcvNew.adapter = newAdapter

            rcvPop.layoutManager = GridLayoutManager(context, 2)
            rcvPop.adapter = popAdapter

            rcvSearch.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvSearch.adapter = allAdapter

            txtViewAllNew.setOnClickListener {
                startActivity(Intent(context, ViewAllActivity::class.java))
            }

            txtViewAllPop.setOnClickListener {
                startActivity(Intent(context, ViewAllActivity::class.java))
            }

            edtSearch.addTextChangedListener {
                if (it.toString().isEmpty()){
                    rcvSearch.isInvisible = true
                    ll.isVisible = true
                    lstViewAll.clear()
                    allAdapter.notifyDataSetChanged()
                }
                else{
                    lstViewAll.clear()
                    allAdapter.notifyDataSetChanged()
                    ll.isInvisible = true
                    rcvSearch.isVisible = true
                    getDataSearch(edtSearch.text.toString())
                }
            }
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataSearch(toString: String) {
        firestore.collection("All").whereEqualTo("name_product", toString)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null){
                    for (doc in it.result){
                        val all = doc.toObject(ViewAll::class.java)
                        lstViewAll.add(all)
                        allAdapter.notifyDataSetChanged()
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFirestore() {
        firestore.collection("Category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val cate = document.toObject(Category::class.java)
                    lstCate.add(cate)
                    cateAdapter.notifyDataSetChanged()

                    binding.pb.isVisible = false
                    binding.scroll.isInvisible = false
                }
            }
        firestore.collection("NewProducts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val products = document.toObject(NewProducts::class.java)
                    lstNew.add(products)
                    newAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
            }
        firestore.collection("PopularProducts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val pop = document.toObject(PopularProducts::class.java)
                    lstPop.add(pop)
                    popAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
            }
    }
}