package com.shubhank.foodhub_app.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity
import com.shubhank.foodhub_app.database.OrderDatabase
import com.shubhank.foodhub_app.database.OrderEntity
import com.shubhank.foodhub_app.model.Food


class OrderRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<Food>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {


    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textFoodIndex: TextView = view.findViewById(R.id.orderIndex)
        val textFoodName: TextView = view.findViewById(R.id.orderRestaurantOrder)
        val textFoodPrice: TextView = view.findViewById(R.id.orderPrice)
        val orderButton: Button = view.findViewById(R.id.orderButton)
        val orderButton1: Button = view.findViewById(R.id.orderButton1)
    }

    interface OnItemClickListener {
        fun onAddItemClick(foodItem: Food)
        fun onRemoveItemClick(foodItem: Food)
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
        holder.textFoodIndex.text = (position + 1).toString()
        holder.textFoodName.text = food.orderName
        holder.textFoodPrice.text = food.orderPrice

        val orderEntity = OrderEntity(
            food.orderId?.toInt() as Int,
            food.orderName,
            food.orderPrice
        )

        if (DBAsyncTask(context, orderEntity, 3).execute().get()) {
            holder.orderButton.visibility = View.GONE
            holder.orderButton1.visibility = View.VISIBLE
        } else {
            holder.orderButton1.visibility = View.GONE
            holder.orderButton.visibility = View.VISIBLE
        }

        holder.orderButton.setOnClickListener {

            holder.orderButton.visibility = View.GONE
            holder.orderButton1.visibility = View.VISIBLE
            listener.onAddItemClick(food)
            val async = DBAsyncTask(context, orderEntity, 1).execute().get()
            CartRecyclerAdapter.price += food.orderPrice.toInt()
            if (async) {
                Toast.makeText(
                    context,
                    "Added to Cart",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Some Error Occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        holder.orderButton1.setOnClickListener {

            holder.orderButton1.visibility = View.GONE
            holder.orderButton.visibility = View.VISIBLE
            listener.onRemoveItemClick(food)
            val async = DBAsyncTask(context, orderEntity, 2).execute().get()
            CartRecyclerAdapter.price -= food.orderPrice.toInt()
            if (async) {
                Toast.makeText(
                    context,
                    "Removed from Cart",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Some Error Occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}


class DBAsyncTask(val context: Context, val orderEntity: OrderEntity, val mode: Int) :
    AsyncTask<Void, Void, Boolean>() {

    val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {

        when (mode) {


            1 -> {
                //Save the order into DB
                db.orderDao().insertOrder(orderEntity)
                db.close()
                return true
            }

            2 -> {
                //Remove the order
                db.orderDao().deleteOrder(orderEntity)
                db.close()
                return true
            }

            3 -> {
                val order: OrderEntity? =
                    db.orderDao().getOrderById(orderEntity.order_id.toString())
                db.close()
                return order != null
            }
        }
        return false
    }

}
