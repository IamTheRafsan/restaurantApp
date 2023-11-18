package com.example.ladidh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var searchBtn: Button
    lateinit var menuItem: RecyclerView
    val menuList: ArrayList<HashMap <String, String>> = ArrayList()
    private lateinit var adapter: myAdapter
    private lateinit var menuRecycleView: RecyclerView
    var menuHashMap: HashMap<String, String>? = null
    private lateinit var progressbar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        menuRecycleView = findViewById(R.id.menuRecycleView)
        adapter = myAdapter()
        menuRecycleView.adapter = adapter
        menuRecycleView.layoutManager = LinearLayoutManager(this)

        loadData()


    }

//===================================================Adapter

    private inner class myAdapter : RecyclerView.Adapter<myAdapter.MenuViewHolder>() {

        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ItemName : TextView = itemView.findViewById(R.id.ItemName)
            var ItemPrice : TextView = itemView.findViewById(R.id.ItemPrice)
            var ItemAmount : TextView = itemView.findViewById(R.id.ItemAmount)
            var ItemImage : ImageView = itemView.findViewById(R.id.ItemImage)
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


            }

        }

        override fun getItemCount(): Int {
            return menuList.size
        }
    }


//======================================Load Results




    private fun loadData() {

        val url = "https://www.digitalrangersbd.com/app/ladidh/menu.php?f="


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


                    } catch (e: Exception) {
                        // Handle general exceptions here
                        e.printStackTrace()
                    }
                }

                if (menuList.size > 0) {
                    adapter.notifyDataSetChanged()

                }
            },
            Response.ErrorListener{ error ->
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }




}


