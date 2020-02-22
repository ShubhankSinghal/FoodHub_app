package com.shubhank.foodhub_app.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat

import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.activity.MainActivity

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Are you sure you want to log out?")
        dialog.setPositiveButton("Yes") { text, listener ->

            activity?.finish()
        }
        dialog.setNegativeButton("No") { _, _ ->
        }
        dialog.create()
        dialog.show()

        return inflater.inflate(R.layout.activity_main, container, false)

    }





}
