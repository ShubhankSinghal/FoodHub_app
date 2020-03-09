package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonArray
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.CartRecyclerAdapter
import com.shubhank.foodhub_app.adapter.OrderRecyclerAdapter
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.database.OrderDatabase
import com.shubhank.foodhub_app.database.OrderEntity
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart: RecyclerView
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var cartBack: ImageView
    lateinit var cartButton: Button
    lateinit var toolbar: Toolbar
    var restaurantId: String? = "100"
    lateinit var restaurantName: String
    var dbOrderList = listOf<OrderEntity>()
    lateinit var cartRestaurantName: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        cartButton = findViewById(R.id.cartButton)
        cartBack = findViewById(R.id.cartBack)
        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this@CartActivity)

        dbOrderList = RetrieveOrders(this@CartActivity).execute().get()
        recyclerAdapter = CartRecyclerAdapter(this@CartActivity, dbOrderList)
        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager
        cartButton.text = "Place Order(Total: Rs.${CartRecyclerAdapter.price.toString()})"

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        cartBack.setOnClickListener {
            onBackPressed()
        }

        val userId = sharedPreferences.getString("res_id", "01").toString()
        val totalCost = CartRecyclerAdapter.price
        if (intent != null) {
            restaurantId = intent.getStringExtra("id")
            restaurantName = intent.getStringExtra("name")

        } else {
            finish()
            val intent = Intent(this@CartActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@CartActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        cartRestaurantName = findViewById(R.id.cartRestaurantName)
        cartRestaurantName.text = restaurantName

        if (restaurantId == "100") {
            finish()
            val intent = Intent(this@CartActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@CartActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        cartButton.setOnClickListener {

            val queue = Volley.newRequestQueue(this@CartActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("user_id", userId)
            jsonParams.put("restaurant_id", restaurantId.toString())
            jsonParams.put("total_cost", totalCost.toString())
            var arrayOrder = JSONArray()
            for (element in dbOrderList) {
                var jsonOrder = JSONObject()
                jsonOrder.put("food_item_id", element.order_id.toString())
                arrayOrder.put(jsonOrder)
            }
            jsonParams.put("food", arrayOrder)

            if (ConnectionManager().checkConnectivity(this@CartActivity)) {

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val intent =
                                        Intent(this@CartActivity, completionActivity::class.java)
                                    startActivity(intent)
                                    DeleteOrders(this@CartActivity).execute().get()
                                    CartRecyclerAdapter.price = 0
                                }


                            } catch (e: Exception) {

                                Toast.makeText(
                                    this@CartActivity,
                                    "Some Error has Occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }, Response.ErrorListener {

                            Toast.makeText(
                                this@CartActivity,
                                "Volley error $it!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "6f5311403e6661"
                            return headers
                        }
                    }
                queue.add(jsonRequest)

            } else {
                val dialog = AlertDialog.Builder(this@CartActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { _, _ ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()

                }
                dialog.setNegativeButton("Exit") { _, _ ->
                    ActivityCompat.finishAffinity((this@CartActivity))
                }
                dialog.create()
                dialog.show()
            }
        }
    }


    override fun onBackPressed() {

        finish()

    }

    class RetrieveOrders(val context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {

        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

            return db.orderDao().getAllOrders()
        }

    }

    class DeleteOrders(val context: Context) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

            db.orderDao().deleteAll()
            db.close()
            return true
        }

    }
}

