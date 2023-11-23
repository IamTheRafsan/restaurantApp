package com.example.ladidh

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException


class Login : AppCompatActivity() {

    private lateinit var regName: EditText
    private lateinit var regMobile: EditText
    private lateinit var regLocation: EditText
    private lateinit var regPassword: EditText
    private lateinit var regConfirmPassword: EditText
    private lateinit var signUpBtn: Button
    private lateinit var signInBtn: Button
    private lateinit var regBtn: TextView
    private lateinit var loginBtn: TextView
    private lateinit var progressbar: ProgressBar
    private lateinit var progressbar2: ProgressBar
    private lateinit var regBox: LinearLayout
    private lateinit var loginBox: LinearLayout

    private lateinit var loginMobile: EditText
    private lateinit var loginPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        regName = findViewById(R.id.regName)
        regMobile = findViewById(R.id.regMobile)
        regLocation = findViewById(R.id.regLocation)
        regPassword = findViewById(R.id.regPassword)
        regConfirmPassword = findViewById(R.id.regConfirmPassword)
        signInBtn = findViewById(R.id.signInBtn)
        signUpBtn = findViewById(R.id.signUpBtn)
        regBtn = findViewById(R.id.regBtn)
        loginBtn = findViewById(R.id.loginBtn)
        progressbar = findViewById(R.id.progressbar)
        progressbar2 = findViewById(R.id.progressbar2)
        regBox = findViewById(R.id.regBox)
        loginBox = findViewById(R.id.loginBox)

        loginMobile = findViewById(R.id.loginMobile)
        loginPassword = findViewById(R.id.loginPassword)

        regBtn.setBackgroundColor(resources.getColor(R.color.white))
        regBtn.setTextColor(resources.getColor(R.color.primary))

        regBtn.setOnClickListener(){
            regBox.visibility = View.VISIBLE
            loginBox.visibility = View.GONE

            loginBtn.setBackgroundColor(resources.getColor(R.color.white))
            loginBtn.setTextColor(resources.getColor(R.color.primary))

            regBtn.setBackgroundColor(resources.getColor(R.color.primary))
            regBtn.setTextColor(resources.getColor(R.color.white))
        }

        loginBtn.setOnClickListener(){
            loginBox.visibility = View.VISIBLE
            regBox.visibility = View.GONE

            loginBtn.setBackgroundColor(resources.getColor(R.color.primary))
            loginBtn.setTextColor(resources.getColor(R.color.white))

            regBtn.setBackgroundColor(resources.getColor(R.color.white))
            regBtn.setTextColor(resources.getColor(R.color.primary))
        }


        signUpBtn.setOnClickListener {

            val name = regName.text.toString()
            val mobile = regMobile.text.toString()
            val location = regLocation.text.toString()
            val password = regPassword.text.toString()
            val confirm = regConfirmPassword.text.toString()
            val url = "https://www.digitalrangersbd.com/app/ladidh/user.php?n="+name+"&m="+mobile+"&l="+location+"&p="+password
            val url2 = "https://www.digitalrangersbd.com/app/ladidh/regcon.php?m="+mobile

            if (name.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty()) {
                if (password == confirm) {
                    progressbar2.visibility = View.VISIBLE

                    val jsonArrayRequest = JsonArrayRequest(
                        Request.Method.GET, url2,
                        null,
                        Response.Listener<JSONArray> { response ->
                            val result = response.toString()

                            if (result.length > 3) {
                                progressbar2.visibility = View.GONE
                                AlertDialog.Builder(this)
                                    .setTitle("Mobile Already Exists!")
                                    .setMessage("Please use a new mobile number.")
                                    .setNegativeButton("OK") { _, _ -> }
                                    .show()
                            } else {
                                val stringRequest = StringRequest(
                                    Request.Method.GET, url,
                                    Response.Listener<String> { _ -> },
                                    Response.ErrorListener { _ -> }
                                )

                                val requestQueue =
                                    Volley.newRequestQueue(this.applicationContext)
                                requestQueue.add(stringRequest)
                                progressbar2.visibility = View.GONE

                                AlertDialog.Builder(this)
                                    .setTitle("Congratulations!")
                                    .setMessage("Your Registration Is Completed!")
                                    .setNegativeButton("OK") { _, _ -> }
                                    .show()
                            }
                        },
                        Response.ErrorListener { _ -> }
                    )

                    val requestQueue =
                        Volley.newRequestQueue(this.applicationContext)
                    requestQueue.add(jsonArrayRequest)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Password Did Not Match!")
                        .setMessage("Please confirm with the same password.")
                        .setNegativeButton("OK") { _, _ -> }
                        .show()
                }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Empty Field!")
                    .setMessage("Please fill in all the fields.")
                    .setNegativeButton("OK") { _, _ -> }
                    .show()
            }
        }


        signInBtn.setOnClickListener(View.OnClickListener {

            val sMobile: String = loginMobile.text.toString()
            val sPassword: String = loginPassword.text.toString()

            if (sMobile.isNotEmpty() && sPassword.isNotEmpty()) {

                progressbar.setVisibility(View.VISIBLE)

                val url3 = "https://www.digitalrangersbd.com/app/ladidh/userLogin.php?m=$sMobile&p=$sPassword"

                val jsonArrayRequest = JsonArrayRequest(
                    Request.Method.GET, url3, null,
                    { response ->
                        val result = response.toString()

                        if (result.length < 3) {
                            progressbar.setVisibility(View.GONE)
                            AlertDialog.Builder(this)
                                .setTitle("Wrong Email or Password!")
                                .setMessage("Please put all information correctly.")
                                .setNegativeButton("OK") { dialog, which -> }
                                .show()
                        } else {
                            val url4 = "https://www.digitalrangersbd.com/app/ladidh/userDetail.php?m=$sMobile&p=$sPassword"
                            val detailArrayRequest = JsonArrayRequest(
                                Request.Method.GET, url4, null,
                                { response ->
                                    for (x in 0 until response.length()) {
                                        try {
                                            val jsonObject = response.getJSONObject(x)
                                            val name = jsonObject.getString("name")
                                            val mobile = jsonObject.getString("mobile")
                                            val location = jsonObject.getString("location")

                                            val sharedPreferences: SharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
                                            val editor = sharedPreferences.edit()
                                            editor.putString("userMobile", mobile)
                                            editor.putString("userLocation", location)
                                            editor.putString("userName", name)
                                            editor.apply()

                                            progressbar.visibility = View.GONE

                                        } catch (e: JSONException) {
                                            throw RuntimeException(e)
                                        }
                                    }
                                },
                                { error ->
                                    progressbar.visibility = View.GONE
                                    AlertDialog.Builder(this)
                                        .setTitle("Error")
                                        .setMessage("Error in userDetail.php request: ${error.message}")
                                        .setNegativeButton("OK") { _, _ -> }
                                        .show()
                                }
                            )

                            val requestQueue = Volley.newRequestQueue(applicationContext)
                            requestQueue.add(detailArrayRequest)

                            val myintent = Intent(this, profile::class.java)
                            startActivity(myintent)
                        }
                    },
                    { error ->
                        progressbar.setVisibility(View.GONE)
                        AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Error in userLogin.php request: ${error.message}")
                            .setNegativeButton("OK") { _, _ -> }
                            .show()
                    }
                )

                val requestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.add(jsonArrayRequest)
            } else {
                progressbar.setVisibility(View.GONE)
                AlertDialog.Builder(this)
                    .setTitle("Empty Field!")
                    .setMessage("Please fill in all the fields.")
                    .setNegativeButton("OK") { dialog, which -> }
                    .show()
            }
        })

    }
}
