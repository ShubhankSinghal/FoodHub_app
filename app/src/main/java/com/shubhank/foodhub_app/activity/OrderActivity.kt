package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
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

    val foodInfoList = arrayListOf<Food>()

    lateinit var toolbar: Toolbar

    var restaurantId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        recyclerOrder = findViewById(R.id.recyclerOrder)
        orderBack = findViewById(R.id.orderBack)
        layoutManager = LinearLayoutManager(this@OrderActivity)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        orderBack.setOnClickListener {
            finish()
        }

        if (intent != null) {
            restaurantId = intent.getStringExtra("id")
        } else {
            finish()
            Toast.makeText(
                this@OrderActivity,
                "Some unexpected error occured!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (restaurantId == "100") {
            finish()
            Toast.makeText(
                this@OrderActivity,
                "Some unexpected error occured!",
                Toast.LENGTH_SHORT
            ).show()
        }


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
/*
                              val foodEntity = FoodEntity(
                                    txtRestaurantId?.toInt() as Int,
                                    txtRestaurantName.text.toString(),
                                    txtRestaurantPrice.text.toString()
                                )

                                val checkFav = DBAsyncTask(applicationContext, foodEntity, 1).execute()
                                val isFav = checkFav.get()

                                if (isFav) {
                                    btnAddToFavorites.text = "Remove From Favorites"
                                    val favColor = ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorFavorites
                                    )
                                    btnAddToFavorites.setBackgroundColor(favColor)
                                } else {
                                    btnAddToFavorites.text = "Add to Favorites"
                                    val noFavColor =
                                        ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                    btnAddToFavorites.setBackgroundColor(noFavColor)
                                }

                                btnAddToFavorites.setOnClickListener {

                                    if (!DBAsyncTask(
                                            applicationContext,
                                            foodEntity,
                                            1
                                        ).execute().get()
                                    ) {

                                        val async =
                                            DBAsyncTask(applicationContext, foodEntity, 2).execute()
                                        val result = async.get()

                                        if (result) {
                                            Toast.makeText(
                                                this@OrderActivity,
                                                "Book added to Favorites",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            btnAddToFavorites.text = "Remove from Favorites"
                                            val favColor = ContextCompat.getColor(
                                                applicationContext,
                                                R.color.colorFavorites
                                            )
                                            btnAddToFavorites.setBackgroundColor(favColor)
                                        } else {
                                            Toast.makeText(
                                                this@OrderActivity,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {

                                        val async =
                                            DBAsyncTask(applicationContext, foodEntity, 3).execute()
                                        val result = async.get()

                                        if (result) {
                                            Toast.makeText(
                                                this@OrderActivity,
                                                "Book removed from Favorites",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            btnAddToFavorites.text = "Add to Favorites"
                                            val favColor = ContextCompat.getColor(
                                                applicationContext,
                                                R.color.colorPrimary
                                            )
                                            btnAddToFavorites.setBackgroundColor(favColor)
                                        } else {
                                            Toast.makeText(
                                                this@OrderActivity,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                                }*/

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
            dialog.setPositiveButton("Open Settings") { text, listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit") { text, listener ->
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

        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "books-db").build()

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

}
