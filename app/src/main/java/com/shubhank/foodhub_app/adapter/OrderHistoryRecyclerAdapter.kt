package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R

class OrderHistoryRecyclerAdapter(val context:Context, val itemList: ArrayList<String>) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtRestaurantName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_order_history_single_row, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val text = itemList[position]
        holder.textView.text = text
    }
}