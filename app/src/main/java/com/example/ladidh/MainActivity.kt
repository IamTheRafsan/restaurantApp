package com.example.ladidh

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso



class MainActivity<JSONException : Any> : AppCompatActivity() {

    private lateinit var searchBtn: ImageView
    val menuList: ArrayList<HashMap <String, String>> = ArrayList()
    private lateinit var adapter: myAdapter
    private lateinit var menuRecycleView: RecyclerView
    var menuHashMap: HashMap<String, String>? = null
    private lateinit var progressbar: ProgressBar
    var food:String = " "
    private lateinit var searchText : EditText
    val cartList: ArrayList<HashMap <String, String>> = ArrayList()
    private lateinit var cartpage: LinearLayout
    private lateinit var offers: LinearLayout
    private lateinit var profile: LinearLayout




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cartList.clear()

        searchBtn = findViewById(R.id.searchBtn)
        searchText = findViewById(R.id.searchText)
        progressbar = findViewById(R.id.progressbar)
        cartpage = findViewById(R.id.cart)
        offers = findViewById(R.id.offer)
        profile = findViewById(R.id.profile)



        progressbar.visibility = View.VISIBLE

        menuRecycleView = findViewById(R.id.menuRecycleView)
        adapter = myAdapter()
        menuRecycleView.adapter = adapter
        menuRecycleView.layoutManager = LinearLayoutManager(this)

        loadData()

        searchBtn.setOnClickListener{
            food = searchText.text.toString()
            loadData()
        }

        cartpage.setOnClickListener(){
            val intent = Intent(this, cart::class.java)
            intent.putExtra("cartList", cartList)
            startActivity(intent)
        }
        profile.setOnClickListener(){
            val intent = Intent(this, profile::class.java)
            startActivity(intent)
        }

    }

//===================================================Adapter

    private inner class myAdapter : RecyclerView.Adapter<myAdapter.MenuViewHolder>() {

        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ItemName : TextView = itemView.findViewById(R.id.ItemName)
            var ItemPrice : TextView = itemView.findViewById(R.id.ItemPrice)
            var ItemImage : ImageView = itemView.findViewById(R.id.ItemImage)
            val AddToCartBtn : TextView = itemView.findViewById(R.id.AddToCartBtn)
            val GoToCartBtn : TextView = itemView.findViewById(R.id.GoToCartBtn)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.menu_item, parent, false)
            return MenuViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            if (position < menuList.size) {

                val menuHashMap = menuList[position]
                val name = menuHashMap["name"]
                val description = menuHashMap["description"]
                val price = menuHashMap["price"]
                val image = menuHashMap["image"]

                holder.ItemName.text = name
                holder.ItemPrice.text = price

                Picasso.get().load(image).into(holder.ItemImage)

                val isInCart = cartList.any { it["name"] == name }

                if (isInCart) {
                    holder.AddToCartBtn.visibility = View.GONE
                    holder.GoToCartBtn.visibility = View.VISIBLE
                } else {
                    holder.AddToCartBtn.visibility = View.VISIBLE
                    holder.GoToCartBtn.visibility = View.GONE
                }

                holder.AddToCartBtn.setOnClickListener {
                    val cartHashMap = HashMap<String, String>()
                    cartHashMap["name"] = name ?: ""
                    cartHashMap["price"] = price ?: ""
                    cartHashMap["image"] = image ?: ""

                    cartList.add(cartHashMap)
                    holder.AddToCartBtn.visibility = View.GONE
                    holder.GoToCartBtn.visibility = View.VISIBLE
                }

                holder.GoToCartBtn.setOnClickListener {
                    val intent = Intent(holder.itemView.context, cart::class.java)
                    intent.putExtra("cartList", cartList)
                    holder.itemView.context.startActivity(intent)
                }


            }
        }

        override fun getItemCount(): Int {
            return menuList.size
        }
    }


//======================================Load Results




    private fun loadData() {

        menuList.clear()

        val url = "https://www.digitalrangersbd.com/app/ladidh/menu.php?f="+food


        val jsonArrayRequest = JsonArrayRequest(com.android.volley.Request.Method.GET, url, null, Response.Listener
            { response ->
                for (x in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(x)
                        val name = jsonObject.getString("name")
                        val description = jsonObject.getString("description")
                        val price = jsonObject.getString("price")
                        val image = jsonObject.getString("image")


                        menuHashMap = HashMap()
                        menuHashMap!!["name"] = name
                        menuHashMap!!["description"] = description
                        menuHashMap!!["price"] = price
                        menuHashMap!!["image"] = image
                        menuList.add(menuHashMap!!)

                        progressbar.visibility = View.GONE


                    } catch (e: Exception) {
                        // Handle general exceptions here
                        e.printStackTrace()
                    }
                }

                if (menuList.size > 0) {
                    adapter.notifyDataSetChanged()

                }

                if (menuList.isEmpty()) {
                    adapter.notifyDataSetChanged()
                    progressbar.visibility = View.GONE

                    AlertDialog.Builder(this)
                        .setTitle("Sorry! No results found.")
                        .setMessage("Please search something else.")
                        .setNegativeButton("OK") { dialog, which -> }
                        .show()
                } else {
                    adapter.notifyDataSetChanged()
                    progressbar.visibility = View.GONE
                }
            },
            Response.ErrorListener{ error ->
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }




}


