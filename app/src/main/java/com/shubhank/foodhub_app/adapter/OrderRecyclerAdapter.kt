package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.model.Food

class OrderRecyclerAdapter(val context:Context, val itemList: ArrayList<Food>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

    var i : Int  = 1

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textFoodIndex: TextView = view.findViewById(R.id.orderIndex)
        val textFoodName: TextView = view.findViewById(R.id.orderRestaurantOrder)
        val textFoodPrice: TextView = view.findViewById(R.id.orderPrice)
        val linearLayout : LinearLayout = view.findViewById(R.id.l2Content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_order_single_row, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val food = itemList[position]
        holder.textFoodIndex.text = i.toString()
        holder.textFoodName.text = food.orderName
        holder.textFoodPrice.text = food.orderPrice
        i++
    }
}