package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.database.OrderEntity

class CartRecyclerAdapter(val context: Context, val orderList: List<OrderEntity>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    companion object {
        var price = 0
    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val cartOrderName: TextView = view.findViewById(R.id.cartOrderName)
        val cartOrderPrice: TextView = view.findViewById(R.id.cartOrderPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val order = orderList[position]
        holder.cartOrderName.text = order.orderName
        holder.cartOrderPrice.text = "Rs.${order.orderPrice}"

    }

}
