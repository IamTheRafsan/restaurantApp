package com.example.ladidh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    lateinit var menuItem: RecyclerView
    val menuList: ArrayList<HashMap <String, String>> = ArrayList()
    private lateinit var adapter: myAdapter
    private lateinit var menuRecycleView: RecyclerView
    var menuHashMap: HashMap<String, String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuRecycleView = findViewById(R.id.menuRecycleView)
        adapter = myAdapter()
        menuRecycleView.adapter = adapter
        menuRecycleView.layoutManager = LinearLayoutManager(this)

        menuHashMap = HashMap()
        menuHashMap!!["dishName"] = "senderName"
        menuList.add(menuHashMap!!)

        menuHashMap = HashMap()
        menuHashMap!!["dishName"] = "senderName"
        menuList.add(menuHashMap!!)

        menuHashMap = HashMap()
        menuHashMap!!["dishName"] = "senderName"
        menuList.add(menuHashMap!!)

        menuHashMap = HashMap()
        menuHashMap!!["dishName"] = "senderName"
        menuList.add(menuHashMap!!)

        menuHashMap = HashMap()
        menuHashMap!!["dishName"] = "senderName"
        menuList.add(menuHashMap!!)
    }



    private inner class myAdapter : RecyclerView.Adapter<myAdapter.MenuViewHolder>() {

        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var dishName : TextView = itemView.findViewById(R.id.dishName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.menu_item, parent, false)
            return MenuViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            if (position < menuList.size) {


                val menuHashMap = menuList[position]
                val dishName = menuHashMap["dishName"]
                holder.dishName.text = "$dishName"
            }

        }

        override fun getItemCount(): Int {
            return menuList.size
        }
    }



}