package com.example.ladidh

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class myOrders : AppCompatActivity() {

    private lateinit var ordersRecycleView: RecyclerView
    private lateinit var adapter: MyAdapter
    val ordersList: ArrayList<HashMap<String, String>> = ArrayList()
    var ordersHashMap: HashMap<String, String>? = null
    private lateinit var progressbar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        ordersList.clear()

        ordersRecycleView = findViewById(R.id.ordersRecycleView)
        progressbar = findViewById(R.id.progressbar)
        progressbar.visibility = View.VISIBLE


        adapter = MyAdapter()
        ordersRecycleView.adapter = adapter
        ordersRecycleView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    //=======recycle view Adapter
    private inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MenuViewHolder>() {

        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var Dname: TextView = itemView.findViewById(R.id.name)
            var Dlocation: TextView = itemView.findViewById(R.id.location)
            var Ditems: TextView = itemView.findViewById(R.id.items)
            var Dbill: TextView = itemView.findViewById(R.id.bill)
            var Dtime: TextView = itemView.findViewById(R.id.time)
            var Dstatus: TextView = itemView.findViewById(R.id.status)
            var cancelBtn: TextView = itemView.findViewById(R.id.cancelBtn)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.my_orders, parent, false)
            return MenuViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            if (position < ordersList.size) {

                val ordersHashMap = ordersList[position]
                val id = ordersHashMap["id"]
                val name = ordersHashMap["name"]
                val mobile = ordersHashMap["mobile"]
                val location = ordersHashMap["location"]
                val items = ordersHashMap["items"]
                val bill = ordersHashMap["bill"]
                val status = ordersHashMap["status"]
                val date = ordersHashMap["date"]

                holder.Dname.text = name
                holder.Dlocation.text = location
                holder.Ditems.text = items
                holder.Dbill.text = bill
                holder.Dstatus.text = status
                holder.Dtime.text = date

                holder.cancelBtn.setOnClickListener() {
                    val url = "https://www.digitalrangersbd.com/app/ladidh/cancelOrder.php?i="+id

                    val stringRequest = StringRequest(
                        Request.Method.GET, url,
                        Response.Listener<String> { response ->
                        },
                        Response.ErrorListener { error ->
                        })

                    val requestQueue = Volley.newRequestQueue(this@myOrders)
                    requestQueue.add(stringRequest)

                    AlertDialog.Builder(this@myOrders)
                        .setTitle("Cancelled!")
                        .setMessage("Order has been cancelled.")
                        .setNegativeButton("OK") { _, _ -> }
                        .show()

                    loadData()
                }

            }
        }

        override fun getItemCount(): Int {
            return ordersList.size
        }
    }

    //=======load orders
    private fun loadData() {
        ordersList.clear()

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val uMobile = sharedPreferences.getString("userMobile", "")

        val url = "https://digitalrangersbd.com/app/ladidh/loadOrders.php?m=$uMobile"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                for (x in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(x)
                        val id = jsonObject.getString("id")
                        val name = jsonObject.getString("name")
                        val mobile = jsonObject.getString("mobile")
                        val location = jsonObject.getString("location")
                        val items = jsonObject.getString("items")
                        val bill = jsonObject.getString("bill")
                        val status = jsonObject.getString("status")
                        val date = jsonObject.getString("date")

                        ordersHashMap = HashMap()
                        ordersHashMap!!["id"] = id
                        ordersHashMap!!["name"] = name
                        ordersHashMap!!["mobile"] = mobile
                        ordersHashMap!!["location"] = location
                        ordersHashMap!!["items"] = items
                        ordersHashMap!!["bill"] = bill
                        ordersHashMap!!["status"] = status
                        ordersHashMap!!["date"] = date

                        ordersList.add(ordersHashMap!!)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                progressbar.visibility = View.GONE
                if (ordersList.isNotEmpty()) {
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.notifyDataSetChanged()
                    AlertDialog.Builder(this)
                        .setTitle("No Orders Found")
                        .setMessage("You haven't placed any orders yet.")
                        .setNegativeButton("OK") { dialog, which ->
                            val intent = Intent(this, profile::class.java)
                            startActivity(intent)
                        }
                        .show()
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
