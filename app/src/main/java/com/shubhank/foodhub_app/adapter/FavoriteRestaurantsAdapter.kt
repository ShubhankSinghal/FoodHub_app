package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.activity.MainActivity
import com.shubhank.foodhub_app.activity.OrderActivity
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.Restaurant
import com.squareup.picasso.Picasso

class FavoriteRestaurantsAdapter(val context: Context, val itemList: List<FoodEntity>) :
    RecyclerView.Adapter<FavoriteRestaurantsAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val textRestaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val textRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val l3Content: RelativeLayout = view.findViewById(R.id.l3Content)
        val img: ImageView = view.findViewById(R.id.imgRestaurantImage)

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

        holder.l3Content.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("id", restaurant.restaurant_id)
            context.startActivity(intent)
        }
        holder.img.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("id", restaurant.restaurant_id)
            context.startActivity(intent)
        }

    }
}