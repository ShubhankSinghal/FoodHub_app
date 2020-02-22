package com.shubhank.foodhub_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.shubhank.foodhub_app.R

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotPasswordMobileNumber : EditText
    lateinit var forgotPasswordEmail : EditText
    lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotPasswordMobileNumber = findViewById(R.id.forgotPasswordMobileNumber)
        forgotPasswordEmail = findViewById(R.id.forgotPasswordEmail)
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton)

        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
