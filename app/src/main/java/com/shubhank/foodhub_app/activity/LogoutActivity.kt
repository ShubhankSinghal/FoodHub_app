package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhank.foodhub_app.R

class LogoutActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val dialog = AlertDialog.Builder(this@LogoutActivity)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Are you sure you want to log out?")
        dialog.setPositiveButton("Yes") { _ , _ ->

            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(this@LogoutActivity, LoginActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
        dialog.setNegativeButton("No") { _ , _ ->

            this.finish()

        }
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()

    }
}
