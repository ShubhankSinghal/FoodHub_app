package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.model.Food

class OrderHistoryRecyclerAdapterChild(
    val context: Context,
    private val itemList: ArrayList<Food>
) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapterChild.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderName: TextView = view.findViewById(R.id.cartOrderName)
        val orderPrice: TextView = view.findViewById(R.id.cartOrderPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = itemList[position]
        holder.orderName.text = order.orderName
        holder.orderPrice.text = "Rs.${order.orderPrice}"
    }
}