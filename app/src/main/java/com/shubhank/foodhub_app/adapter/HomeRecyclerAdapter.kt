package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.activity.MainActivity
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context:Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val textRestaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val textRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)

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
        Picasso.get().load(restaurant.restaurantImage)
            .into(holder.imgRestaurantImage)

        }
}