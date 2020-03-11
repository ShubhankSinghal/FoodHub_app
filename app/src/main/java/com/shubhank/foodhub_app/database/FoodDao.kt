package com.shubhank.foodhub_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {

    @Insert
    fun insertRestaurant(foodEntity: FoodEntity)

    @Delete
    fun deleteRestaurant(foodEntity: FoodEntity)

    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): List<FoodEntity>

    @Query("SELECT * FROM restaurants WHERE restaurant_id = :restaurantId")
    fun getRestaurantById(restaurantId: String): FoodEntity

    @Query("DELETE FROM restaurants")
    fun deleteAll()

}