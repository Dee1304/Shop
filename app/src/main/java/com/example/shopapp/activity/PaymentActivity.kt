package com.example.shopapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.shopapp.databinding.ActivityPaymentBinding
import com.example.shopapp.model.MyCart
import com.example.shopapp.model.ViewAll
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    private val firestore = Firebase.firestore

    private var lstCart: MutableList<MyCart> = mutableListOf()
    private lateinit var viewAll: ViewAll

    private var subTotal = 0
    private var total = 0
    private var discount = 0

    private var mAddress: String? = null
    private var name: String? = null
    private var ql: String? = null
    private var saveTime: String? = null
    private var saveDate: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataIntent()

        tinhTong()

        total = subTotal - discount

        binding.apply {
            txtSubTotal.text = subTotal.toString()
            txtTotal.text = total.toString()
            btnCheckOut.setOnClickListener {
                putDataFirestore()
                startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            }
        }

    }

    private fun tinhTong() {
        for (l in lstCart){
            if (l.totalPrice != null){
                subTotal += l.totalPrice!!.toInt()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun putDataFirestore() {
        if (lstCart.size > 0) {
            for (cart in lstCart) {
                val calendar = Calendar.getInstance()
                val date = calendar.time
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val stf = SimpleDateFormat("hh:mm:ss")

                saveTime = stf.format(date)
                saveDate = sdf.format(date)

                var map = HashMap<String?, String?>()
                map["name_product"] = cart.name
                map["totalQl"] = cart.totalQl
                map["sub_total"] = cart.totalPrice
                map["address"] = mAddress
                map["currentTime"] = saveTime
                map["currentDate"] = saveDate

                firestore.collection("Order")
                    .document("1")
                    .collection("DetailOrder")
                    .add(map)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Order thành công", Toast.LENGTH_LONG).show()

                        val intent = Intent("DeleteItemCart")
                        intent.putExtra("${cart.id}", cart.id)
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDataIntent() {
        if (intent.getSerializableExtra("cart") != null){
            lstCart = intent.getSerializableExtra("cart") as MutableList<MyCart>
        }
        mAddress = intent.getStringExtra("address")
        name = intent.getStringExtra("name")
        val s = intent.getStringExtra("totalPrice")
        if (s != null) {
            subTotal = s.toInt()
        }
        ql = intent.getStringExtra("totalQL")
        val cart = MyCart("", saveTime, saveDate, name, "", subTotal.toString(), ql)
        lstCart.add(cart)
//        binding.txt.text = "${lstCart[0].name} ${lstCart[0].totalPrice} ${lstCart[0].totalQl} ${lstCart[0].currentTime} ${lstCart[0].currentDate} $mAddress"
    }
}