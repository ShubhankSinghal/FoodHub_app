package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.CartRecyclerAdapter
import com.shubhank.foodhub_app.adapter.OrderRecyclerAdapter
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.database.OrderDatabase
import com.shubhank.foodhub_app.database.OrderEntity
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_cart.*

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
    lateinit var cartRestaurantName : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        dbOrderList = RetrieveOrders(this.applicationContext).execute().get()

        cartButton = findViewById(R.id.cartButton)
        cartBack = findViewById(R.id.cartBack)

        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this@CartActivity)

        recyclerAdapter = CartRecyclerAdapter(this@CartActivity, dbOrderList)
        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        cartBack.setOnClickListener {
            onBackPressed()
        }

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
        // recyclerAdapter = CartRecyclerAdapter(this@CartActivity, foodInfoList)

        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager

        cartButton.setOnClickListener {
            val queue = Volley.newRequestQueue(this@CartActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"

            if (ConnectionManager().checkConnectivity(this@CartActivity)) {

                /*val jsonRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, , Response.Listener {

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {

                                val intent =
                                    Intent(this@CartActivity, completionActivity::class.java)
                                startActivity(intent)

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
            }*/
            }
        }
    }

    class RetrieveOrders(val context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {

        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

            return db.orderDao().getAllOrders()
        }

    }
}

