package com.example.ladidh

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.text.DecimalFormat





class cart : AppCompatActivity() {

    private lateinit var cartRecyleView: RecyclerView
    var cartList: ArrayList<HashMap<String, String>> = ArrayList()
    var menuHashMap: HashMap<String, String>? = null
    var couponList: ArrayList<HashMap<String, String>> = ArrayList()
    var couponHashMap: HashMap<String, String>? = null
    private lateinit var item_total: TextView
    private lateinit var total_price: TextView
    private lateinit var applyBtn: TextView
    private lateinit var checkoutBtn: TextView
    private lateinit var coupon_code: EditText
    var couponPrice: Double = 0.00
    var itemTotal: Double = 0.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        item_total = findViewById(R.id.itemTotal)
        applyBtn = findViewById(R.id.apply)
        checkoutBtn = findViewById(R.id.checkout)
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

        applyBtn.setOnClickListener() {

            val couponCode = coupon_code.text.toString()
            couponList.clear()

            val url = "https://www.digitalrangersbd.com/app/ladidh/coupon.php?c="+couponCode

            val jsonArrayRequest = JsonArrayRequest(com.android.volley.Request.Method.GET, url, null, Response.Listener
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
                            couponHashMap!!["percent"] = percent
                            couponHashMap!!["max"] = max
                            couponHashMap!!["state"] = state

                            couponList.add(couponHashMap!!)

                            var discount:Double = 0.00

                            if ( couponList.isEmpty() )
                            {
                                AlertDialog.Builder(this)
                                    .setTitle("Sorry! Invalid Coupon")
                                    .setMessage("Please put a valid coupon")
                                    .setNegativeButton("OK") { dialog, which -> }
                                    .show()

                            }else{
                                discount = percent.toDouble() * couponPrice / 100

                                if (discount <= max.toDouble()) {
                                    couponPrice = couponPrice - discount
                                    val decimalFormat = DecimalFormat("#.##") // Adjust the pattern based on your requirements
                                    val formattedItemTotal = decimalFormat.format(couponPrice)
                                    total_price.text = formattedItemTotal
                                    couponList.clear()
                                    applyBtn.visibility = View.GONE
                                } else {
                                    couponPrice = couponPrice - max.toDouble()
                                    val decimalFormat = DecimalFormat("#.##") // Adjust the pattern based on your requirements
                                    val formattedItemTotal = decimalFormat.format(couponPrice)
                                    total_price.text = formattedItemTotal
                                    applyBtn.visibility = View.GONE
                                    couponList.clear()
                                }
                            }

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


        checkoutBtn.setOnClickListener() {

            if (couponPrice != 0.0) {
                val intent = Intent(this, checkOut::class.java)

                val selectedItemsList = ArrayList<HashMap<String, String>>()

                for (i in 0 until cartList.size) {
                    val menuHashMap = cartList[i]
                    val itemName = menuHashMap["name"] ?: ""
                    val itemCount = menuHashMap["itemCount"] ?: "0"

                    val selectedItem = HashMap<String, String>()
                    selectedItem["itemName"] = itemName
                    selectedItem["itemCount"] = itemCount

                    selectedItemsList.add(selectedItem)
                }

                intent.putExtra("selectedItems", selectedItemsList)
                intent.putExtra("couponPrice", couponPrice)

                startActivity(intent)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Empty Cart")
                    .setMessage("Please select an item")
                    .setNegativeButton("OK") { dialog, which -> }
                    .show()
            }
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
                }

                holder.minus.setOnClickListener {
                    if (holder.itemCount > 0) {
                        holder.itemCount--
                        holder.ItemAmount.text = holder.itemCount.toString()

                        menuHashMap["itemCount"] = holder.itemCount.toString()

                        updatePrice()
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return cartList.size
        }
    }


    //================price update
    fun updatePrice() {

        var itemTotal:Double = 0.00


        for (i in 0 until cartList.size) {
            val menuHashMap = cartList[i]
            val price = menuHashMap["price"] ?: "0"
            val itemCount = menuHashMap["itemCount"] ?: "0"
            val itemSubtotal:Double = price.toDouble()*itemCount.toDouble()
            itemTotal = itemTotal+itemSubtotal
        }

        val decimalFormat = DecimalFormat("#.##") // Adjust the pattern based on your requirements
        val formattedItemTotal = decimalFormat.format(itemTotal)
        item_total.text = formattedItemTotal
        total_price.text = formattedItemTotal
        couponPrice = itemTotal
    }


}
