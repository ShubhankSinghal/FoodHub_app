package com.shubhank.foodhub_app.model

import org.json.JSONArray

data class History(

    val orderId: String,
    val restaurantName: String,
    val totalCost: String,
    val orderPlacedAt: String,
    val foodItems: JSONArray

)