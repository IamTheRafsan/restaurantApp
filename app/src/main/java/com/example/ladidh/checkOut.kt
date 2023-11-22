package com.example.ladidh

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

            val CustomerName = name.toString()
            val CustomerMobile = name.toString()
            val CustomerLocation = name.toString()


        }
    }
}