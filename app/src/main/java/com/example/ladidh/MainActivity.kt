package com.example.ladidh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    lateinit var menuItem: RecyclerView
    val menuList: ArrayList<HashMap <String, String>> = ArrayList()
    private lateinit var adapter: myAdapter
    private lateinit var menuRecycleView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuRecycleView = findViewById(R.id.menuRecycleView)
        adapter = myAdapter()
        menuRecycleView.adapter = adapter
        menuRecycleView.layoutManager = LinearLayoutManager(this)
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
            val currentItem = menuList[position]
            holder.dishName.text = currentItem["dishName"]

        }

        override fun getItemCount(): Int {
            return menuList.size
        }
    }



}