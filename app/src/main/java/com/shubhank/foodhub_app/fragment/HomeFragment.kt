package com.shubhank.foodhub_app.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.HomeRecyclerAdapter
import com.shubhank.foodhub_app.model.Restaurant
import com.shubhank.foodhub_app.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    val restaurantInfoList = arrayListOf<Restaurant>()
    lateinit var recyclerAdapter: HomeRecyclerAdapter

    private var ratingComparator = Comparator<Restaurant> { res1, res2 ->

        if (res1.restaurantRating.compareTo(res2.restaurantRating, true) == 0) {
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantRating.compareTo(res2.restaurantRating, true)
        }

    }

    private var ratingComparator1 = Comparator<Restaurant> { res1, res2 ->

        if (res1.restaurantPrice.compareTo(res2.restaurantPrice, true) == 0) {
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantPrice.compareTo(res2.restaurantPrice, true)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    try {

                        progressLayout.visibility = View.GONE

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if (success) {
                            val data1 = data.getJSONArray("data")

                            for (i in 0 until data1.length()) {
                                val restaurantJsonObject = data1.getJSONObject((i))
                                val restaurantObject = Restaurant(
                                    restaurantJsonObject.getString("id"),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    restaurantJsonObject.getString("cost_for_one"),
                                    restaurantJsonObject.getString("image_url")
                                )
                                restaurantInfoList.add(restaurantObject)
                                recyclerAdapter =
                                    HomeRecyclerAdapter(activity as Context, restaurantInfoList)

                                recyclerHome.adapter = recyclerAdapter
                                recyclerHome.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Unexpected Error has Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "JSON error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {

                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6f5311403e6661"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { _, _ ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity((activity as Activity))
            }
            dialog.setCancelable(false)
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_sort_rating) {
            Collections.sort(restaurantInfoList, ratingComparator)
            restaurantInfoList.reverse()
        }

        if (id == R.id.action_sort_cost) {
            Collections.sort(restaurantInfoList, ratingComparator1)
            restaurantInfoList
        }

        if (id == R.id.action_sort_cost_desc) {
            Collections.sort(restaurantInfoList, ratingComparator1)
            restaurantInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}