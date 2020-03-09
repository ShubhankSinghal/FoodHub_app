package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.History

class OrderHistoryRecyclerAdapterParent(val context: Context, val itemList: ArrayList<History>) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapterParent.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resName: TextView = view.findViewById(R.id.orderHistoryRestaurantName)
        val orderDate: TextView = view.findViewById(R.id.orderHistoryDate)
        val recyclerOrderHistory: RecyclerView =
            view.findViewById(R.id.recyclerOrderHistorySingleRow)
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
        val history = itemList[position]
        holder.resName.text = history.restaurantName
        val date = history.orderPlacedAt
        holder.orderDate.text =
            "${date.substring(0, 2)}/${date.substring(3, 5)}/20${date.substring(6, 8)}"
        setUpRecycler(holder.recyclerOrderHistory, history)
    }

    private fun setUpRecycler(recyclerOrderHistory: RecyclerView, history: History) {
        val orderList = ArrayList<Food>()
        for (i in 0 until history.foodItems.length()) {
            val foodJson = history.foodItems.getJSONObject(i)
            orderList.add(
                Food(
                    foodJson.getString("food_item_id"),
                    foodJson.getString("name"),
                    foodJson.getString("cost")
                )
            )
        }
        val cartItemAdapter = OrderHistoryRecyclerAdapterChild(context, orderList)
        val LayoutManager = LinearLayoutManager(context)
        recyclerOrderHistory.layoutManager = LayoutManager
        recyclerOrderHistory.adapter = cartItemAdapter
    }

}