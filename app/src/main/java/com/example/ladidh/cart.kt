package com.example.ladidh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.math.BigDecimal



class cart : AppCompatActivity() {

    private lateinit var cartRecyleView: RecyclerView
    var cartList: ArrayList<HashMap<String, String>> = ArrayList()
    var menuHashMap: HashMap<String, String>? = null
    var couponList: ArrayList<HashMap<String, String>> = ArrayList()
    var couponHashMap: HashMap<String, String>? = null
    private lateinit var item_total: TextView
    private lateinit var total_price: TextView
    private lateinit var applyBtn: TextView
    private lateinit var coupon_code: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        item_total = findViewById(R.id.itemTotal)
        applyBtn = findViewById(R.id.apply)
        cartRecyleView = findViewById(R.id.cartRecycleView)
        total_price = findViewById(R.id.total_price)
        coupon_code = findViewById(R.id.coupon)

        val adapter = myAdapter()
        cartRecyleView.adapter = adapter
        cartRecyleView.layoutManager = LinearLayoutManager(this)

        val receivedIntent = intent
        if (receivedIntent != null) {
            val receivedMenuList = receivedIntent.getSerializableExtra("cartList") as? ArrayList<HashMap<String, String>>

            if (receivedMenuList != null) {
                this.cartList.addAll(receivedMenuList)
            }
        }

        updateTotalPrice()

        applyBtn.setOnClickListener() {

            val couponCode = coupon_code.text.toString()
            couponList.clear()

            val url = "https://www.digitalrangersbd.com/app/ladidh/coupon.php?c="+couponCode

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null, Response.Listener
                { response ->

                    for (x in 0 until response.length()) {
                        try {
                            val jsonObject = response.getJSONObject(x)
                            val code = jsonObject.getString("code")
                            val percent = jsonObject.getString("percent")
                            val max = jsonObject.getString("max")
                            val state = jsonObject.getString("state")

                            couponHashMap = HashMap()
                            couponHashMap!!["code"] = code
                            couponHashMap!!["perent"] = percent
                            couponHashMap!!["max"] = max
                            couponHashMap!!["state"] = state

                            couponList.add(couponHashMap!!)



                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                }
            )

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(jsonArrayRequest)
        }




    }

    //==========================recycle View adapter

    private inner class myAdapter : RecyclerView.Adapter<myAdapter.CartViewHolder>() {

        inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ItemName: TextView = itemView.findViewById(R.id.ItemName)
            var ItemPrice: TextView = itemView.findViewById(R.id.ItemPrice)
            var ItemImage: ImageView = itemView.findViewById(R.id.ItemImage)
            var ItemAmount: TextView = itemView.findViewById(R.id.ItemAmount)
            var plus: ImageView = itemView.findViewById(R.id.plus)
            var minus: ImageView = itemView.findViewById(R.id.minus)
            var itemCount = 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.cart_menu_item, parent, false)
            return CartViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            if (position < cartList.size) {
                var menuHashMap = cartList[position]
                val name = menuHashMap["name"]
                val description = menuHashMap["description"]
                val price = menuHashMap["price"]
                val image = menuHashMap["image"]

                holder.ItemName.text = name
                holder.ItemPrice.text = price
                Picasso.get().load(image).into(holder.ItemImage)

                holder.ItemAmount.text = holder.itemCount.toString()
                menuHashMap["itemCount"] = holder.itemCount.toString()

                holder.plus.setOnClickListener {
                    holder.itemCount++
                    holder.ItemAmount.text = holder.itemCount.toString()

                    menuHashMap["itemCount"] = holder.itemCount.toString()

                    updatePrice()
                    updateTotalPrice()
                }

                holder.minus.setOnClickListener {
                    if (holder.itemCount > 0) {
                        holder.itemCount--
                        holder.ItemAmount.text = holder.itemCount.toString()

                        menuHashMap["itemCount"] = holder.itemCount.toString()

                        updatePrice()
                        updateTotalPrice()
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return cartList.size
        }
    }

    fun updatePrice() {
        var itemTotal = BigDecimal("0.00")

        for (i in 0 until cartList.size) {
            val menuHashMap = cartList[i]
            val price = BigDecimal(menuHashMap["price"] ?: "0")
            val itemCount = BigDecimal(menuHashMap["itemCount"] ?: "0")
            val itemSubtotal = price.multiply(itemCount)
            itemTotal = itemTotal.add(itemSubtotal)
        }

        item_total.text = itemTotal.toString()
    }

    fun updateTotalPrice(){

        var itemTotal = BigDecimal("0.00")

        for (i in 0 until cartList.size) {
            val menuHashMap = cartList[i]
            val price = BigDecimal(menuHashMap["price"] ?: "0")
            val itemCount = BigDecimal(menuHashMap["itemCount"] ?: "0")
            val itemSubtotal = price.multiply(itemCount)
            itemTotal = itemTotal.add(itemSubtotal)
        }
        total_price.text = itemTotal.toString()



    }

}
