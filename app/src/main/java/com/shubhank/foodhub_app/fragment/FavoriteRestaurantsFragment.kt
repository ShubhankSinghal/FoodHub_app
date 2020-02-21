package com.shubhank.foodhub_app.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shubhank.foodhub_app.R

/**
 * A simple [Fragment] subclass.
 */
class FavoriteRestaurantsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_restaurants, container, false)
    }


}
