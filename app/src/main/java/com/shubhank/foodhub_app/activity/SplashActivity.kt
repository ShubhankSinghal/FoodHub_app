package com.shubhank.foodhub_app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.shubhank.foodhub_app.R

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        Handler().postDelayed({

            if(isLoggedIn){

                val mainActivity = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mainActivity)

            }
            else{

                val loginActivity = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(loginActivity)

            }
            finish()
        },1000)
    }
}
