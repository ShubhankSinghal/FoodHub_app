package com.shubhank.foodhub_app.adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.activity.MainActivity
import com.shubhank.foodhub_app.activity.OrderActivity
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.fragment.HomeFragment
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val textRestaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val textRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val l3Content: RelativeLayout = view.findViewById(R.id.l3Content)
        val imgFavorite: ImageView = view.findViewById(R.id.imgFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.textRestaurantName.text = restaurant.restaurantName
        holder.textRestaurantPrice.text = restaurant.restaurantPrice
        holder.textRestaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.logo)
            .into(holder.imgRestaurantImage)

            val resEntity = FoodEntity(
                restaurant.restaurantId?.toInt() as Int,
                restaurant.restaurantName,
                restaurant.restaurantRating,
                restaurant.restaurantPrice,
                restaurant.restaurantImage
            )

            val checkFav = DBAsyncTask(
                context,
                resEntity,
                1
            ).execute()
            val isFav = checkFav.get()

            if (isFav) {
                holder.imgFavorite.setImageResource(R.drawable.ic_rating2)
            } else {
                holder.imgFavorite.setImageResource(R.drawable.ic_rating1)
            }

            holder.imgFavorite.setOnClickListener {

                if (!DBAsyncTask(
                        context,
                        resEntity,
                        1
                    ).execute().get()
                ) {

                    val async =
                        DBAsyncTask(
                            context,
                            resEntity,
                            2
                        ).execute()
                    val result = async.get()

                    if (result) {
                        Toast.makeText(
                            context,
                            "Restaurant added to Favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                        holder.imgFavorite.setImageResource(R.drawable.ic_rating2)
                    } else {
                        Toast.makeText(
                            context,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    val async =
                        DBAsyncTask(
                            context,
                            resEntity,
                            3
                        ).execute()
                    val result = async.get()

                    if (result) {
                        Toast.makeText(
                            context,
                            "Restaurant removed from Favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                        holder.imgFavorite.setImageResource(R.drawable.ic_rating1)
                    } else {
                        Toast.makeText(
                            context,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

        holder.l3Content.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("id", restaurant.restaurantId)
            context.startActivity(intent)
        }
        holder.imgRestaurantImage.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("id", restaurant.restaurantId)
            context.startActivity(intent)
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
}
