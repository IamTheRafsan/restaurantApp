package com.example.ladidh

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class updateInfo : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var mobile: EditText
    private lateinit var location: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var updateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_info)

        name = findViewById(R.id.name)
        mobile = findViewById(R.id.mobile)
        location = findViewById(R.id.location)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        updateBtn = findViewById(R.id.updateBtn)

        updateBtn.setOnClickListener(){

            val Uname = name.text.toString()
            val Umobile = mobile.text.toString()
            val Ulocation = location.text.toString()
            val Upassword = password.text.toString()
            val Uconfirm = confirmPassword.text.toString()

            if (Uname.isNotEmpty() && Umobile.isNotEmpty() && Upassword.isNotEmpty() && Ulocation.isNotEmpty() && Uconfirm.isNotEmpty()){
                if (Upassword == Uconfirm){


                    val url = "https://www.digitalrangersbd.com/app/ladidh/updateUser.php?n"+Uname+"&m="+Umobile+"&l="+Ulocation+"&p="+Upassword

                    val stringRequest = StringRequest(com.android.volley.Request.Method.GET, url,
                        Response.Listener<String> { response ->
                        },
                        Response.ErrorListener { error ->
                        })

                    val requestQueue = Volley.newRequestQueue(this)
                    requestQueue.add(stringRequest)

                    AlertDialog.Builder(this)
                        .setTitle("Profile Updated Successfully!")
                        .setMessage("Information update was successful.")
                        .setNegativeButton("OK") { _, _ -> }
                        .show()



                }
                else
                {
                    AlertDialog.Builder(this)
                        .setTitle("Password Did Not Match!")
                        .setMessage("Please confirm with the same password.")
                        .setNegativeButton("OK") { _, _ -> }
                        .show()
                }

            }
            else
            {
                AlertDialog.Builder(this)
                    .setTitle("Empty Field!")
                    .setMessage("Please fill in all the fields.")
                    .setNegativeButton("OK") { _, _ -> }
                    .show()
            }
        }
    }
}
