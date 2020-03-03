package com.shubhank.foodhub_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Orders")

data class OrderEntity(

    @PrimaryKey val order_id: Int,
    @ColumnInfo(name = "order_name") val orderName: String,
    @ColumnInfo(name = "order_price") val orderPrice: String

)