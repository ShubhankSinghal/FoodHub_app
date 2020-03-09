package com.shubhank.foodhub_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shubhank.foodhub_app.R
import kotlinx.android.synthetic.main.activity_order_completion.*

class completionActivity : AppCompatActivity() {

    lateinit var orderCompletionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_completion)

        orderCompletionButton = findViewById(R.id.orderCompletionButton)

        orderCompletionButton.setOnClickListener {
            val intent = Intent(this@completionActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
