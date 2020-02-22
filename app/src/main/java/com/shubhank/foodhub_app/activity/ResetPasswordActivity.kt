package com.shubhank.foodhub_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.shubhank.foodhub_app.R

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var otpOtp: EditText
    lateinit var otpNewPassword: EditText
    lateinit var otpConfirmNewPassword: EditText
    lateinit var otpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpOtp = findViewById(R.id.otpOTP)
        otpNewPassword = findViewById(R.id.otpNewPassword)
        otpConfirmNewPassword = findViewById(R.id.otpConfirmNewPassword)
        otpButton = findViewById(R.id.otpButton)

        otpButton.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
