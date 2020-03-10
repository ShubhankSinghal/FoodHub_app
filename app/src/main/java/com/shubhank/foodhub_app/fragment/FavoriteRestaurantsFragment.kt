package com.shubhank.foodhub_app.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.FavoriteRestaurantsAdapter
import com.shubhank.foodhub_app.database.FoodDatabase
import com.shubhank.foodhub_app.database.FoodEntity


class FavoriteRestaurantsFragment : Fragment() {

    private lateinit var recyclerFavorite: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: FavoriteRestaurantsAdapter
    private var dbRestaurantList = listOf<FoodEntity>()
    private lateinit var noRestaurant: RelativeLayout
    private lateinit var noRestaurantText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite_restaurants, container, false)

        recyclerFavorite = view.findViewById(R.id.recyclerFavorites)
        layoutManager = LinearLayoutManager(activity)
        noRestaurant = view.findViewById(R.id.noRestaurant)
        noRestaurantText = view.findViewById(R.id.noRestaurantText)
        noRestaurant.visibility = View.VISIBLE

        dbRestaurantList = RetrieveFavorite(activity as Context).execute().get()

        if (activity != null) {
            if (dbRestaurantList.isNotEmpty()) {
                noRestaurant.visibility = View.GONE
                recyclerAdapter = FavoriteRestaurantsAdapter(activity as Context, dbRestaurantList)
                recyclerFavorite.adapter = recyclerAdapter
                recyclerFavorite.layoutManager = layoutManager
            } else {
                noRestaurant.visibility = View.VISIBLE
            }
        }

        return view
    }

    class RetrieveFavorite(val context: Context) : AsyncTask<Void, Void, List<FoodEntity>>() {

        override fun doInBackground(vararg params: Void?): List<FoodEntity> {
            val db =
                Room.databaseBuilder(context, FoodDatabase::class.java, "restaurants-db").build()

            return db.foodDao().getAllRestaurants()
        }
    }
}
