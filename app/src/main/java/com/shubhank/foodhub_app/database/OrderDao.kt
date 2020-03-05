package com.shubhank.foodhub_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shubhank.foodhub_app.model.Food
import java.sql.RowId


@Dao
interface OrderDao {

    @Insert
    fun insertOrder(orderEntity: OrderEntity)

    @Delete
    fun deleteOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM Orders")
    fun getAllOrders(): List<OrderEntity>

    @Query("SELECT COUNT(*) FROM Orders")
    fun getCountOrders() : Int

    @Query("SELECT * FROM Orders WHERE order_id = :orderId")
    fun getOrderById(orderId: String): OrderEntity

    @Query("DELETE FROM Orders")
    fun deleteAll()

}