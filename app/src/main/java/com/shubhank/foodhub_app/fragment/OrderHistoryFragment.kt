package com.shubhank.foodhub_app.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.adapter.OrderHistoryRecyclerAdapterParent
import com.shubhank.foodhub_app.model.History
import com.shubhank.foodhub_app.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_order.*
import org.json.JSONException


class OrderHistoryFragment : Fragment() {

    val historyInfoList = arrayListOf<History>()
    lateinit var recyclerOrderHistory: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryRecyclerAdapterParent
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory)
        layoutManager = LinearLayoutManager(activity)

        val sharedPreferences =
            this.activity!!.getSharedPreferences(
                getString(R.string.preference_file_name),
                Context.MODE_PRIVATE
            )
        val id = sharedPreferences.getString("res_id", "107").toString()

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/${id}"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {

                        progressLayout.visibility = View.GONE

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if (success) {
                            val data1 = data.getJSONArray("data")

                            for (i in 0 until data1.length()) {
                                val historyJsonObject = data1.getJSONObject((i))
                                val historyObject = History(
                                    historyJsonObject.getString("order_id"),
                                    historyJsonObject.getString("restaurant_name"),
                                    historyJsonObject.getString("total_cost"),
                                    historyJsonObject.getString("order_placed_at"),
                                    historyJsonObject.getJSONArray("food_items")
                                )
                                historyInfoList.add(historyObject)
                                recyclerAdapter =
                                    OrderHistoryRecyclerAdapterParent(
                                        activity as Context,
                                        historyInfoList
                                    )

                                recyclerOrderHistory.adapter = recyclerAdapter
                                recyclerOrderHistory.layoutManager = layoutManager

                                recyclerOrderHistory.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerOrderHistory.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error has Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred!!!",
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
        }
        return view
    }


}
