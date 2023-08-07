package com.example.shopapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopapp.R
import com.example.shopapp.activity.StartActivity
import com.example.shopapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase

class AccountFragment(var mContext: Context) : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    private val db = Firebase.database.getReference("Users")
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)

        binding.apply {
            val id = auth.currentUser?.uid
            if (id != null){
                db.child(id!!)
                    .addValueEventListener(object: ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                val user = snapshot.getValue(com.example.shopapp.model.User::class.java)
                                txtName.text = user!!.userName
                                txtEmail.text = user!!.userEmail
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }

            btnLogout.setOnClickListener {
                if (auth.currentUser != null) {
                    auth.signOut()
                    startActivity(Intent(mContext, StartActivity::class.java))
                }
            }
        }

        return binding.root
    }

}