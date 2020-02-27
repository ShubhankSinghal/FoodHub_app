package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R

class OrderRecyclerAdapter(val context:Context, val itemList: ArrayList<String>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtRestaurantName)
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
        val text = itemList[position]
        holder.textView.text = text
    }
}