package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.database.OrderDatabase
import com.shubhank.foodhub_app.database.OrderEntity
import com.shubhank.foodhub_app.model.Food

class OrderRecyclerAdapter(val context: Context, val itemList: ArrayList<Food>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textFoodIndex: TextView = view.findViewById(R.id.orderIndex)
        val textFoodName: TextView = view.findViewById(R.id.orderRestaurantOrder)
        val textFoodPrice: TextView = view.findViewById(R.id.orderPrice)
        val orderButton: Button = view.findViewById(R.id.orderButton)
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
        holder.textFoodIndex.text = food.orderIndex
        holder.textFoodName.text = food.orderName
        holder.textFoodPrice.text = food.orderPrice

        holder.orderButton.setOnClickListener {



        }

    }
    class DBAsyncTask(val context: Context, val orderEntity: OrderEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    //Check DB if the order is present or not
                    val order: OrderEntity? =
                        db.orderDao().getOrderById(orderEntity.order_id.toString())
                    db.close()
                    return order != null
                }

                2 -> {
                    //Save the order into DB
                    db.orderDao().insertOrder(orderEntity)
                    db.close()
                    return true
                }

                3 -> {
                    //Remove the order
                    db.orderDao().deleteOrder(orderEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

}