package com.shubhank.foodhub_app.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.activity.OrderActivity
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity
import com.squareup.picasso.Picasso


class FavoriteRestaurantsAdapter(val context: Context, private val itemList: List<FoodEntity>) :
    RecyclerView.Adapter<FavoriteRestaurantsAdapter.HomeViewHolder>() {

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
            restaurant.restaurant_id,
            restaurant.restaurantName,
            restaurant.restaurantPrice,
            restaurant.restaurantRating,
            restaurant.restaurantImage
        )

        val checkFav = HomeRecyclerAdapter.DBAsyncTask(
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

            if (!HomeRecyclerAdapter.DBAsyncTask(
                    context,
                    resEntity,
                    1
                ).execute().get()
            ) {

                val async =
                    HomeRecyclerAdapter.DBAsyncTask(
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
                    HomeRecyclerAdapter.DBAsyncTask(
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
            intent.putExtra("id", restaurant.restaurant_id.toString())
            intent.putExtra("name", restaurant.restaurantName)
            intent.putExtra("rating", restaurant.restaurantRating)
            intent.putExtra("price", restaurant.restaurantPrice)
            intent.putExtra("image", restaurant.restaurantImage)
            context.startActivity(intent)
            (context as Activity).finish()
        }
        holder.imgRestaurantImage.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("id", restaurant.restaurant_id)
            intent.putExtra("name", restaurant.restaurantName)
            intent.putExtra("rating", restaurant.restaurantRating)
            intent.putExtra("price", restaurant.restaurantPrice)
            intent.putExtra("image", restaurant.restaurantImage)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }
}