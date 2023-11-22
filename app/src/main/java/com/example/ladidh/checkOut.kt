package com.example.ladidh

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.DecimalFormat

class checkOut : AppCompatActivity() {

    private lateinit var coupon_Price: TextView
    private lateinit var name: EditText
    private lateinit var mobile: EditText
    private lateinit var location: EditText
    private lateinit var confirmBtn: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        coupon_Price = findViewById(R.id.total_price)
        name = findViewById(R.id.name)
        mobile = findViewById(R.id.mobile)
        location = findViewById(R.id.location)
        confirmBtn = findViewById(R.id.confirm)





        val receivedIntent = intent
        if (receivedIntent != null) {
            val couponPrice = receivedIntent.getDoubleExtra("couponPrice", 0.0)

            val decimalFormat = DecimalFormat("#.##")
            val formattedCouponPrice = decimalFormat.format(couponPrice)
            coupon_Price.text = formattedCouponPrice
        }


        confirmBtn.setOnClickListener(){

            val CustomerName = name.text.toString()
            val CustomerMobile = mobile.text.toString()
            val CustomerLocation = location.text.toString()
            val CustomeBill = coupon_Price.text.toString()
            val status = "confirmed"

            val url = "https://www.digitalrangersbd.com/app/ladidh/order.php?n="+CustomerName+"&m="+CustomerMobile+"&l="+CustomerLocation+"&b="+CustomeBill+"&s="+status

            if(CustomerName.isEmpty() || CustomerMobile.isEmpty() || CustomerLocation.isEmpty())
            {
                AlertDialog.Builder(this)
                    .setTitle("Empty Fields!")
                    .setMessage("Please put all the information.")
                    .setNegativeButton("OK") { dialog, which -> }
                    .show()

            }
            else
            {
                val stringRequest = StringRequest(com.android.volley.Request.Method.GET, url,
                    Response.Listener<String> { response ->
                    },
                    Response.ErrorListener { error ->
                    })

                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)

                AlertDialog.Builder(this)
                    .setTitle("Congrats! Order Confirmed.")
                    .setMessage("Note: You will be charged Tk.2 per km from our location as delivery charges.")
                    .setNegativeButton("OK") { dialog, which -> }
                    .show()

            }




        }
    }
}