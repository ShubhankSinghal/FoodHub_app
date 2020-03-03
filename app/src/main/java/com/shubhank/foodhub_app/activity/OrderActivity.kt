package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.HomeRecyclerAdapter
import com.shubhank.foodhub_app.adapter.OrderRecyclerAdapter
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.Restaurant
import com.shubhank.foodhub_app.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_single_row.*
import org.json.JSONObject

class OrderActivity : AppCompatActivity() {

    lateinit var recyclerOrder: RecyclerView
    lateinit var recyclerAdapter: OrderRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var orderBack: ImageView
    lateinit var imgOrderFavorite: ImageView
    val foodInfoList = arrayListOf<Food>()
    lateinit var toolbar: Toolbar
    var restaurantId: String? = "100"
    lateinit var restaurantName: String
    lateinit var restaurantRating: String
    lateinit var restaurantPrice: String
    lateinit var restaurantImage: String
    lateinit var orderToolbarName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        orderToolbarName = findViewById(R.id.orderToolbarName)
        imgOrderFavorite = findViewById(R.id.imgOrderFavorite)
        recyclerOrder = findViewById(R.id.recyclerOrder)
        orderBack = findViewById(R.id.orderBack)
        layoutManager = LinearLayoutManager(this@OrderActivity)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        orderBack.setOnClickListener {
            finish()
            val intent = Intent(this@OrderActivity, MainActivity::class.java)
            startActivity(intent)
        }

        if (intent != null) {
            restaurantId = intent.getStringExtra("id")
            restaurantName = intent.getStringExtra("name")
            restaurantRating = intent.getStringExtra("rating")
            restaurantPrice = intent.getStringExtra("price")
            restaurantImage = intent.getStringExtra("image")
        } else {
            finish()
            val intent = Intent(this@OrderActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@OrderActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        orderToolbarName.text = restaurantName

        if (restaurantId == "100") {
            finish()
            val intent = Intent(this@OrderActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@OrderActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        var count = 1
        val queue = Volley.newRequestQueue(this@OrderActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        if (ConnectionManager().checkConnectivity(this@OrderActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val data1 = data.getJSONArray("data")

                            for (i in 0 until data1.length()) {
                                val foodJsonObject = data1.getJSONObject((i))
                                val foodObject = Food(
                                    (count++).toString(),
                                    foodJsonObject.getString("id"),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("cost_for_one")
                                )
                                foodInfoList.add(foodObject)
                                recyclerAdapter =
                                    OrderRecyclerAdapter(this@OrderActivity, foodInfoList)

                                recyclerOrder.adapter = recyclerAdapter

                                recyclerOrder.layoutManager = layoutManager

                            }

                            val resEntity = FoodEntity(
                                restaurantId?.toInt() as Int,
                                restaurantName,
                                restaurantRating,
                                restaurantPrice,
                                restaurantImage
                            )

                            val checkFav = DBAsyncTask(
                                applicationContext,
                                resEntity,
                                1
                            ).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                imgOrderFavorite.setImageResource(R.drawable.ic_rating2)
                            } else {
                                imgOrderFavorite.setImageResource(R.drawable.ic_rating1)
                            }

                            imgOrderFavorite.setOnClickListener {

                                if (!DBAsyncTask(
                                        applicationContext,
                                        resEntity,
                                        1
                                    ).execute().get()
                                ) {

                                    val async =
                                        DBAsyncTask(
                                            applicationContext,
                                            resEntity,
                                            2
                                        ).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@OrderActivity,
                                            "Restaurant added to Favorites",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        imgOrderFavorite.setImageResource(R.drawable.ic_rating2)
                                    } else {
                                        Toast.makeText(
                                            this@OrderActivity,
                                            "Some Error Occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {

                                    val async =
                                        DBAsyncTask(
                                            applicationContext,
                                            resEntity,
                                            3
                                        ).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@OrderActivity,
                                            "Restaurant removed from Favorites",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        imgOrderFavorite.setImageResource(R.drawable.ic_rating1)
                                    } else {
                                        Toast.makeText(
                                            this@OrderActivity,
                                            "Some Error Occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@OrderActivity,
                                "Some Error has Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(
                            this@OrderActivity,
                            "Some Error has Occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }, Response.ErrorListener {

                    Toast.makeText(
                        this@OrderActivity,
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
            val dialog = AlertDialog.Builder(this@OrderActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { _ , _ ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit") { _ , _ ->
                ActivityCompat.finishAffinity((this@OrderActivity))
            }
            dialog.create()
            dialog.show()
        }
    }


    class DBAsyncTask(val context: Context, val foodEntity: FoodEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favorite or not
        Mode 2 -> Save the book into DB as favorite
        Mode 3 -> Remove the favorite book
         */

        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "restaurants-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    //Check DB if the book is favorite or not
                    val book: FoodEntity? =
                        db.FoodDao().getRestaurantById(foodEntity.restaurant_id.toString())
                    db.close()
                    return book != null
                }

                2 -> {
                    //Save the book into DB as favorite
                    db.FoodDao().insertRestaurant(foodEntity)
                    db.close()
                    return true
                }

                3 -> {
                    //Remove the favorite book
                    db.FoodDao().deleteRestaurant(foodEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@OrderActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
