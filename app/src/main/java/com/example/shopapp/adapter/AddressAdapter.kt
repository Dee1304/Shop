package com.example.shopapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.databinding.CustomRcvAddressBinding
import com.example.shopapp.model.Address

class AddressAdapter(
    private var lst: List<Address>,
    var context: Context,
    private var selectedAddress: SelectedAddress
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private lateinit var binding: CustomRcvAddressBinding

    private var radioButton: RadioButton? = null

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        binding =
            CustomRcvAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        binding.apply {
            holder.itemView.apply {
                txtName.text ="Name: " + lst[position].name
                txtAddress.text ="Address: " + lst[position].address
                txtPhone.text ="Phone: " + lst[position].phone
                rb.isEnabled = false
            }

            holder.itemView.setOnClickListener {
                for (address in lst) {
                    address.selected = false
                }
                lst[position].selected = true

                // Nếu có Radio Button seleted thì unseleted
                if (radioButton != null) {
                    radioButton!!.isChecked = false
                }

                // Gán bằng Radio Button click
                radioButton = it.findViewById(R.id.rb) as RadioButton
                radioButton!!.isChecked = lst[position].selected
                selectedAddress!!.setAddress("Name: " + lst[position].name + " | Address: " + lst[position].address + " | Phone: " + lst[position].phone)
            }
        }
    }

    interface SelectedAddress {
        fun setAddress(address: String)
    }
}