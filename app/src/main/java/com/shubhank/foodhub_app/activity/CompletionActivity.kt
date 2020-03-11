package com.shubhank.foodhub_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.shubhank.foodhub_app.R

class CompletionActivity : AppCompatActivity() {

    private lateinit var orderCompletion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_completion)

        orderCompletion = findViewById(R.id.orderCompletion)

        orderCompletion.setOnClickListener {
            val intent = Intent(this@CompletionActivity, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}
