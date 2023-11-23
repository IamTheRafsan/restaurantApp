package com.example.ladidh

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class profile : AppCompatActivity() {

    private lateinit var displayName: TextView
    private lateinit var displayMobile: TextView
    private lateinit var displayLocation: TextView
    private lateinit var logoutBtn: LinearLayout
    private lateinit var settingsBtn: LinearLayout
    private lateinit var ordersBtn: LinearLayout
    private lateinit var backBtn: ImageView
    private lateinit var editBtn: ImageView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        displayName = findViewById(R.id.displayName)
        displayMobile = findViewById(R.id.displayNumber)
        displayLocation = findViewById(R.id.displayLocation)
        logoutBtn = findViewById(R.id.logoutBtn)
        settingsBtn = findViewById(R.id.settingsBtn)
        ordersBtn = findViewById(R.id.ordersBtn)
        backBtn = findViewById(R.id.backBtn)
        editBtn = findViewById(R.id.editBtn)


        val sharedPreferences: SharedPreferences = this.getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

        val uName = sharedPreferences.getString("userName", "")
        val uMobile = sharedPreferences.getString("userMobile", "")
        val uLocation = sharedPreferences.getString("userLocation", "")

        displayName.text = uName
        displayLocation.text = uLocation
        displayMobile.text = uMobile

        logoutBtn.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Yes"){ dialog, which ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .show()
        }

        settingsBtn.setOnClickListener(){
            Toast.makeText(this, "This Page Is Still Under Development", Toast.LENGTH_SHORT).show()
        }

        ordersBtn.setOnClickListener(){
            val intent = Intent(this, myOrders::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Yes"){ dialog, which ->
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
                .show()
        }

        editBtn.setOnClickListener(){
            val intent = Intent(this, updateInfo::class.java)
            startActivity(intent)
        }
    }



}
