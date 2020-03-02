package com.shubhank.foodhub_app.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.FavoriteRestaurantsAdapter
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity


class FavoriteRestaurantsFragment : Fragment() {

    lateinit var recyclerFavorite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavoriteRestaurantsAdapter
    var dbRestaurantList = listOf<FoodEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_favorite_restaurants, container, false)

        recyclerFavorite = view.findViewById(R.id.recyclerFavorites)
        layoutManager = GridLayoutManager(activity as Context, 2)

        dbRestaurantList = RetrieveFavorite(activity as Context).execute().get()

        if (activity != null) {
            recyclerAdapter = FavoriteRestaurantsAdapter(activity as Context, dbRestaurantList)
            recyclerFavorite.adapter = recyclerAdapter
            recyclerFavorite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavorite(val context: Context) : AsyncTask<Void, Void, List<FoodEntity>>() {

        override fun doInBackground(vararg params: Void?): List<FoodEntity> {
            val db = Room.databaseBuilder(context, FoodDatabase::class.java, "books-db").build()

            return db.FoodDao().getAllRestaurants()
        }

    }

}
