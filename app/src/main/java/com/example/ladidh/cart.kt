package com.example.ladidh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class cart : AppCompatActivity() {

    private lateinit var cartRecyleView: RecyclerView
    var cartHashMap: HashMap<String, String>? = null
    var cartList: ArrayList<HashMap <String, String>> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        cartRecyleView = findViewById(R.id.cartRecycleView)
        val adapter = myAdapter()
        cartRecyleView.adapter = adapter
        cartRecyleView.layoutManager = LinearLayoutManager(this)



        val receivedIntent = intent
        if (receivedIntent != null) {
            val receivedMenuList = receivedIntent.getSerializableExtra("cartList") as? ArrayList<HashMap<String, String>>

            if (receivedMenuList != null) {
                // Do something with the received menuList
                this.cartList.addAll(receivedMenuList)
            }
        }





    }

    //==========================recycle View adapter

    private inner class myAdapter : RecyclerView.Adapter<myAdapter.CartViewHolder>() {

        inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ItemName : TextView = itemView.findViewById(R.id.ItemName)
            var ItemPrice : TextView = itemView.findViewById(R.id.ItemPrice)
            var ItemImage : ImageView = itemView.findViewById(R.id.ItemImage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.cart_menu_item, parent, false)
            return CartViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            if (position < cartList.size) {

                val menuHashMap = cartList[position]
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
            return cartList.size
        }
    }



}